package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.VersionRangeQueryResult;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.MavenResolverPort;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.impl.DefaultServiceLocator;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResolutionException;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.version.Version;

@Slf4j
public class MavenResolverAdapter implements MavenResolverPort {

    private final RepositorySystem repositorySystem;
    private final RepositorySystemSession session;



    public MavenResolverAdapter() {
        this(new MavenResolverConfig());
    }

    public MavenResolverAdapter(MavenResolverConfig mavenProperties) {
        this(mavenProperties, createDefaultRepositorySystem());
    }

    public MavenResolverAdapter(RepositorySystem repositorySystem) {
        this(new MavenResolverConfig(), repositorySystem);
    }

    public MavenResolverAdapter(MavenResolverConfig mavenProperties, RepositorySystem repositorySystem) {
        Objects.requireNonNull(mavenProperties, "Argument 'mavenProperties' is required");
        Objects.requireNonNull(mavenProperties, "Argument 'repositorySystem' is required");

        this.repositorySystem = repositorySystem;
        this.session = newRepositorySystemSession(this.repositorySystem, mavenProperties);
    }



    @Override
    public VersionRangeQueryResult resolveVersionRange(Collection<ResolverMavenRepository> repositories,
            ResolverArtifactSpecification spec) {

        try {
            VersionRangeRequest rangeRequest = new VersionRangeRequest();
            rangeRequest.setArtifact(this.convert(spec));
            rangeRequest.setRepositories(this.convert(repositories));

            VersionRangeResult rangeResult = this.repositorySystem.resolveVersionRange(session, rangeRequest);

            List<Version> versions = rangeResult.getVersions();
            log.info("artifact {} -> {}", this.convert(spec).toString(), versions);

            return new VersionRangeQueryResultAdapter(rangeResult);
        } catch (VersionRangeResolutionException e) {
            throw new RuntimeException(e);
        }


    }

    private List<RemoteRepository> convert(Collection<ResolverMavenRepository> repositories) {
        return repositories.stream().map(this::convert).collect(Collectors.toList());
    }

    private RemoteRepository convert(ResolverMavenRepository repository) {
        return new RemoteRepository.Builder(repository.getId(), "default", repository.getUrl()).build();
    }

    private Artifact convert(ResolverArtifactSpecification coordinates) {
        return new DefaultArtifact(
                coordinates.getGroupId(),
                coordinates.getArtifactId(),
                coordinates.getClassifier(),
                coordinates.getExtension(),
                coordinates.getVersion());
    }


    /**
     * Aether's components implement {@link org.eclipse.aether.spi.locator.Service} to ease manual wiring. Using the
     * prepopulated {@link DefaultServiceLocator}, we need to register the repository connector and transporter
     * factories
     */
    private static RepositorySystem createDefaultRepositorySystem() {
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
    private RepositorySystemSession newRepositorySystemSession(RepositorySystem system, MavenResolverConfig config) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();

        LocalRepository localRepository = new LocalRepository(config.getLocalRepository());
        LocalRepositoryManager localRepositoryManager = system.newLocalRepositoryManager(session, localRepository);
        session.setLocalRepositoryManager(localRepositoryManager);

        session.setOffline(config.isOffline());
        session.setUpdatePolicy(config.getUpdatePolicy());
        session.setChecksumPolicy(config.getChecksumPolicy());

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
