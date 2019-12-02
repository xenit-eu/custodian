package eu.xenit.custodian.adapters.buildsystem.maven.notation;

import java.util.function.Function;

@FunctionalInterface
public interface NotationParser<T, R> extends Function<T, R> {

}
