package eu.xenit.custodian.domain.usecases.scm;

import eu.xenit.custodian.domain.usecases.UseCase;
import eu.xenit.custodian.domain.usecases.scm.CloneRepositoryUseCase.CloneRepositoryUseCaseCommand;
import eu.xenit.custodian.domain.usecases.scm.CloneRepositoryUseCase.CloneRepositoryUseCaseResult;
import eu.xenit.custodian.ports.api.ClonedRepositoryHandle;
import eu.xenit.custodian.ports.api.SourceRepositoryReference;
import eu.xenit.custodian.ports.spi.scm.SourceControlHandler;
import eu.xenit.custodian.ports.spi.scm.UnsupportedProjectReference;
import eu.xenit.custodian.util.Arguments;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

public interface CloneRepositoryUseCase extends UseCase<CloneRepositoryUseCaseCommand, CloneRepositoryUseCaseResult> {

    CloneRepositoryUseCaseResult handle(CloneRepositoryUseCaseCommand cloneRepositoryUseCaseCommand);

    @Value
    class CloneRepositoryUseCaseCommand {
        SourceRepositoryReference reference;
    }

    @Value
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class CloneRepositoryUseCaseResult {

        ClonedRepositoryHandle handle;
        IOException exception;

        static CloneRepositoryUseCaseResult success(ClonedRepositoryHandle handle) {
            Arguments.notNull(handle, "handle");
            return new CloneRepositoryUseCaseResult(handle, null);
        }

        static CloneRepositoryUseCaseResult failed(IOException exception) {
            Arguments.notNull(exception, "exception");
            return new CloneRepositoryUseCaseResult(null, exception);
        }

        public ClonedRepositoryHandle getHandle() {
            if (handle == null) {
                throw new RuntimeException(exception);
            }
            return handle;
        }

    }
}

class DefaultCloneRepositoryUseCase implements CloneRepositoryUseCase {

    private final List<SourceControlHandler> scmHandlers;

    DefaultCloneRepositoryUseCase(SourceControlHandler sourceControlHandler) {
        this(Collections.singletonList(sourceControlHandler));
    }

    DefaultCloneRepositoryUseCase(Collection<SourceControlHandler> sourceControlHandlers) {
        this.scmHandlers = List.copyOf(sourceControlHandlers);
    }

    @Override
    public CloneRepositoryUseCaseResult handle(CloneRepositoryUseCaseCommand command) {
        try {
            SourceRepositoryReference reference = command.getReference();
            SourceControlHandler handler = this.scmHandlers.stream()
                    .filter(scm -> scm.canHandle(reference))
                    .findFirst()
                    .orElseThrow(() -> {
                        String msg = String.format("No handler for %s available", reference.getUri());
                        return new UnsupportedProjectReference(msg, reference);
                    });

            ClonedRepositoryHandle handle = handler.checkout(reference);
            return CloneRepositoryUseCaseResult.success(handle);
        } catch (IOException e) {
            return CloneRepositoryUseCaseResult.failed(e);
        }
    }

}
