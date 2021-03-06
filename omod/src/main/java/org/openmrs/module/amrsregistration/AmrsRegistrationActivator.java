package org.openmrs.module.amrsregistration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.Activator;

public class AmrsRegistrationActivator implements Activator {
    private Log log;

    public AmrsRegistrationActivator() {
        this.log = LogFactory.getLog(super.getClass());
    }

    public void startup() {
        this.log.info("Starting AMRS Registration Module");
    }

    public void shutdown() {
        this.log.info("Stopping AMRS Registration Module");
    }
}
