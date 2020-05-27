package eu.xenit.custodian.ports.spi.buildsystem;

import eu.xenit.custodian.domain.usecases.updates.Patch;
import java.util.stream.Stream;

public interface BuildModification {

    Stream<Patch> getPatches();
    boolean isEmpty();


    /**
     * Returns true if there are any non-empty patches
     * @return
     */
    default boolean isModified() {
        return !this.isEmpty();
    }



}
