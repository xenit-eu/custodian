package eu.xenit.custodian.sentinel.adapters.gradle;

import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class GradleInfo implements AnalysisContentPart {

    private final String version;
    private final String buildDir;
    private final String buildFile;

}
