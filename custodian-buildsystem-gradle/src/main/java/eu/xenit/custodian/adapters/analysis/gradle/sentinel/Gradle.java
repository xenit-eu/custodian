package eu.xenit.custodian.adapters.analysis.gradle.sentinel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Gradle {

    public Gradle(String version) {
        this.version = version;
    }

    private String version;
    private String buildDir = "build";
    private String buildFile = "build.gradle";

}
