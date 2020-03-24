package eu.xenit.custodian.adapters.buildsystem.gradle.stubs;

import java.io.File;
import java.io.Writer;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Nullable;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.InvalidPluginMetadataException;
import org.gradle.testkit.runner.InvalidRunnerConfigurationException;
import org.gradle.testkit.runner.UnexpectedBuildFailure;
import org.gradle.testkit.runner.UnexpectedBuildSuccess;
import org.gradle.testkit.runner.internal.DefaultBuildResult;
import org.gradle.testkit.runner.internal.DefaultBuildTask;

public class GradleRunnerStub extends GradleRunner {

    private final Function<GradleRunner, BuildResult> buildCallback;

    public GradleRunnerStub() {
        this(runner -> new DefaultBuildResult("", Collections.emptyList()));
    }

    public GradleRunnerStub(Function<GradleRunner, BuildResult> buildCallback) {
        this.buildCallback = buildCallback;
    }

    public GradleRunnerStub(BuildTask buildTask) {
        this((runner) -> new DefaultBuildResult("", Collections.singletonList(buildTask)));
    }

    @Override
    public GradleRunner withGradleVersion(String versionNumber) {
        return this;
    }

    @Override
    public GradleRunner withGradleInstallation(File file) {
        return this;
    }

    @Override
    public GradleRunner withGradleDistribution(URI uri) {
        return this;
    }

    @Override
    public GradleRunner withTestKitDir(File file) {
        return this;
    }

    @Override
    public File getProjectDir() {
        return null;
    }

    @Override
    public GradleRunner withProjectDir(File file) {
        return this;
    }

    @Override
    public List<String> getArguments() {
        return null;
    }

    @Override
    public GradleRunner withArguments(List<String> list) {
        return this;
    }

    @Override
    public GradleRunner withArguments(String... strings) {
        return this;
    }

    @Override
    public List<? extends File> getPluginClasspath() {
        return null;
    }

    @Override
    public GradleRunner withPluginClasspath() throws InvalidPluginMetadataException {
        return this;
    }

    @Override
    public GradleRunner withPluginClasspath(Iterable<? extends File> iterable) {
        return this;
    }

    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public GradleRunner withDebug(boolean b) {
        return this;
    }

    @Nullable
    @Override
    public Map<String, String> getEnvironment() {
        return null;
    }

    @Override
    public GradleRunner withEnvironment(Map<String, String> map) {
        return this;
    }

    @Override
    public GradleRunner forwardStdOutput(Writer writer) {
        return this;
    }

    @Override
    public GradleRunner forwardStdError(Writer writer) {
        return this;
    }

    @Override
    public GradleRunner forwardOutput() {
        return this;
    }

    @Override
    public BuildResult build() throws InvalidRunnerConfigurationException, UnexpectedBuildFailure {
        return this.buildCallback.apply(this);
    }

    @Override
    public BuildResult buildAndFail() throws InvalidRunnerConfigurationException, UnexpectedBuildSuccess {
        return this.buildCallback.apply(this);
    }

}
