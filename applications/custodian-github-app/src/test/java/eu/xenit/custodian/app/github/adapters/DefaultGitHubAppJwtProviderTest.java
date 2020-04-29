package eu.xenit.custodian.app.github.adapters;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.app.github.domain.config.GitHubAppProperties;
import eu.xenit.custodian.app.github.usecase.ports.JwtProvider;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.Test;

public class DefaultGitHubAppJwtProviderTest {

    private GitHubAppProperties props = new GitHubAppProperties()
            .setAppId(61069)
            .setPemPath("classpath:test-pkcs-1.pem");

    @Test
    public void getPrivateKey() throws IOException, GeneralSecurityException {
        GitHubAppJwtProvider jwtProvider = new GitHubAppJwtProvider(props, Clock.systemUTC());
        PrivateKey privateKey = jwtProvider.getPrivateKey();

        assertThat(privateKey).isNotNull();
    }

    @Test
    public void testJwtToken() throws IOException {
        // This example-jwt.txt is generated using the example ruby script from
        // https://developer.github.com/apps/building-github-apps/authenticating-with-github-apps/
        final String expected = readJwt("example-jwt.txt");

        // the example jwt uses claim 'issued-at': 1587502411
        Clock clock = Clock.fixed(Instant.ofEpochSecond(1587502411L), ZoneOffset.UTC);

        JwtProvider jwtProvider = new GitHubAppJwtProvider(props, clock);

        String jwt = jwtProvider.get();
        assertThat(jwt).isEqualTo(expected);
    }

    private static String readJwt(String filename) throws IOException {
        final String expected;
        try (InputStream stream = DefaultGitHubAppJwtProviderTest.class.getClassLoader().getResourceAsStream(filename)) {
            assertThat(stream).isNotNull();
            expected = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
        return expected;
    }
}