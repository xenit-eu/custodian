package eu.xenit.custodian.adapters.buildsystem.maven.resolver.adapters.aether;

import lombok.Data;
import org.eclipse.aether.repository.RepositoryPolicy;

@Data
public class MavenResolverConfig {

    /**
     * The location of a repository on the local file system used to cache contents of remote repositories.
     */
    private String localRepository = System.getProperty("java.io.tmpdir") + "/maven";

    /**
     * Controls whether the repository system operates in offline mode and avoids/refuses any access to remote
     * repositories.
     */
    public boolean offline = false;

    /**
     * The update policy. If set, the global update policy overrides the update policies of the remote
     * repositories being used for resolution.
     *
     * May be {@code null}/empty to apply the per-repository policies.
     *
     * @see RepositoryPolicy#UPDATE_POLICY_ALWAYS
     * @see RepositoryPolicy#UPDATE_POLICY_DAILY
     * @see RepositoryPolicy#UPDATE_POLICY_NEVER
     */
    public String updatePolicy = null;

    /**
     * Sets the global checksum policy. If set, the global checksum policy overrides the checksum policies of the remote
     * repositories being used for resolution.
     *
     * May be {@code null}/empty to apply the per-repository policies.
     *
     * @see RepositoryPolicy#CHECKSUM_POLICY_FAIL
     * @see RepositoryPolicy#CHECKSUM_POLICY_IGNORE
     * @see RepositoryPolicy#CHECKSUM_POLICY_WARN
     */
    public String checksumPolicy;

}
