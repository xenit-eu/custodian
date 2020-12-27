package eu.xenit.custodian.gradle.sentinel.plugin.support;

import io.github.azagniotov.matcher.AntPathMatcher;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class AntPathMatcherAdapter implements PathMatcher {

    private AntPathMatcher delegate = new AntPathMatcher.Builder().build();

    @Override
    public boolean isPattern(String pattern) {
        Objects.requireNonNull(pattern, "pattern must not be null");
        return (pattern.contains("*") || pattern.contains("?"));
    }

    @Override
    public boolean matches(String pattern, String path) {

        Objects.requireNonNull(path, "path must not be null");

        return delegate.isMatch(pattern, path);
    }
}
