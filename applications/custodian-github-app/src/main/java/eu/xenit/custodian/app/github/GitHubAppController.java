package eu.xenit.custodian.app.github;

import java.io.IOException;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GitHubAppController {

    @GetMapping("/github/list")
    String list() throws IOException {
        GitHub github = new GitHubBuilder().withJwtToken("my_jwt_token").build();
        return "foo";
    }
}
