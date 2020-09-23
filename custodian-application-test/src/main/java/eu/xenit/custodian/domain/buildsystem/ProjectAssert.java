//package eu.xenit.custodian.domain.buildsystem;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import eu.xenit.custodian.ports.spi.buildsystem.Project;
//import eu.xenit.custodian.ports.spi.buildsystem.ProjectContainer;
//import java.util.Optional;
//import java.util.function.Consumer;
//import org.assertj.core.api.AbstractAssert;
//
//public class ProjectAssert<SELF extends ProjectAssert<SELF,ACTUAL>, ACTUAL extends Project> extends AbstractAssert<SELF, ACTUAL> {
//
//    public ProjectAssert(ACTUAL project) {
//        super(project, ProjectAssert.class);
//    }
//
//    public ProjectAssert<SELF,ACTUAL> hasDependency(String notation) {
//        this.isNotNull();
//
//        return this.withDependencies(dependencies -> {
//            dependencies.hasDependency(notation);
//        });
//    }
//
//    public ProjectAssert<SELF,ACTUAL> withDependencies(Consumer<DependencyContainerAssert> callback) {
//        this.isNotNull();
//        callback.accept(new DependencyContainerAssert(this.actual.getDependencies()));
//        return this.myself;
//    }
//
//    public ProjectAssert<SELF,ACTUAL> withChildProjects(Consumer<ProjectContainer> consumer) {
//        consumer.accept(this.actual.getChildProjects());
//        return myself;
//    }
//
//    public ProjectAssert<SELF,ACTUAL> hasParent(Consumer<Optional<? extends Project>> callback) {
//        callback.accept(this.actual.getParent());
//        return myself;
//    }
//
//    public ProjectAssert<SELF,ACTUAL> hasName(String custodian) {
//        assertThat(this.actual.getName()).isEqualTo(custodian);
//        return myself;
//    }
//
//}
