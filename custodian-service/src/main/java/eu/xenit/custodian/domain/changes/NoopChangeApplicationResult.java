package eu.xenit.custodian.domain.changes;

import java.util.stream.Stream;

public class NoopChangeApplicationResult implements ChangeApplicationResult {

    public final static ChangeApplicationResult INSTANCE =  new NoopChangeApplicationResult();

    private NoopChangeApplicationResult() {

    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public String getShortDescription() {
        return "Noop";
    }

    @Override
    public Stream<Patch> getPatches() {
        return Stream.empty();
    }
}
