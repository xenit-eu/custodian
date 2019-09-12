package eu.xenit.custodian.sentinel.analyzer;

import com.fasterxml.jackson.annotation.JsonFormat;
import eu.xenit.custodian.sentinel.analyzer.dependencies.ConfigurationContainer;
import eu.xenit.custodian.sentinel.analyzer.gradle.GradleInfo;
import eu.xenit.custodian.sentinel.analyzer.project.ProjectInformation;
import eu.xenit.custodian.sentinel.analyzer.repositories.RepositoriesContainer;
import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class SentinelAnalysisResult {

//    @Getter
//    @Default
//    private final Map<String, AspectAnalysis> analysisMap

    @Getter
    @Default
    private final ProjectInformation project = ProjectInformation.builder().build();

    @Getter
    @Default
    private final GradleInfo gradle = GradleInfo.builder().build();

    @Getter
    @Default
    private final ConfigurationContainer configurations = new ConfigurationContainer();

    @Getter
    @Default
    @JsonFormat(shape=JsonFormat.Shape.ARRAY)
    private final RepositoriesContainer repositories = new RepositoriesContainer();

}
