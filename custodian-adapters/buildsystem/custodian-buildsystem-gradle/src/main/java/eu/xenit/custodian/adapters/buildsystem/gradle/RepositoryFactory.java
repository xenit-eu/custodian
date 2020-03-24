package eu.xenit.custodian.adapters.buildsystem.gradle;

import eu.xenit.custodian.adapters.buildsystem.gradle.sentinel.dto.RepositoryDto;
import eu.xenit.custodian.adapters.buildsystem.maven.MavenRepository;
import java.util.Objects;

public class RepositoryFactory {

    public MavenRepository from(RepositoryDto dto) {
        Objects.requireNonNull(dto.getType(), "dto.getType() is null");
        switch (dto.getType()) {
            case MAVEN:
                return new DefaultMavenRepository(dto.getName(), dto.getUrl());
            default:
                throw new UnsupportedOperationException("Repository type '"+ dto.getType() + "' not supported");
        }
    }
}
