package eu.xenit.custodian.adapters.gradle.buildsystem.api;

import eu.xenit.custodian.ports.spi.buildsystem.VersionSpecification;
import lombok.Getter;

public interface GradleVersionSpecification extends VersionSpecification {

    static GradleVersionSpecification from(String version) {
        return new DefaultGradleVersionSpecification(version);
    }

    class DefaultGradleVersionSpecification implements GradleVersionSpecification {

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
}
