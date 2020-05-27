package eu.xenit.custodian.ports.spi.updates;

import eu.xenit.custodian.domain.usecases.updates.ChangeApplicationResult;

public interface LogicalChange {

   String getDescription();
   ChangeApplicationResult apply();

}
