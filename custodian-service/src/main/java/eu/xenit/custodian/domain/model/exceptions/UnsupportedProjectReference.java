package eu.xenit.custodian.domain.model.exceptions;

import eu.xenit.custodian.domain.model.ProjectReference;

public class UnsupportedProjectReference extends RuntimeException {

    private final ProjectReference projectReference;

    public UnsupportedProjectReference(String message, ProjectReference projectReference)
    {
        super(message);
        this.projectReference = projectReference;
    }

    public ProjectReference getProjectReference() {
        return this.projectReference;
    }

}
