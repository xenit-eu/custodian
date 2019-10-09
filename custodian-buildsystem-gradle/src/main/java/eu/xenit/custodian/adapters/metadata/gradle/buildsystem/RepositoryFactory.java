package eu.xenit.custodian.adapters.metadata.gradle.buildsystem;

import eu.xenit.custodian.adapters.metadata.gradle.sentinel.dto.RepositoryDto;
import java.util.Objects;

public class RepositoryFactory {

    public GradleRepository from(RepositoryDto dto) {
        Objects.requireNonNull(dto.getType(), "dto.getType() is null");
        switch (dto.getType()) {
            case MAVEN:
                return new MavenRepository(dto.getName(), dto.getUrl());
            default:
                throw new UnsupportedOperationException("Repository type '"+ dto.getType() + "' not supported");
        }
    }
}
