package eu.xenit.custodian.gradle.sentinel.plugin.support;

public interface PathMatcher {

    boolean isPattern(String pattern);
    boolean matches(String pattern, String relativePath);

}
