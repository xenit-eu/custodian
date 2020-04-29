package eu.xenit.custodian.app.github.adapters.githubclient;

import com.budjb.httprequests.HttpClientFactory;
import com.budjb.httprequests.converter.EntityConverterManager;
import com.budjb.httprequests.filter.jackson.JacksonListReader;
import com.budjb.httprequests.filter.jackson.JacksonListWriter;
import com.budjb.httprequests.reference.ReferenceHttpClientFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.xenit.custodian.app.github.adapters.GitHubAppJwtProvider;
import eu.xenit.custodian.app.github.domain.config.GitHubAppProperties;
import eu.xenit.custodian.app.github.usecase.ports.JwtProvider;
import eu.xenit.custodian.app.github.usecase.ports.githubclient.GitHubAppClient;
import eu.xenit.custodian.app.github.usecase.ports.githubclient.GitHubAppClientConfig;
import java.io.IOException;
import java.time.Clock;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Ignore;
import org.junit.Test;

public class DefaultGitHubAppClientTest {

    private final GitHubAppProperties properties = new GitHubAppProperties()
            .setAppId(61069)
            .setPemPath("~/xenit-custodian.private-key.pem");

    private final GitHubAppClientConfig config = new DefaultGitHubAppClientConfig(properties);

    private final JwtProvider jwtProvider = createJwtProvider(properties);
    private final HttpClientFactory httpClientFactory = new ReferenceHttpClientFactory(
            new EntityConverterManager(Arrays.asList(
                    new JacksonListReader(new ObjectMapper()))));

    @Test
    @Ignore("work in progress")
    public void listInstallations() throws IOException {
        GitHubAppClient client = new DefaultGitHubAppClient(config, jwtProvider, httpClientFactory);
        client.listInstallations();
    }


    private static JwtProvider createJwtProvider(GitHubAppProperties properties) {
        return new GitHubAppJwtProvider(properties, Clock.system(ZoneOffset.UTC));
    }

}