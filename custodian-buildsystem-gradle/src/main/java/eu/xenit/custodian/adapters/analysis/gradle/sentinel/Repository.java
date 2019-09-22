package eu.xenit.custodian.adapters.analysis.gradle.sentinel;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Repository {

    private String name;
    private String type;
    private String url;

    @Default
    private Collection<String> metadataSources = new ArrayList<>();
    // TODO ivy repo


    public static Repository mavenCentral() {
        return Repository.builder()
                .name("MavenRepo")
                .type("maven")
                .url("https://repo.maven.apache.org/maven2/")
                .metadataSources(Arrays.asList("mavenPom", "artifact"))
                .build();
    }

}
