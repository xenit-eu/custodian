package eu.xenit.custodian.sentinel.adapters.gradle;

import eu.xenit.custodian.sentinel.domain.JsonPartialReporter;
import eu.xenit.custodian.sentinel.reporting.io.IndentingWriter;
import eu.xenit.custodian.sentinel.reporting.io.JsonWriter;

public class GradleJsonReporter implements JsonPartialReporter<GradleInfo> {

    @Override
    public Runnable report(IndentingWriter writer, GradleInfo gradle) {
        JsonWriter json = new JsonWriter(writer);
        return this.reportGradle(json, gradle);
    }

    private Runnable reportGradle(JsonWriter json, GradleInfo gradle) {
        return json.object(
                json.property("version", gradle.getVersion()),
                json.property("buildDir", gradle.getBuildDir()),
                json.property("buildFile", gradle.getBuildFile())
        );
    }
}
