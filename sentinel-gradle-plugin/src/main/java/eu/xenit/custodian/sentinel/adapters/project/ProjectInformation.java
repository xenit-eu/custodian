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

    /**
     * The directory containing the project build file, relative to the root directory
     */
    private final String projectDir;

    /**
     * The name of the build script for this project
     */
    private final String buildFile;

    /**
     * The build directory of this project, relative to the project directory
     */
    private final String buildDir;

    @Default
    private final Map<String, ProjectInformation> subprojects = new HashMap<>();

    @Override
    public String getContributionName() {
        return ProjectInfoAnalysisContributor.NAME;
    }
}
