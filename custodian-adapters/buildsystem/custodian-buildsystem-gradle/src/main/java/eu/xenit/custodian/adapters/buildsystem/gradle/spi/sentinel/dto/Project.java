package eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Project {

    private String name;
    private String path;
    private String parent;

    private String projectDir;
    private String buildDir;
    private String buildFile;


    @Default
    private Map<String, Project> subprojects = new LinkedHashMap<>();
}
