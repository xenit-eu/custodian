package eu.xenit.custodian.app.github.adapters.githubclient;

import com.budjb.httprequests.HttpClient;
import com.budjb.httprequests.HttpClientFactory;
import com.budjb.httprequests.HttpMethod;
import com.budjb.httprequests.HttpRequest;
import com.budjb.httprequests.HttpResponse;
import com.budjb.httprequests.exception.UnsupportedConversionException;
import eu.xenit.custodian.app.github.adapters.githubclient.model.ListInstallationsResponse;
import eu.xenit.custodian.app.github.domain.app.GitHubAppInstallation;
import eu.xenit.custodian.app.github.usecase.ports.JwtProvider;
import eu.xenit.custodian.app.github.usecase.ports.githubclient.GitHubAppClient;
import eu.xenit.custodian.app.github.usecase.ports.githubclient.GitHubAppClientConfig;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class DefaultGitHubAppClient implements GitHubAppClient {

    private final GitHubAppClientConfig config;
    private final JwtProvider jwt;
    private final HttpClient http;

    public DefaultGitHubAppClient(GitHubAppClientConfig config, JwtProvider jwt, HttpClientFactory factory) {
        this(config, jwt, factory.createHttpClient());
    }

    public DefaultGitHubAppClient(GitHubAppClientConfig config, JwtProvider jwt, HttpClient http) {
        this.config = config;
        this.jwt = jwt;
        this.http = http;
    }

    @Override
    public List<GitHubAppInstallation> listInstallations() throws IOException {


        HttpRequest request = new HttpRequest()
                .setUri(this.config.getApiRoot().resolve("/app/installations"))
                .setHeader("Accept", MediaTypes.MACHINE_MAN)
                .setHeader("Authorization", "Bearer " + jwt.get());

        try (HttpResponse response = this.http.execute(HttpMethod.GET, request)) {
            return response.getEntity(ListInstallationsResponse.class)
                    .stream()
                    .map(GitHubAppInstallation.class::cast)
                    .collect(Collectors.toList());
        } catch (UnsupportedConversionException e) {
            throw new RuntimeException(e);
        }
    }

}
