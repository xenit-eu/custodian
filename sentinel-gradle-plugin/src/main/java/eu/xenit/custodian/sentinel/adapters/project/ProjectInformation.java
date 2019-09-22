package eu.xenit.custodian.sentinel.adapters.project;

import eu.xenit.custodian.sentinel.domain.AnalysisContentPart;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ProjectInformation implements AnalysisContentPart {

    private final String name;
    private final String path;
    private final String displayName;
    private final String parent;
    private final String projectDir;

    @Default
    private final Map<String, ProjectInformation> subprojects = new HashMap<>();


}
