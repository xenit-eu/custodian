package eu.xenit.custodian.adapters.model.build.gradle;

import eu.xenit.custodian.domain.model.build.Build;
import eu.xenit.custodian.domain.model.buildsystem.BuildSystem;
import eu.xenit.custodian.adapters.model.buildsystem.gradle.GradleBuildSystem;
import java.util.ArrayList;
import java.util.Collection;

public class GradleBuild implements Build<GradleBuild> {

    private final GradleBuild parent;
    private final Collection<GradleBuild> subprojects = new ArrayList<>();
    private final String name;


    public GradleBuild(String name) {
        this(name, null);
    }

    public GradleBuild(String name, GradleBuild parent) {
        this.name = name;
        this.parent = parent;
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
        return this.subprojects;
    }
}
