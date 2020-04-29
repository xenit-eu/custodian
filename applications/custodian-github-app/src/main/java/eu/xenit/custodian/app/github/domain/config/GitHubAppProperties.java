package eu.xenit.custodian.app.github.domain.config;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class GitHubAppProperties {

    long appId;
    String pemPath;

}
