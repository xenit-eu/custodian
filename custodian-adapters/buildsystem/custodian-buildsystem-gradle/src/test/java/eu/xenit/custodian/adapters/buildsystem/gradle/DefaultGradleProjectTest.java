package eu.xenit.custodian.adapters.buildsystem.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.domain.buildsystem.GradleProjectAssert;
import java.nio.file.Paths;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DefaultGradleProjectTest {

    @Test
    public void testChildProjectBiDirectionalBinding() {

        GradleProject root = new DefaultGradleProject(Paths.get("."), "root", null);
        GradleProject child = new DefaultGradleProject(Paths.get(".").resolve("child"), "child", root);

        GradleProjectAssert.assertThat(child)
                .hasParent(parent -> assertThat(parent)
                    .isPresent()
                    .hasValueSatisfying(project -> assertThat(project).isEqualTo(root)));

        Assertions.assertThat(root.getChildProjects().iterator())
                .hasSize(1)
                .hasOnlyOneElementSatisfying(element -> assertThat(element).isEqualTo(child));
    }
}