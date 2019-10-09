package eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Report {

    @Default
    private Project project = new Project();

    @Default
    private Gradle gradle = new Gradle();

    @Default
    private List<RepositoryDto> repositories = new ArrayList<>();

    @Singular
    private Map<String, Configuration> configurations;
}
