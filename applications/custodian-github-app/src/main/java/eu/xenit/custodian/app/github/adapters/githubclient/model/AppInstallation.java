package eu.xenit.custodian.app.github.adapters.githubclient.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInstallation {

    private long id;
    private Account account;

    @JsonProperty("access_tokens_url")
    private String accessTokenUrl;

    @JsonProperty("repositories_url")
    private String repositoriesUrl;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("app_id")
    private long appId;

}
