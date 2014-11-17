package org.magnum.symptoms.service;

import java.util.ArrayList;
import java.util.List;

import org.magnum.symptoms.service.auth.OAuth2SecurityConfiguration;
import org.magnum.symptoms.service.repository.Doctor;
import org.magnum.symptoms.service.repository.DoctorRepository;
import org.magnum.symptoms.service.repository.Patient;
import org.magnum.symptoms.service.repository.PatientRecord;
import org.magnum.symptoms.service.repository.PatientRecordRepository;
import org.magnum.symptoms.service.repository.PatientRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//Tell Spring to automatically inject any dependencies that are marked in
//our classes with @Autowired
@EnableAutoConfiguration
// Tell Spring to automatically create a JPA implementation of our
@EnableJpaRepositories
// Tell Spring to turn on WebMVC (e.g., it should enable the DispatcherServlet
// so that requests can be routed to our Controllers)
@EnableWebMvc
// Tell Spring that this object represents a Configuration for the
// application
@Configuration
// Tell Spring to go and scan our controller package (and all sub packages) to
// find any Controllers or other components that are part of our applciation.
// Any class in this package that is annotated with @Controller is going to be
// automatically discovered and connected to the DispatcherServlet.
@ComponentScan
// We use the @Import annotation to include our OAuth2SecurityConfiguration
// as part of this configuration so that we can have security and oauth
// setup by Spring
@Import(OAuth2SecurityConfiguration.class)
public class Application /* extends RepositoryRestMvcConfiguration */{

	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	// 1. Run->Run Configurations
	// 2. Under Java Applications, select your run configuration for this app
	// 3. Open the Arguments tab
	// 4. In VM Arguments, provide the following information to use the
	// default keystore provided with the sample code:
	//
	// -Dkeystore.file=src/main/resources/private/keystore
	// -Dkeystore.pass=changeit
	//
	// 5. Note, this keystore is highly insecure! If you want more securtiy, you
	// should obtain a real SSL certificate:
	//
	// http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//
	// Tell Spring to launch our app!
	
	private static List<PatientRecord> _patRecordList = new ArrayList<PatientRecord>();
	
	public static void main(String[] args) throws Exception{

		
		ConfigurableApplicationContext context = SpringApplication.run(
				Application.class, args);

		PatientRepository patientRepo = context.getBean(PatientRepository.class);
		DoctorRepository doctorRepo = context.getBean(DoctorRepository.class);
		PatientRecordRepository patientRecordRepo = context.getBean(PatientRecordRepository.class);
		
		Doctor doctor1, doctor2;
		Patient patient1, patient2, patient3, patient4, patient5, patient6;
		PatientRecord patientRecord1, patientRecord2, patientRecord3, 
						patientRecord4, patientRecord5, patientRecord6, patientRecord7;
		
		//Patients
		patient1 = new Patient("Name01", "LastName01", "11-01-1981", true,"patient01", "pat01");
		patient2 = new Patient("Name02", "LastName02", "12-02-1982", false,"patient02", "pat02");
		patient3 = new Patient("Name03", "LastName03", "13-03-1983", false,"patient03", "pat03");
		patient4 = new Patient("Name04", "LastName04", "14-04-1984", true,"patient04", "pat04");
		patient5 = new Patient("Name05", "LastName05", "15-05-1985", true,"patient05", "pat05");
		patient6 = new Patient("Name03", "LastName06", "13-03-1983", false,"patient06", "pat06");
		
		//Doctors
		doctor1 = new Doctor("DocName01", "DocLastName01", "11-01-1981", false, "doctor01", "doc01");
		doctor2 = new Doctor("DocName02", "DocLastName02", "12-02-1982", true, "doctor02", "doc02");
		
		//Save to the doctor Repo the doctors
		doctor1 = doctorRepo.saveAndFlush(doctor1);
		doctor2 = doctorRepo.saveAndFlush(doctor2);
		
		//Save to the patient Repo the patients
		patient1 = patientRepo.saveAndFlush(patient1);
		patient2 = patientRepo.saveAndFlush(patient2);
		patient3 = patientRepo.saveAndFlush(patient3);
		patient4 = patientRepo.saveAndFlush(patient4);
		patient5 = patientRepo.saveAndFlush(patient5);
		patient6 = patientRepo.saveAndFlush(patient6);
		
		//Patient Record information init
		patientRecord1 = new PatientRecord(patient1, doctor1);
		patientRecord2 = new PatientRecord(patient2, doctor1);
		patientRecord3 = new PatientRecord(patient3, doctor1);
		patientRecord6 = new PatientRecord(patient3, doctor2);
		patientRecord4 = new PatientRecord(patient4, doctor2);
		patientRecord5 = new PatientRecord(patient5, doctor2);
		patientRecord7 = new PatientRecord(patient6, doctor2);
				
		//Patient Record 
		patientRecordRepo.saveAndFlush(patientRecord1);
		patientRecordRepo.saveAndFlush(patientRecord2);
		patientRecordRepo.saveAndFlush(patientRecord3);
		patientRecordRepo.saveAndFlush(patientRecord4);
		patientRecordRepo.saveAndFlush(patientRecord5);
		patientRecordRepo.saveAndFlush(patientRecord6);
		patientRecordRepo.saveAndFlush(patientRecord7);
	}

	// This version uses the Tomcat web container and configures it to
	// support HTTPS. The code below performs the configuration of Tomcat
	// for HTTPS. Each web container has a different API for configuring
	// HTTPS.
	//
	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	// 1. Run->Run Configurations
	// 2. Under Java Applications, select your run configuration for this app
	// 3. Open the Arguments tab
	// 4. In VM Arguments, provide the following information to use the
	// default keystore provided with the sample code:
	//
	// -Dkeystore.file=src/main/resources/private/keystore
	// -Dkeystore.pass=changeit
	//
	// 5. Note, this keystore is highly insecure! If you want more securtiy, you
	// should obtain a real SSL certificate:
	//
	// http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//

	/*
	 * @Bean public UserDetailsService getUserDetailSvc() { return new
	 * CustomAuthorityService(); }
	 */
}
