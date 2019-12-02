package eu.xenit.custodian.sentinel.domain;

import java.util.Objects;

public class AnalysisContributorContainer extends ItemContainer<String, SentinelAnalysisContributor<? extends AnalysisContentPart>> {

    public void add(SentinelAnalysisContributor<? extends AnalysisContentPart> contributor) {
        Objects.requireNonNull(contributor, "Argument 'contributor' is required");

        this.add(contributor.getName(), contributor);
    }
}
