package eu.xenit.custodian.app.github.adapters.githubclient.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    private long id;
    private String login;

    @JsonProperty("node_id")
    private String nodeId;

    private String url;
}
