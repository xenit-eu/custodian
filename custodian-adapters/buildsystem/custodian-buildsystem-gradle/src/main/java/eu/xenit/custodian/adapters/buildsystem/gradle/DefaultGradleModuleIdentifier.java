package eu.xenit.custodian.adapters.buildsystem.gradle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class DefaultGradleModuleIdentifier implements GradleModuleIdentifier {
    private final String group;
    private final String name;
}
