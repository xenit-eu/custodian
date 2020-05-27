package eu.xenit.custodian.adapters.gradle.buildsystem;

public interface Fixtures {

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
            return GradleModuleDependency.implementation(Modules.apacheHttpClient(), "4.5.10");
        }

        static GradleModuleDependency junit() {
            return GradleModuleDependency.implementation(Modules.junit(), "4.12");
        }
    }

}
