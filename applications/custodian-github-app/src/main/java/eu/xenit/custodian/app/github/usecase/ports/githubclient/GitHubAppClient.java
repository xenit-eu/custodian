package eu.xenit.custodian.app.github.usecase.ports.githubclient;

import eu.xenit.custodian.app.github.domain.app.GitHubAppInstallation;
import java.io.IOException;
import java.util.List;

public interface GitHubAppClient {

    List<GitHubAppInstallation> listInstallations() throws IOException;
}
