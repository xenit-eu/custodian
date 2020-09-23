package eu.xenit.custodian.adapters.gradle.buildsystem.infrastructure.model.sentinel.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Report {

    @Default
    private Project project = new Project();

    @Default
    private Gradle gradle = new Gradle();

    @Default
    private List<Plugin> plugins = new ArrayList<>();

    @Default
    private List<RepositoryDto> repositories = new ArrayList<>();

    @Default
    private Dependencies dependencies = new Dependencies();

    public DependencySet getDependencies(String configuration) {
        return this.dependencies.get(configuration);
    }
}
