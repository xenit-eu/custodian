package eu.xenit.custodian.sentinel.analyzer.dependencies;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import eu.xenit.custodian.sentinel.analyzer.dependencies.DependenciesAnalyzer;
import eu.xenit.custodian.sentinel.analyzer.dependencies.DependencyContainer;
import java.util.stream.Stream;
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
    public void analyzeEmpty() {

        DependencySet dependencySet = Mockito.mock(DependencySet.class);
        ResolutionResult resolution = Mockito.mock(ResolutionResult.class);

        DependencyContainer dependencies = analyzer.analyzeIncomingDependencies(dependencySet, resolution);

        assertThat(dependencies.isEmpty()).isTrue();
    }

    @Test
    public void analyzeResolved() {

        DependencySet dependencySet = Mockito.mock(DependencySet.class, InvocationOnMock::callRealMethod);
//        when(dependencySet.forEach();)
        when(dependencySet.stream()).thenReturn(Stream.of(DEP_HTTPCLIENT));

        ResolutionResult resolution = Mockito.mock(ResolutionResult.class);

        DependencyContainer dependencies = analyzer.analyzeIncomingDependencies(dependencySet, resolution);

        assertThat(dependencies.items())
                .hasSize(1)
                ;
    }
}