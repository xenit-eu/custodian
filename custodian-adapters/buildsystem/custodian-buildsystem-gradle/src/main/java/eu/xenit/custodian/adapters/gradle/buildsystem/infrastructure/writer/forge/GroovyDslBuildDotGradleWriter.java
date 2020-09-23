package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactRepository;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactRepository.GradleRepositoryType;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradlePlugin;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.MavenRepository;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge.io.IndentingWriter;
import java.util.stream.Collectors;

public class GroovyDslBuildDotGradleWriter extends BuildDotGradleWriter {


    protected void writePlugins(IndentingWriter writer, GradleProject project) {
        writeNestedCollection(writer, "plugins",
                project.getPlugins().stream().collect(Collectors.toList()),
                this::pluginAsString);
        writer.println();
    }

    @Override
    protected void writeDependency(IndentingWriter writer, GradleDependency dependency) {
        String quoteStyle = "\"";
        String version = dependency.getVersion();
        String type = null;

        boolean hasExclusions = dependency instanceof GradleModuleDependency && !((GradleModuleDependency) dependency).getExcludeRules().isEmpty();
        writer.print(dependency.getTargetConfiguration());
        writer.print((hasExclusions) ? "(" : " ");
        writer.print(quoteStyle + dependency.getGroup() + ":" + dependency.getName()
                + ((version != null) ? ":" + version : "") + ((type != null) ? "@" + type : "") + quoteStyle);
        if (hasExclusions) {
            throw new UnsupportedOperationException("exclusions not yet implemented");
//            writer.println(") {");
//            writer.indented(
//                    () -> writeCollection(writer, dependency.getExclusions(), this::dependencyExclusionAsString));
//            writer.println("}");
        }
        else {
            writer.println();
        }
    }


    @Override
    protected String repositoryAsString(GradleArtifactRepository repository) {
        if (repository.getType() != GradleRepositoryType.MAVEN) {
            throw new UnsupportedOperationException("Repository type '" + repository.getType() + "' not implemented");
        }

        if (MavenRepository.mavenCentral().getUrl().equalsIgnoreCase(repository.getUrl())) {
            return "mavenCentral()";
        }

        return "maven { url '" + repository.getUrl() + "' }";
    }


    private String pluginAsString(GradlePlugin plugin) {
        String string = "id '" + plugin.getId() + "'";
        if (plugin.getVersion() != null) {
            string += " version '" + plugin.getVersion() + "'";

            if (!plugin.isApplied()) {
                string += " apply false";
            }
        }
        return string;
    }
}
