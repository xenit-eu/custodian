package eu.xenit.gradle.sentinel.analyzer.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SentinelAnalysisResult {

    @Getter
    private final ConfigurationContainer configurations;

    @Getter
    private final RepositoriesContainer repositories;

}
