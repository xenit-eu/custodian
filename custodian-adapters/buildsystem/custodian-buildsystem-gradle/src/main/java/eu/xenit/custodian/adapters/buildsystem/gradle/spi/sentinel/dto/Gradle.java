package eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto;

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
