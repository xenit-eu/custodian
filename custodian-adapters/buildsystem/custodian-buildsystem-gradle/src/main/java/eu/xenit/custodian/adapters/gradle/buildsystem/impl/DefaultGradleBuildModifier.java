package eu.xenit.custodian.adapters.gradle.buildsystem.impl;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleBuildModifier;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.domain.usecases.updates.Patch;
import eu.xenit.custodian.ports.spi.buildsystem.BuildModification;
import eu.xenit.custodian.ports.spi.buildsystem.ProjectModuleDependencyModifier;
import eu.xenit.custodian.util.Arguments;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultGradleBuildModifier implements GradleBuildModifier {

    public DefaultGradleBuildModifier() {
    }

    @Override
    public BuildModification updateDependency(GradleProject project, GradleModuleDependency dependency,
            Consumer<ProjectModuleDependencyModifier> callback) {

        GradleProjectModuleDependencyModifier modifier = new GradleProjectModuleDependencyModifier(project, dependency);

        callback.accept(modifier);

        return modifier.getModification();
    }


    static class GradleBuildModification implements BuildModification {

        private final List<Patch> patches = new ArrayList<>();

        @Override
        public Stream<Patch> getPatches() {
            return this.patches.stream();
        }

        @Override
        public boolean isEmpty() {
            return this.patches.isEmpty();
        }

        void add(Patch patch) {
            this.patches.add(Arguments.notNull(patch, "patch"));
        }

        void addAll(Stream<Patch> patches) {
            Arguments.notNull(patches, "patches");
            patches.forEach(this.patches::add);
        }

    }

}
