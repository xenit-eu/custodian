package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto;

import lombok.Data;

@Data
public class Plugin {

    private String id;
    private String version;
    private boolean apply;

}
