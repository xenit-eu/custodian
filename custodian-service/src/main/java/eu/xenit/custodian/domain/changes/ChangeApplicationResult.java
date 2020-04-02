package eu.xenit.custodian.domain.changes;

import java.util.stream.Stream;

public interface ChangeApplicationResult {

    boolean isSuccess();

    String getShortDescription();
    Stream<Patch> getPatches();

}
