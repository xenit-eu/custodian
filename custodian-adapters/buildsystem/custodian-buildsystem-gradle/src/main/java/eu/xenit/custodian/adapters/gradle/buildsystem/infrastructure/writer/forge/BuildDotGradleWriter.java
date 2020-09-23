package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleArtifactRepository;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDependency;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleDsl;
import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleProject;
import eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.writer.forge.io.IndentingWriter;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class BuildDotGradleWriter {

    static BuildDotGradleWriter createBuildDotGradleWriter(GradleDsl dsl) {
        if (dsl == GradleDsl.GROOVY) {
            return new GroovyDslBuildDotGradleWriter();
        }

        throw new UnsupportedOperationException("dsl '" + dsl + "' not supported");
    }


    public void writeTo(IndentingWriter writer, GradleProject project) {
//        GradleBuildSettings settings = build.getSettings();
//        writeImports(writer, build.tasks());
//        writeBuildscript(writer, build);
        writePlugins(writer, project);
//        writeProperty(writer, "group", settings.getGroup());
//        writeProperty(writer, "version", settings.getVersion());
//        writeJavaSourceCompatibility(writer, settings);
//        writer.println();
//        writeConfigurations(writer, build.configurations());
        writeRepositories(writer, project);
//        writeProperties(writer, build.properties());
        writeDependencies(writer, project);
//        writeBoms(writer, build);
//        writeTasks(writer, build.tasks());
    }


    protected abstract void writePlugins(IndentingWriter writer, GradleProject project);

    protected void writeRepositories(IndentingWriter writer, GradleProject project) {
        writeNestedCollection(writer, "repositories", project.getRepositories().items().collect(Collectors.toList()),
                this::repositoryAsString);
    }

    protected void writeDependencies(IndentingWriter writer, GradleProject project) {
        // TODO do we need to sort the configurations (from implementation -> testImplementation -> ...) ?
        writer.println();
        writer.println("dependencies" + " {");
        writer.indented(
                () -> project.getDependencies().stream().forEach((dependency) -> writeDependency(writer, dependency)));
        writer.println("}");

    }

    protected abstract void writeDependency(IndentingWriter writer, GradleDependency dependency);


    protected abstract String repositoryAsString(GradleArtifactRepository repository);

    protected final <T> void writeNestedCollection(IndentingWriter writer, String name, Collection<T> collection,
            Function<T, String> converter) {
//        this.writeNestedCollection(writer, name, collection, itemToStringConverter, null);
//    }
//
//    protected final <T> void writeNestedCollection(IndentingWriter writer, String name, Collection<T> collection,
//            Function<T, String> converter, Runnable beforeWriting) {
        if (!collection.isEmpty()) {
//            if (beforeWriting != null) {
//                beforeWriting.run();
//            }
            writer.println(name + " {");
            writer.indented(() -> writeCollection(writer, collection, converter));
            writer.println("}");

        }
    }

    protected final <T> void writeCollection(IndentingWriter writer, Collection<T> collection,
            Function<T, String> converter) {
//        writeCollection(writer, collection, converter, null);
//    }
//
//    protected final <T> void writeCollection(IndentingWriter writer, Collection<T> collection,
//            Function<T, String> itemToStringConverter, Runnable beforeWriting) {
//        if (!collection.isEmpty()) {
//            if (beforeWriting != null) {
//                beforeWriting.run();
//            }
        collection.stream().map(converter).forEach(writer::println);
    }
}
