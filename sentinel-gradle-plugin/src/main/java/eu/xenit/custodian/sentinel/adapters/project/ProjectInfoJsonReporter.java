package eu.xenit.custodian.sentinel.adapters.project;

import eu.xenit.custodian.sentinel.domain.JsonPartialReporter;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import eu.xenit.custodian.sentinel.reporting.io.JsonWriter;

public class ProjectInfoJsonReporter implements JsonPartialReporter<ProjectInformation> {

    public ProjectInfoJsonReporter() {
    }

    @Override
    public Runnable report(IndentingWriter writer, ProjectInformation project) {
        JsonWriter json = new JsonWriter(writer);
        return this.report(json, project);
    }

    private Runnable report(JsonWriter json, ProjectInformation project) {
        return json.object(
                json.property("name", project.getName()),
                json.property("path", project.getPath()),
                json.property("projectDir", project.getProjectDir()),
                json.property("buildDir", project.getBuildDir()),
                json.property("buildFile", project.getBuildFile()),
                json.property("subprojects", json.object(
                        project.getSubprojects().values().stream()
                                .map(child -> json.property(child.getName(), report(json, child)))
                ))
        );

//                property(writer, "displayName", project.getDisplayName()),
//                property(writer, "subprojects", () -> writer.writeObject(
//                        project.getSubprojects().values()
//                                .stream()
//                                .map(child -> property(writer, child.getName(), projectInfo(writer, child)))
//                                .filter(Optional::isPresent).map(Optional::get))
//                        , () -> !project.getSubprojects().isEmpty())
//        );
    }


}
