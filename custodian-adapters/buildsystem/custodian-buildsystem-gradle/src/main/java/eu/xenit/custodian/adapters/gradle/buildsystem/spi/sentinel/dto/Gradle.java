package eu.xenit.custodian.adapters.gradle.buildsystem.spi.sentinel.dto;

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
