package eu.xenit.custodian.sentinel.analyzer.gradle;

import eu.xenit.custodian.sentinel.analyzer.AspectAnalysis;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class GradleInfo implements AspectAnalysis {

    private final String version;
    private final String buildDir;
    private final String buildFile;

}
