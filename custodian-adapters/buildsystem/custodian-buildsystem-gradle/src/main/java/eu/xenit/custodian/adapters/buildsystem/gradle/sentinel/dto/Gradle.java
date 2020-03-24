package eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Gradle {

    public Gradle(String version) {
        this.version = version;
    }

    private String version;

}
