package eu.xenit.custodian.gradle.sentinel.plugin;

import eu.xenit.custodian.gradle.sentinel.model.DefaultDependenciesContainer;
import eu.xenit.custodian.gradle.sentinel.model.DefaultGradleBuildModel;
import eu.xenit.custodian.gradle.sentinel.model.DefaultProjectModel;
import eu.xenit.custodian.gradle.sentinel.model.DefaultRepositoriesContainer;
import eu.xenit.custodian.gradle.sentinel.model.GradleBuildModel;
import eu.xenit.custodian.gradle.sentinel.model.PluginContainer;
import eu.xenit.custodian.gradle.sentinel.model.ProjectModel;
import eu.xenit.custodian.gradle.sentinel.plugin.service.PluginRegistrationLookup;
import org.gradle.api.Project;
import org.gradle.api.artifacts.ExternalModuleDependency;
import org.gradle.api.artifacts.repositories.MavenArtifactRepository;
import org.gradle.api.artifacts.repositories.RepositoryContentDescriptor;
import org.gradle.api.internal.artifacts.repositories.RepositoryContentDescriptorInternal;
import org.gradle.tooling.provider.model.ToolingModelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

class SentinelToolingModelBuilder implements ToolingModelBuilder {

    private static final Logger log = LoggerFactory.getLogger(SentinelToolingModelBuilder.class);

    private final PluginRegistrationLookup pluginRegistrationLookup = new PluginRegistrationLookup();

    SentinelToolingModelBuilder() {
        this.pluginRegistrationLookup.loadPlugins(SentinelToolingApiModelPlugin.class.getClassLoader());
    }

    @Override
    public boolean canBuild(String modelName) {
        return modelName.equals(GradleBuildModel.class.getName());
    }

    @Override
    public Object buildAll(String modelName, Project project) {
        Map<String, ProjectModel> projectMap = this.buildAllProjectModels(project.getRootProject());

        return new DefaultGradleBuildModel(projectMap);
    }

    private Map<String, ProjectModel> buildAllProjectModels(Project project) {
        Map<String, ProjectModel> result = project.getChildProjects().values().stream()
                .map(this::buildAllProjectModels)
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));

        result.put(project.getPath(), this.buildProjectModel(project));

        return result;
    }

    public ProjectModel buildProjectModel(Project project) {
        DefaultProjectModel model = new DefaultProjectModel(project.getPath());

        model.setName(project.getName());
        model.setGroup(project.getGroup().toString());
        model.setVersion(project.getVersion().toString());

        model.setProjectDir(project.getProjectDir().toString());
        model.setBuildDir(project.getBuildDir().toString());
        model.setBuildFile(project.getBuildFile().toString());

        this.buildPluginModel(project, model.getPlugins());
        this.buildRepositoriesModel(project, model.getRepositories());
        this.buildDependenciesModel(project, model.getDependencies());

        return model;
    }



    void buildPluginModel(Project project, PluginContainer pluginContainer) {
//        Configuration pluginClasspath = project.getBuildscript().getConfigurations().getAt("classpath");
//        LenientConfiguration lenientConfig = pluginClasspath.getResolvedConfiguration().getLenientConfiguration();
//        Set<ResolvedDependency> dependencies = lenientConfig.getFirstLevelModuleDependencies();
//
//        log.warn("DEPENDENCIES: " + dependencies.size());
//        dependencies.forEach(dep -> {
//            log.warn(dep.getModuleGroup() + ":" + dep.getModuleName() + ":" + dep.getModuleVersion());
//        });

        project.getPlugins().stream()
                .peek(plugin -> log.debug("looking up plugin for implementation class "+plugin.getClass().getName()))
                .map(plugin -> this.pluginRegistrationLookup.lookupPluginByClass(plugin.getClass()))
                .peek(plugin -> {
                    if (plugin.isEmpty()) {
                        log.debug("!! plugin optional was empty");
                    } else {
                        log.debug("-- resolved to '"+plugin.get().getId()+"'");
                    }
                })
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(plugin -> {
                    pluginContainer.add(plugin.getId(), plugin.getImplementationClass(), plugin.getVersion());
                });
    }

    void buildRepositoriesModel(Project project, DefaultRepositoriesContainer repositoriesContainer) {
        project.getRepositories().stream()
                .peek(repo -> log.warn("repo: "+repo.getName()+ " ["+repo.getClass().getName()+"]"))
                .forEach(repo -> {
                    if (repo instanceof MavenArtifactRepository) {
                        MavenArtifactRepository mavenRepo = (MavenArtifactRepository) repo;
                        repositoriesContainer.addMavenRepository(mavenRepo.getUrl().normalize().toString(), mavenRepo.getName());
                    } else {
                        log.warn("ignoring repo type "+repo.getClass().getName());
                    }
                });
    }
    void buildDependenciesModel(Project project, DefaultDependenciesContainer dependencyDto) {
        project.getConfigurations().stream()
                .peek(config -> log.debug("get dependencies from "+config.getName()))
                .forEach(config -> config.getDependencies().forEach(dependency -> {
                    log.debug("-- "+config.getName()+" - "+dependency);

                    if (dependency instanceof ExternalModuleDependency) {
                        dependencyDto.addExternalModule(config.getName(), dependency.getGroup(), dependency.getName(), dependency.getVersion());
                    } else {
                        log.warn("Dependency {} of type {} skipped ...", dependency, dependency.getClass().getName());
                    }
                }));
    }
}
