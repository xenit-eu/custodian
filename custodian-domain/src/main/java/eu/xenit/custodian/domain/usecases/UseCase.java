package eu.xenit.custodian.domain.usecases;

public interface UseCase<TCommand, TResponse> {

    TResponse handle(TCommand command);

}
