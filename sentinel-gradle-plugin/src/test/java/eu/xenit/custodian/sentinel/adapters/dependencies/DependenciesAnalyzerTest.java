package eu.xenit.custodian.sentinel.adapters.dependencies;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.DependencySet;
import org.gradle.api.artifacts.result.ResolutionResult;
import org.gradle.api.internal.artifacts.dependencies.DefaultExternalModuleDependency;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class DependenciesAnalyzerTest {

    private DependenciesAnalyzer analyzer = new DependenciesAnalyzer();

    private Dependency DEP_HTTPCLIENT = dependency("org.apache.httpcomponents", "httpclient", "4.5.1");

    private static Dependency dependency(String group, String name, String version) {
        return new DefaultExternalModuleDependency(group, name, version);
    }

    @Test
    public void analyzeConfigurationWithoutDeclaredDependencies() {

        DependencySet dependencySet = Mockito.mock(DependencySet.class);
        when(dependencySet.stream()).thenReturn(Stream.empty());

        Configuration configuration = Mockito.mock(Configuration.class);
        when(configuration.getName()).thenReturn("implementation");
        when(configuration.getDependencies()).thenReturn(dependencySet);

        ConfigurationDependenciesContainer result = analyzer.analyzeConfiguration(configuration);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("implementation");
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void analyzeResolved() {

        ConfigurationDependenciesContainer result = analyzer
                .analyzeConfiguration("implementation", Stream.of(DEP_HTTPCLIENT));

        assertThat(result.dependencies())
                .hasOnlyOneElementSatisfying(dep -> {
                    assertThat(dep.getGroup()).isEqualTo(DEP_HTTPCLIENT.getGroup());
                    assertThat(dep.getName()).isEqualTo(DEP_HTTPCLIENT.getName());
                    assertThat(dep.getVersion()).isEqualTo(DEP_HTTPCLIENT.getVersion());
                });
    }
}