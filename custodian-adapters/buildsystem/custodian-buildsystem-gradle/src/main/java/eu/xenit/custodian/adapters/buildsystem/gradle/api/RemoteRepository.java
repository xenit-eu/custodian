package eu.xenit.custodian.adapters.buildsystem.gradle.api;

import eu.xenit.custodian.adapters.buildsystem.gradle.spi.sentinel.dto.RepositoryDto;
import eu.xenit.custodian.ports.spi.buildsystem.Repository;
import java.util.Objects;

public interface RemoteRepository extends Repository {

    String getId();
    String getUrl();

    static RemoteRepository from(RepositoryDto dto) {
        Objects.requireNonNull(dto.getType(), "dto.getType() is null");
        switch (dto.getType()) {
            case MAVEN:
                return new DefaultMavenRepository(dto.getName(), dto.getUrl());
            default:
                throw new UnsupportedOperationException("Repository type '"+ dto.getType() + "' not supported");
        }
    }
}
