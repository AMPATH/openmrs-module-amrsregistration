package org.openmrs.module.amrsregistration.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonName;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.amrsregistration.AmrsRegistrationService;
import org.openmrs.module.amrsregistration.db.AmrsRegistrationDAO;
import org.openmrs.reporting.export.DataExportFunctions;
import org.openmrs.util.OpenmrsUtil;

public class AmrsRegistrationServiceImpl implements AmrsRegistrationService {
    private static Log log = LogFactory
            .getLog(AmrsRegistrationServiceImpl.class);
    private AmrsRegistrationDAO daoAmrs;

    public void setRemoteRegistrationDAO(
            AmrsRegistrationDAO paramAmrsRegistrationDAO) {
        this.daoAmrs = paramAmrsRegistrationDAO;
    }

    @SuppressWarnings("unused")
    private AmrsRegistrationDAO getRemoteRegistrationDAO() {
        return this.daoAmrs;
    }

    /**
     * This method will be used for remote registration.  Registered patient
     * information will be placed into an xml file for the FormEntry processor.
     * @param paramPatient
     * @param paramString1
     * @param paramString2
     * @throws APIException
     */
    public void registerPatient(Patient paramPatient, String paramString1,
            String paramString2) throws APIException {
        File localFile1 = OpenmrsUtil
                .getDirectoryInApplicationDataDirectory("amrsregistration");
        File localFile2 = new File(localFile1, "registrationTemplate.xml");
        try {
            String str1 = OpenmrsUtil.getFileAsString(localFile2);

            Properties localProperties = new Properties();
            localProperties.setProperty("resource.loader", "class");
            localProperties.setProperty("class.resource.loader.description",
                    "VelocityClasspathResourceLoader");
            localProperties
                    .setProperty("class.resource.loader.class",
                            "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
            localProperties.setProperty("runtime.log.logsystem.class",
                    "org.apache.velocity.runtime.log.NullLogSystem");

            Velocity.init(localProperties);
            VelocityContext localVelocityContext = new VelocityContext();
            localVelocityContext.put("locale", Context.getLocale());
            localVelocityContext.put("patient", Context.getPatientService()
                    .getPatient(paramPatient.getPatientId()));
            localVelocityContext.put("user", Context.getAuthenticatedUser());
            localVelocityContext.put("fn", new DataExportFunctions());
            localVelocityContext.put("dateEntered", new Date());
            localVelocityContext.put("sid", paramString1);
            localVelocityContext.put("uid", paramString2);
            StringWriter localStringWriter = new StringWriter();
            Velocity.evaluate(localVelocityContext, localStringWriter, super
                    .getClass().getName(), str1);

            String str2 = Context.getAdministrationService().getGlobalProperty(
                    "remoteformentry.pending_queue_dir");
            File localFile3 = OpenmrsUtil
                    .getDirectoryInApplicationDataDirectory(str2);
            Date localDate = new Date();
            String str3 = "amrsregistration_" + localDate.toString();
            File localFile4 = new File(localFile3, str3);
            try {
                FileWriter localFileWriter = new FileWriter(localFile4);
                localFileWriter.write(localStringWriter.toString());
                localFileWriter.flush();
                localFileWriter.close();
            } catch (IOException localIOException) {
                log.error("Unable to write amrsregistration output file.",
                        localIOException);
            }
        } catch (Exception localException) {
            log.error("Unable to create amrsregistration output file",
                    localException);
            throw new APIException(localException);
        }
    }

    public List<Person> getPersons(PersonName paramPersonName,
            PersonAddress paramPersonAddress, Set<PersonAttribute> paramSet,
            String paramString, Date paramDate, Integer paramInteger, Integer limit)
            throws APIException {
        return this.daoAmrs.getPersons(paramPersonName, paramPersonAddress,
                paramSet, paramString, paramDate, paramInteger, limit);
    }

    public String sayHello() {
        return "Hello";
    }

    public List<Person> getPersons() throws APIException {
        return daoAmrs.getPersons();
    }
}
