package eu.xenit.custodian.adapters.buildsystem.gradle.notation;

import java.util.function.Function;

@FunctionalInterface
public interface NotationParser<T, R> extends Function<T, R> {

}
