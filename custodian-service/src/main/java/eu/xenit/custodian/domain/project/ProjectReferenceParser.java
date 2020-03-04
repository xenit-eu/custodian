package eu.xenit.custodian.domain.project;

import eu.xenit.custodian.ports.api.ProjectReference;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.util.Assert;

public class ProjectReferenceParser {


    public static ProjectReference from(URI uri) {
        return new DefaultProjectReference(uri);
    }

    public static ProjectReference from(Path path) {
        Assert.notNull(path, "path must not be null");
        return new DefaultProjectReference(path.toUri());
    }

    public static ProjectReference from(String from) {
        Assert.notNull(from, "from must not be null");

        // The shorter scp-like syntax for the git-ssh-links would be rejected:
        // Examples:
        // git@github.com:xenit-eu/custodian.git
        // git@bitbucket.org:xenit/custodian.git
        if (from.startsWith("git@") && from.endsWith(".git")) {
            from = "ssh://" +from;
        }

        URI uri = URI.create(from);
        if (!uri.isAbsolute()) {
            // relative uri's (= not specifying scheme) are interpreted as Path's (~ local file://)
            uri = Paths.get(uri.toString()).toUri();
        }

        uri = uri.normalize();

        return new DefaultProjectReference(uri);
    }
}
