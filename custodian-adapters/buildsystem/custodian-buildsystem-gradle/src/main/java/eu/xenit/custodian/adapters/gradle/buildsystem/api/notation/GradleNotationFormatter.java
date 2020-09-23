package eu.xenit.custodian.adapters.gradle.buildsystem.api.notation;

import eu.xenit.custodian.adapters.gradle.buildsystem.api.GradleModuleDependency;

public class GradleNotationFormatter {

    public static String format(GradleModuleDependency moduleDependency) {
        return String.format("%s '%s:%s:%s'", moduleDependency.getTargetConfiguration(),
                moduleDependency.getGroup(), moduleDependency.getName(), moduleDependency.getVersionSpec().getValue());
    }

}
