package eu.xenit.custodian.sentinel.analyzer.repositories;

import eu.xenit.custodian.sentinel.analyzer.AspectAnalysis;
import eu.xenit.custodian.sentinel.analyzer.dependencies.FlatDirsRepositoryDetails;
import eu.xenit.custodian.sentinel.analyzer.dependencies.IvyRepositoryDetails;
import eu.xenit.custodian.sentinel.analyzer.repositories.RepositoryResult.RepositoryResultBuilder;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.gradle.api.Project;
import org.gradle.api.artifacts.repositories.ArtifactRepository;
import org.gradle.api.artifacts.repositories.FlatDirectoryArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.DefaultIvyArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.DefaultMavenArtifactRepository;
import org.gradle.api.internal.artifacts.repositories.descriptor.IvyRepositoryDescriptor;
import org.gradle.api.internal.artifacts.repositories.descriptor.MavenRepositoryDescriptor;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;

public class RepositoriesAnalyzer implements AspectAnalysis {

    private static final Logger logger = Logging.getLogger(RepositoriesAnalyzer.class);

    public RepositoriesContainer analyze(Project project) {

        logger.info("Collecting repository information");

        return new RepositoriesContainer(
                project.getRepositories().stream()
                        .map(this::analyseRepository)
                        .collect(Collectors.toMap(RepositoryResult::getName, descriptor -> descriptor))
        );
    }

    private RepositoryResult analyseRepository(ArtifactRepository repo) {

        logger.info("-- collecting information on repository '{}'", repo.getName());

        Optional<RepositoryResultBuilder> builder;

        // is this a maven repo ?
        builder = Optional.of(repo)
                .filter(DefaultMavenArtifactRepository.class::isInstance)
                .map(DefaultMavenArtifactRepository.class::cast)
                .map(this::analyze);

        if (!builder.isPresent()) {
            // otherwise an ivy repo ?
            builder = Optional.of(repo)
                    .filter(DefaultIvyArtifactRepository.class::isInstance)
                    .map(DefaultIvyArtifactRepository.class::cast)
                    .map(this::analyze);
        }

        if (!builder.isPresent()) {
            // otherwise flat dirs ?
            builder = Optional.of(repo)
                    .filter(FlatDirectoryArtifactRepository.class::isInstance)
                    .map(FlatDirectoryArtifactRepository.class::cast)
                    .map(this::analyze);
        }

        if (!builder.isPresent()) {
            // errr - flat dirs ?
            builder = Optional.of(this.analyzeFallback(repo));
        }

        return builder.get().build();
    }


    private RepositoryResultBuilder analyze(DefaultMavenArtifactRepository maven) {
        MavenRepositoryDescriptor descriptor = (MavenRepositoryDescriptor) maven.getDescriptor();

        return RepositoryResult.builder()
                .type("maven")
                .name(maven.getName())
                .url(maven.getUrl())
                .authenticated(descriptor.authenticated)
                .metadataSources(getFieldOrDefault(descriptor, "metadataSources", Collections.emptyList()));
    }

    private RepositoryResultBuilder analyze(DefaultIvyArtifactRepository ivy) {
        IvyRepositoryDescriptor descriptor = (IvyRepositoryDescriptor) ivy.getDescriptor();

        return RepositoryResult.builder()
                .type("ivy")
                .name(ivy.getName())
                .url(ivy.getUrl())
                .authenticated(descriptor.authenticated)
                .metadataSources(getFieldOrDefault(descriptor, "metadataSources", Collections.emptyList()))
                .ivy(IvyRepositoryDetails.builder()
                        .m2Compatible(descriptor.m2Compatible)
                        .artifactPatterns(getFieldOrDefault(descriptor, "artifactPatterns", Collections.emptyList()))
                        .ivyPatterns(getFieldOrDefault(descriptor, "ivyPatterns", Collections.emptyList()))
                        .layoutType(descriptor.layoutType)
                        .build()
                );

    }

    private RepositoryResultBuilder analyze(FlatDirectoryArtifactRepository flatDir) {
        return RepositoryResult.builder()
                .type("flatDir")
                .flatDirs(
                        FlatDirsRepositoryDetails.builder()
                                .dirs(flatDir.getDirs().stream().map(File::toString).collect(Collectors.toList()))
                                .build()
                );
    }


    private RepositoryResultBuilder analyzeFallback(ArtifactRepository repo) {
        return RepositoryResult.builder()
                .type("unknown")
                .name(repo.getName());
    }

    private <T> T getFieldOrDefault(Object object, String name, T orDefault) {
        return this.<T>getField(object, name).orElse(orDefault);
    }

    private <T> Optional<T> getField(Object object, String name) {
        try {
            Field field = object.getClass().getField(name);
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            T value = (T) field.get(object);
            return Optional.of(value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


}
