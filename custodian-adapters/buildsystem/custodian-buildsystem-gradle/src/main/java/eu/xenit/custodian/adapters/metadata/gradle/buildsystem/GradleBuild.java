package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.domain.buildsystem.Build;
import eu.xenit.custodian.domain.buildsystem.BuildSystem;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class GradleBuild implements Build<GradleBuild, GradleDependency> {

    private final GradleBuild parent;
    private final Map<String, GradleBuild> subprojects = new LinkedHashMap<>();
    private final String name;

    private final GradleDependencyContainer dependencies = new GradleDependencyContainer();
    private final GradleRepositoryContainer repositories = new GradleRepositoryContainer();

    public GradleBuild(String name) {
        this(name, null);
    }

    public GradleBuild(String name, GradleBuild parent) {
        this.name = name;
        this.parent = parent;

        if (parent != null) {
            GradleBuild result = this.parent.subprojects.putIfAbsent(name, this);
            if (result != null) {
                String exMsg = String.format("Parent project %s already contains a module with name %s", parent, name);
                throw new IllegalArgumentException(exMsg);
            }
        }
    }


    @Override
    public BuildSystem buildsystem() {
        return new GradleBuildSystem();
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public GradleBuild parent() {
        return this.parent;
    }

    @Override
    public Collection<GradleBuild> subprojects() {
        return this.subprojects.values();
    }

    @Override
    public GradleDependencyContainer dependencies() {
        return this.dependencies;
    }

    @Override
    public GradleRepositoryContainer repositories() {
        return this.repositories;
    }
}
