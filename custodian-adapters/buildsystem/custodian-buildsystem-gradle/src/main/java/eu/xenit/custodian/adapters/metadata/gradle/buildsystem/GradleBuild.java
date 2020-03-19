package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.ports.spi.build.*;
import eu.xenit.custodian.ports.spi.build.BuildSystem;
import lombok.Getter;

public class GradleBuild implements Build {

//    private final GradleBuild parent;
//    private final Map<String, GradleBuild> subprojects = new LinkedHashMap<>();

    @Getter
    private final GradleProject project;

    public GradleBuild(GradleProject rootProject) {
        this.project = rootProject;
//        this.parent = parent;

//        if (parent != null) {
//            GradleBuild result = this.parent.subprojects.putIfAbsent(name, this);
//            if (result != null) {
//                String exMsg = String.format("Parent project %s already contains a module with name %s", parent, name);
//                throw new IllegalArgumentException(exMsg);
//            }
//        }
    }


    @Override
    public BuildSystem buildsystem() {
        return new GradleBuildSystem();
    }

    @Override
    public String name() {
        return this.getProject().getName();
    }


//    @Override
//    public GradleBuild parent() {
//        return this.parent;
//    }
//
//    @Override
//    public Collection<GradleBuild> subprojects() {
//        return this.subprojects.values();
//    }
//
//    @Override
//    public GradleDependencyContainer dependencies() {
//        return this.dependencies;
//    }
//
//    @Override
//    public GradleRepositoryContainer repositories() {
//        return this.repositories;
//    }
}
