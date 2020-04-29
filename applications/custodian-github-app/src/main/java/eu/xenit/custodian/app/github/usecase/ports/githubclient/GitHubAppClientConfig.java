package eu.xenit.custodian.app.github.usecase.ports.githubclient;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

public interface GitHubAppClientConfig {

    URI getApiRoot();

    long getAppId();
    PrivateKey getPrivateKey() throws IOException, GeneralSecurityException;

}
