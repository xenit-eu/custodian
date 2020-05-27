package eu.xenit.custodian.adapters.gradle.buildsystem.notation;

import eu.xenit.custodian.adapters.gradle.buildsystem.GradleModuleDependency;

public class GradleNotationFormatter {

    public static String format(GradleModuleDependency moduleDependency) {
        return String.format("%s '%s:%s:%s'", moduleDependency.getTargetConfiguration(),
                moduleDependency.getGroup(), moduleDependency.getName(), moduleDependency.getVersionSpec().getValue());
    }

}
