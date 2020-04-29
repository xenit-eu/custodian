package eu.xenit.custodian.app.github.adapters.githubclient;

import eu.xenit.custodian.app.github.domain.config.GitHubAppProperties;
import eu.xenit.custodian.app.github.usecase.ports.githubclient.GitHubAppClientConfig;
import eu.xenit.custodian.app.github.util.PemUtils;
import eu.xenit.custodian.app.github.util.ResourceLoader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

public class DefaultGitHubAppClientConfig implements GitHubAppClientConfig {

    private final GitHubAppProperties properties;

    public DefaultGitHubAppClientConfig(GitHubAppProperties properties) {

        this.properties = properties;
    }

    @Override
    public URI getApiRoot() {
        try {
            return new URI("https://api.github.com/");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getAppId() {
        return this.properties.getAppId();
    }

    @Override
    public PrivateKey getPrivateKey() throws IOException, GeneralSecurityException {

        URL pemFile = ResourceLoader.getURL(this.properties.getPemPath());
        return PemUtils.loadPrivateKey(pemFile);
    }
}
