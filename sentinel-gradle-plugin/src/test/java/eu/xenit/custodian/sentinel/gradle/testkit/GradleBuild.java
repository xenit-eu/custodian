package eu.xenit.custodian.sentinel.gradle.testkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class GradleBuild implements TestRule {

    private final TemporaryFolder temp = new TemporaryFolder();
    private File source;
    private File projectDir;

    @Override
    public Statement apply(Statement base, Description description) {
        return this.temp.apply(new Statement() {

            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    base.evaluate();
                }
                finally {
                    after();
                }
            }

        }, description);
    }

    private void before() throws IOException {
        this.projectDir = this.temp.newFolder();
    }

    private void after() {
        GradleBuild.this.source = null;
    }

    public GradleBuild source(String source) {
        return source(new File(source));
    }

    public GradleBuild source(File source) {
        if (source == null || !source.exists() || !source.isDirectory()) {
            throw new IllegalArgumentException("Invalid source " + source);
        }
        this.source = source;
        return this;
    }

    public File getProjectDir() {
        return this.projectDir;
    }

    public BuildResult build(String... arguments) {
        try {
            return createRunner(arguments).build();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public BuildResult buildAndFail(String... arguments) {
        try {
            return createRunner(arguments).buildAndFail();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }




    private GradleRunner createRunner(String... arguments) throws IOException {
        copyFolder(this.source.getAbsoluteFile().toPath(), this.projectDir.toPath());
        List<String> allArguments = new ArrayList<>();
//        allArguments.add("-PpluginClasspath=" + getPluginClasspath());
//        allArguments.add("-PspringFormatVersion=" + getSpringFormatVersion());
        allArguments.add("--stacktrace");
        allArguments.addAll(Arrays.asList(arguments));


        return GradleRunner.create()
                .withProjectDir(this.projectDir)
                // debug = true triggers a bug in gradle
                // see https://github.com/gradle/gradle/issues/3995
                .withDebug(false)
                .withArguments(allArguments)
                .withPluginClasspath()
                .forwardOutput();



    }

    private void copyFolder(Path source, Path target) throws IOException {
        try (Stream<Path> stream = Files.walk(source)) {
            stream.forEach((child) -> {
                try {
                    Path relative = source.relativize(child);
                    Path destination = target.resolve(relative);
                    if (!destination.toFile().isDirectory()) {
                        System.out.println("copying "+child+" to "+destination);
                        Files.copy(child, destination, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            });
        }
    }
}
