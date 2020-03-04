package eu.xenit.custodian.domain.changes;

public interface LogicalChange {

    ChangeApplicationResult apply();

    interface ChangeApplicationResult {

        boolean success();

    }
}
