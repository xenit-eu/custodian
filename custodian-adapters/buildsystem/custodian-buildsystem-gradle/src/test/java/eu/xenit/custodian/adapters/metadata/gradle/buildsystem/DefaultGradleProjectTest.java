package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import static org.assertj.core.api.Assertions.assertThat;

import eu.xenit.custodian.asserts.build.buildsystem.GradleProjectAssert;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class DefaultGradleProjectTest {

    @Test
    public void testChildProjectBiDirectionalBinding() {

        GradleProject root = new DefaultGradleProject("root", null);
        GradleProject child = new DefaultGradleProject("child", root);

        GradleProjectAssert.assertThat(child)
                .hasParent(parent -> assertThat(parent)
                    .isPresent()
                    .hasValueSatisfying(project -> assertThat(project).isEqualTo(root)));

        Assertions.assertThat(root.getChildProjects().iterator())
                .hasSize(1)
                .hasOnlyOneElementSatisfying(element -> assertThat(element).isEqualTo(child));
    }
}