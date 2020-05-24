package eu.xenit.custodian.adapters.channel.mavenrepo;

import eu.xenit.custodian.adapters.buildsystem.maven.MavenArtifactSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenVersionRangeQueryResult;
import java.util.LinkedList;
import java.util.List;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.version.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMavenArtifactResolver implements MavenArtifactResolver {

    private static final Logger log = LoggerFactory.getLogger(DefaultMavenArtifactResolver.class);

    private final RepositorySystem repositorySystem;
    private final List<RemoteRepository> remoteRepositories = new LinkedList<>();

    public DefaultMavenArtifactResolver() {

        this.remoteRepositories.add(
                new RemoteRepository.Builder("maven-central", "default", "https://repo.maven.apache.org/maven2/")
                        .build()
        );

        this.repositorySystem = newRepositorySystem();
    }

    @Override
    public MavenVersionRangeQueryResult resolve(MavenArtifactSpecification coordinates) {
        Artifact resolverApiArtifact = new DefaultArtifact(
                coordinates.getModuleId().getGroup(),
                coordinates.getModuleId().getName(),
                coordinates.getClassifier(),
                coordinates.getExtension(),
                coordinates.getVersionSpec().toString());

        VersionRangeResult updatedVersionsInRange = this.getUpdatedVersionsInRange(resolverApiArtifact);

        return new MavenVersionRangeQueryResultAdapter(updatedVersionsInRange);
    }


    private VersionRangeResult getUpdatedVersionsInRange(Artifact artifact) {
        RepositorySystemSession session = newRepositorySystemSession(this.repositorySystem, "/tmp/maven/");

        try {
            VersionRangeRequest rangeRequest = new VersionRangeRequest();
            rangeRequest.setArtifact(artifact);
            rangeRequest.setRepositories(this.remoteRepositories);

            VersionRangeResult rangeResult = this.repositorySystem.resolveVersionRange(session, rangeRequest);

            List<Version> versions = rangeResult.getVersions();
            log.info("artifact {} -> {}", artifact.toString(), versions);

            return rangeResult;
        } catch (VersionRangeResolutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Aether's components implement {@link org.eclipse.aether.spi.locator.Service} to ease manual wiring.
     * Using the prepopulated {@link DefaultServiceLocator}, we need to register the repository connector
     * and transporter factories
     */
    private RepositorySystem newRepositorySystem() {
        DefaultServiceLocator locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        locator.setErrorHandler(new DefaultServiceLocator.ErrorHandler() {
            @Override
            public void serviceCreationFailed(Class<?> type, Class<?> impl, Throwable exception) {
                throw new RuntimeException(exception);
            }
        });
        return locator.getService(RepositorySystem.class);
    }

    /*
     * Create a session to manage remote and local synchronization.
     */
    private RepositorySystemSession newRepositorySystemSession(RepositorySystem system, String localRepoPath) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
        LocalRepository localRepo = new LocalRepository(localRepoPath);
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
//        session.setOffline(this.properties.isOffline());
//        session.setUpdatePolicy(this.properties.getUpdatePolicy());
//        session.setChecksumPolicy(this.properties.getChecksumPolicy());
//        if (this.properties.isEnableRepositoryListener()) {
//            session.setRepositoryListener(new ConsoleRepositoryListener());
//        }
//        if (this.properties.getConnectTimeout() != null) {
//            session.setConfigProperty(ConfigurationProperties.CONNECT_TIMEOUT, this.properties.getConnectTimeout());
//        }
//        if (this.properties.getRequestTimeout() != null) {
//            session.setConfigProperty(ConfigurationProperties.REQUEST_TIMEOUT, this.properties.getRequestTimeout());
//        }
//        if (isProxyEnabled()) {
//            DefaultProxySelector proxySelector = new DefaultProxySelector();
//            Proxy proxy = new Proxy(this.properties.getProxy().getProtocol(),
//                    this.properties.getProxy().getHost(),
//                    this.properties.getProxy().getPort(),
//                    this.authentication);
//            proxySelector.add(proxy, this.properties.getProxy().getNonProxyHosts());
//            session.setProxySelector(proxySelector);
//        }
        return session;
    }

}
