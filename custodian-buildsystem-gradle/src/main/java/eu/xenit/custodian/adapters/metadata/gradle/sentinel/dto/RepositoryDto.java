package eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto;


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
public class RepositoryDto {

    public enum RepositoryType {
        MAVEN,
        IVY
    }

    private String name;
    private RepositoryType type;
    private String url;

    @Default
    private Collection<String> metadataSources = new ArrayList<>();
    // TODO ivy repo


    public static RepositoryDto mavenCentral() {
        return RepositoryDto.builder()
                .name("MavenRepo")
                .type(RepositoryType.MAVEN)
                .url("https://repo.maven.apache.org/maven2/")
                .metadataSources(Arrays.asList("mavenPom", "artifact"))
                .build();
    }

}
