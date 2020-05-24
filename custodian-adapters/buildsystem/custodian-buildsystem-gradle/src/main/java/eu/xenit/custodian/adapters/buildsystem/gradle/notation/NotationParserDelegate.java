package eu.xenit.custodian.adapters.buildsystem.gradle.notation;

import java.util.function.Function;

@FunctionalInterface
public interface NotationParserDelegate extends Function<String, ParsedDependencyNotation> {

}
