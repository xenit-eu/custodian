package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;

public interface RepositoryModel extends Serializable {

    String getUrl();
    String getName();
}
