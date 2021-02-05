package eu.xenit.custodian.gradle.sentinel.model;

import java.io.Serializable;
import java.util.List;

public interface RepositoriesContainer extends Serializable {

    List<RepositoryModel> all();
}
