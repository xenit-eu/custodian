package eu.xenit.custodian.adapters.gradle.buildsystem.impl.builder;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleBuild;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.DefaultGradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.PathResolver;
import eu.xenit.custodian.adapters.gradle.buildsystem.impl.ProjectResolver;
import eu.xenit.custodian.util.Arguments;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Setter;

public class GradleProjectTree implements ProjectResolver {

    private final Map<String, GradleProject> projects;

    GradleProjectTree() {
        this.projects = new LinkedHashMap<>();
        ;
    }

    public GradleProject getRootProject() {
        return this.projects.get(":");
    }

    public Stream<GradleProject> stream() {
        return this.projects.values().stream();
    }

    public Optional<GradleProject> getParentProject(GradleProject gradleProject) {
        throw new UnsupportedOperationException("not implemented");
    }

    public void addProject(DefaultGradleProject project) {
        Arguments.notNull(project, "project");
        GradleProject old = this.projects.putIfAbsent(project.getPath(), project);
        if (old != null) {
            throw new IllegalArgumentException(String.format("%s already exists", project.getPath()));
        }
    }

//
//    public Path resolve(Path path) {
//        return this.rootDirSupplier.get().resolve(path);
//    }
//
//
//    public Path resolve(String path) {
//        return this.rootDirSupplier.get().resolve(path);
//    }
//
//    @Override
//    public PathResolver newRelativeResolver(String path) {
//        rootDirSupplier
//        return nu
//    }



}