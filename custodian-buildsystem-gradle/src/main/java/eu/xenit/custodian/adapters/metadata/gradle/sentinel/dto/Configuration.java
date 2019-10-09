package eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto;

import java.util.ArrayList;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class Configuration {

    @Default
    private Collection<Dependency> dependencies = new ArrayList<>();
}
