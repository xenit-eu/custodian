package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether;

import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.MavenRepositoryStub;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.domain.ResolverArtifactVersion;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.api.ResolverMavenRepository;
import eu.xenit.custodian.adapters.buildsystem.maven.resolver.spi.ResolverArtifactSpecification;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.SyncContext;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.CollectResult;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.deployment.DeployRequest;
import org.eclipse.aether.deployment.DeployResult;
import org.eclipse.aether.deployment.DeploymentException;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallResult;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.LocalRepositoryManager;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactDescriptorException;
import org.eclipse.aether.resolution.ArtifactDescriptorRequest;
import org.eclipse.aether.resolution.ArtifactDescriptorResult;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.resolution.DependencyResolutionException;
import org.eclipse.aether.resolution.DependencyResult;
import org.eclipse.aether.resolution.MetadataRequest;
import org.eclipse.aether.resolution.MetadataResult;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.resolution.VersionRequest;
import org.eclipse.aether.resolution.VersionResolutionException;
import org.eclipse.aether.resolution.VersionResult;
import org.eclipse.aether.util.version.GenericVersionScheme;
import org.eclipse.aether.version.InvalidVersionSpecificationException;
import org.eclipse.aether.version.Version;
import org.eclipse.aether.version.VersionScheme;

public class RepositorySystemStub implements RepositorySystem {

    private static final VersionScheme VERSION_SCHEME = new GenericVersionScheme();

    private final Collection<MavenRepositoryStub> repositories;

    public RepositorySystemStub(ResolverMavenRepository repo, Stream<ResolverArtifactSpecification> artifacts) {
        this(new MavenRepositoryStub(repo, artifacts));
    }

    public RepositorySystemStub(MavenRepositoryStub... repositories) {
        this(Stream.of(repositories));
    }

    public RepositorySystemStub(Stream<MavenRepositoryStub> repositories) {
        this.repositories = repositories.collect(Collectors.toList());
    }

    @Override
    public VersionRangeResult resolveVersionRange(RepositorySystemSession session, VersionRangeRequest request) {

        var versionIndex = new LinkedHashMap<ResolverArtifactVersion, ResolverMavenRepository>();

        // convert request to domain artifact-spec
        var spec = convertToArtifactSpec(request.getArtifact());

        // resolve from each repository and collect in the versionIndex
        this.repositories.stream()
                .peek(repo -> {
                    // resolving artifact from 'repo'
                    // QUESTION do we need to filter the repositories from the request ?
                })
                .map(repo -> {
                    var versions = repo.resolveVersionRange(spec);
                    return versions.collect(Collectors.toMap(v -> v, v -> repo));
                })
                .forEach(index -> {
                    index.forEach(versionIndex::putIfAbsent);
                });

        // prepare the result object
        var result = new VersionRangeResult(request);

        versionIndex.entrySet().stream()

                // ensure natural ordering of the version-key
                .sorted(Entry.comparingByKey())

                // record each version-repo entry
                .forEach(entry -> {
                    var version = convert(entry.getKey());
                    var repo = convert(entry.getValue());
                    // versions need to be recorded in 2 different ways in the result object
                    // - once as a repo-to-version mapping
                    result.setRepository(version, repo);
                    // - once as a simple (list of) versions
                    result.addVersion(version);
                });

        return result;
    }

    private Version convert(ResolverArtifactVersion version) {
        try {
            return VERSION_SCHEME.parseVersion(version.getValue());
        } catch (InvalidVersionSpecificationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private RemoteRepository convert(ResolverMavenRepository repository) {
        return new RemoteRepository.Builder(repository.getId(), null, repository.getUrl()).build();
    }

    private static ResolverArtifactSpecification convertToArtifactSpec(Artifact artifact) {
        return ResolverArtifactSpecification.from(
                artifact.getGroupId(),
                artifact.getArtifactId(),
                artifact.getVersion(),
                artifact.getClassifier(),
                artifact.getExtension());
    }

    @Override
    public LocalRepositoryManager newLocalRepositoryManager(RepositorySystemSession session,
            LocalRepository localRepository) {
        return null;
    }

    @Override
    public VersionResult resolveVersion(RepositorySystemSession session, VersionRequest request)
            throws VersionResolutionException {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public ArtifactDescriptorResult readArtifactDescriptor(RepositorySystemSession session,
            ArtifactDescriptorRequest request) throws ArtifactDescriptorException {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public CollectResult collectDependencies(RepositorySystemSession session, CollectRequest request)
            throws DependencyCollectionException {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public DependencyResult resolveDependencies(RepositorySystemSession session, DependencyRequest request)
            throws DependencyResolutionException {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public ArtifactResult resolveArtifact(RepositorySystemSession session, ArtifactRequest request)
            throws ArtifactResolutionException {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public List<ArtifactResult> resolveArtifacts(RepositorySystemSession session,
            Collection<? extends ArtifactRequest> requests) throws ArtifactResolutionException {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public List<MetadataResult> resolveMetadata(RepositorySystemSession session,
            Collection<? extends MetadataRequest> requests) {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public InstallResult install(RepositorySystemSession session, InstallRequest request) throws InstallationException {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public DeployResult deploy(RepositorySystemSession session, DeployRequest request) throws DeploymentException {
        throw new UnsupportedOperationException("not supported by stub");
    }


    @Override
    public SyncContext newSyncContext(RepositorySystemSession session, boolean shared) {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public List<RemoteRepository> newResolutionRepositories(RepositorySystemSession session,
            List<RemoteRepository> repositories) {
        throw new UnsupportedOperationException("not supported by stub");
    }

    @Override
    public RemoteRepository newDeploymentRepository(RepositorySystemSession session, RemoteRepository repository) {
        throw new UnsupportedOperationException("not supported by stub");
    }
}
