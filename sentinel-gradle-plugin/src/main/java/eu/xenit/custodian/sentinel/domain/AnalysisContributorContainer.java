package eu.xenit.custodian.sentinel.domain;

public class AnalysisContributorContainer extends ItemContainer<String, SentinelAnalysisContributor> {

    public void add(SentinelAnalysisContributor contributor) {
        if (contributor == null) {
            throw new IllegalArgumentException("Contributor must not be null");
        }

        this.add(contributor.getName(), contributor);
    }
}
