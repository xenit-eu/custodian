package eu.xenit.custodian.adapters.gradle.buildsystem.api.notation;

import java.util.function.Function;

@FunctionalInterface
public interface NotationParser<T, R> extends Function<T, R> {

}
