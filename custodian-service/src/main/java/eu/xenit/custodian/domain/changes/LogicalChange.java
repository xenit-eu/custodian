package eu.xenit.custodian.domain.changes;

public interface LogicalChange {

    ChangeApplicationResult apply();

    interface ChangeApplicationResult {

        ChangeApplicationResult NOOP = new ChangeApplicationResult() {
            @Override
            public boolean success() {
                return false;
            }
        };

        boolean success();

    }
}
