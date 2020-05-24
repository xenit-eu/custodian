package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.domain.buildsystem.GroupArtifactModuleIdentifier;

public interface Fixtures {


    @Deprecated
    interface GAV {

        static GroupArtifactModuleIdentifier apacheHttpClient() {
            return GroupArtifactModuleIdentifier.from("org.apache.httpcomponents", "httpclient");
        }

        static GroupArtifactModuleIdentifier junit() {
            return GroupArtifactModuleIdentifier.from("junit", "junit");
        }
    }

    interface Modules {

        static GradleModuleIdentifier apacheHttpClient() {
            return GradleModuleIdentifier.from("org.apache.httpcomponents", "httpclient");
        }

        static GradleModuleIdentifier junit() {
            return GradleModuleIdentifier.from("junit", "junit");
        }
    }

    interface Dependency {

        static GradleModuleDependency apacheHttpClient() {
            return GradleModuleDependency.implementation(GAV.apacheHttpClient(), "4.5.10");
        }

        static GradleModuleDependency junit() {
            return GradleModuleDependency.implementation(GAV.junit(), "4.12");
        }
    }

}
