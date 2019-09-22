package eu.xenit.custodian.domain.repository.scm;

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
