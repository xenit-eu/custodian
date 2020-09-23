package eu.xenit.custodian.adapters.gradle.buildsystem.impl;

import java.nio.file.Path;

public interface PathResolver {

    Path resolve(String path);
    Path resolve(Path path);

    PathResolver newRelativeResolver(String path);
}