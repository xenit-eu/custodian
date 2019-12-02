package eu.xenit.custodian.adapters.buildsystem.maven.notation;

import java.util.function.Function;

@FunctionalInterface
public interface NotationParserDelegate extends Function<String, ParsedDependencyNotation> {

}
