package eu.xenit.custodian.gradle.sentinel.model;

import java.util.Map;
import java.util.Set;

public interface DependenciesContainer {

    Map<String, Set<DependencyModel>> all();
}
