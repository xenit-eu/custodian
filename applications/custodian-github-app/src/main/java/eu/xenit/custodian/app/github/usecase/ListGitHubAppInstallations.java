package eu.xenit.custodian.app.github.usecase;

import eu.xenit.custodian.app.github.domain.app.GitHubAppInstallation;
import eu.xenit.custodian.app.github.usecase.ports.githubclient.GitHubAppClient;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ListGitHubAppInstallations {

    private final GitHubAppClient client;

    public ListGitHubAppInstallations(GitHubAppClient client) {
        this.client = client;
    }

    // probably need to convert this to something that supports paging
    public List<GitHubAppInstallation> run() {
        try {
            return client.listInstallations();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
