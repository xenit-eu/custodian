package eu.xenit.custodian.adapters.buildsystem.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.SentinelGradleProjectAnalyzer;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.DependencyFixtures;
import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.Report;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.junit.Test;

public class SentinelReportParserTest {

    private JacksonSentinelReportParser parser = new JacksonSentinelReportParser();

    private InputStream getSampleReportInputStream() {
        String path = "sample/" + SentinelGradleProjectAnalyzer.SENTINEL_REPORT_OUTPUT;
        return this.getClass().getClassLoader().getResourceAsStream(path);
    }

    @Test
    public void parseSample() throws IOException {

        readStreamAsText(this.getSampleReportInputStream(), System.out::println);

        InputStream inputStream = this.getSampleReportInputStream();
        Report report = parser.parse(inputStream);

        assertThat(report)
                .isNotNull()
                .satisfies(r -> assertThat(r.getGradle())
                        .isNotNull()
                        .satisfies(g -> assertThat(g.getVersion()).isEqualTo("5.6"))
                )
                .satisfies(r -> assertThat(r.getProject())
                        .isNotNull()
                        .satisfies(p -> assertThat(p.getName()).isEqualTo("simple"))
                        .satisfies(p -> assertThat(p.getBuildDir()).isEqualTo("build"))
                        .satisfies(p -> assertThat(p.getBuildFile()).isEqualTo("build.gradle"))
                        .satisfies(p -> assertThat(p.getPath()).isEqualTo(":"))
                        .satisfies(p -> assertThat(p.getProjectDir()).isEqualTo(""))
                        .satisfies(p -> assertThat(p.getSubprojects()).isEmpty())
                )
                .satisfies(r -> assertThat(r.getDependencies()).satisfies(d -> {
                    assertThat(d.get("implementation"))
                            .containsExactly(DependencyFixtures.apacheHttpClient());
                    assertThat(d.get("testImplementation"))
                            .containsExactly(DependencyFixtures.junit());
                }));
    }


    private void readStreamAsText(InputStream inputStream, Consumer<String> callback) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            br.lines().forEach(callback);
        }
    }
}