package eu.xenit.custodian.app.github.adapters;

import static io.jsonwebtoken.SignatureAlgorithm.RS256;

import eu.xenit.custodian.app.github.domain.config.GitHubAppProperties;
import eu.xenit.custodian.app.github.usecase.ports.JwtProvider;
import eu.xenit.custodian.app.github.util.PemUtils;
import eu.xenit.custodian.app.github.util.ResourceLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class GitHubAppJwtProvider implements JwtProvider {

    private final GitHubAppProperties properties;
    private final Clock clock;

    public GitHubAppJwtProvider(GitHubAppProperties properties, Clock clock) {
        this.properties = properties;
        this.clock = clock;
    }

    @Override
    public String get() {
        Instant now = Instant.now(this.clock);

        try {
            return Jwts.builder()
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plus(10, ChronoUnit.MINUTES)))

                    // The issuer is a number in the GH docs, not a String
                    // Using the .setIssuer method results in a claim value with type "String"
                    // .setIssuer(this.properties.getAppId())
                    .claim(Claims.ISSUER, this.properties.getAppId())

                    .signWith(this.getPrivateKey(), RS256)
                    .compact();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    PrivateKey getPrivateKey() throws IOException, GeneralSecurityException {

        URL pemFile = ResourceLoader.getURL(this.properties.getPemPath());
        return PemUtils.loadPrivateKey(pemFile);
    }

}
