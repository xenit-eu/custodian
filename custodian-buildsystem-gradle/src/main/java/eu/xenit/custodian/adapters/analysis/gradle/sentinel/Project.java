package eu.xenit.custodian.adapters.analysis.gradle.sentinel;

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

    @Default
    private String projectDir = "";

    @Default
    private Map<String, Project> subprojects = new LinkedHashMap<>();
}
