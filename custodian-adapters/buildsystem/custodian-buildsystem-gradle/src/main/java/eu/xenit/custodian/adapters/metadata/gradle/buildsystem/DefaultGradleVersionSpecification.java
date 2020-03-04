package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import lombok.Getter;

public class DefaultGradleVersionSpecification implements GradleVersionSpecification {

    @Getter
    private final String value;

    DefaultGradleVersionSpecification(String value) {
        // parsing this etc ?
        this.value = value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
