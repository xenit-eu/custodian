package eu.xenit.custodian.adapters.gradle.buildsystem.api.notation;

import java.util.function.Function;

@FunctionalInterface
public interface NotationParserDelegate extends Function<String, ParsedDependencyNotation> {

}
