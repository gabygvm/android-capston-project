package org.magnum.symptoms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.magnum.symptoms.service.auth.OAuth2SecurityConfiguration;
import org.magnum.symptoms.service.repository.Doctor;
import org.magnum.symptoms.service.repository.DoctorRepository;
import org.magnum.symptoms.service.repository.Patient;
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
	public static void main(String[] args) throws Exception{

		Patient patient;
		
		ConfigurableApplicationContext context = SpringApplication.run(
				Application.class, args);

		String date_to_string = "11-01-1981";
		PatientRepository patientRepo = context.getBean(PatientRepository.class);
		DoctorRepository doctorRepo = context.getBean(DoctorRepository.class);
		
		patient = patientRepo.saveAndFlush(new Patient("Name01", "LastName01", date_to_string, true,"patient01", "pat01"));
		doctorRepo.save(new Doctor("DocName01", "DocLastName01", date_to_string, false, "doctor01", "doc01"));
		
		date_to_string = "12-02-1982";
		patient = patientRepo.saveAndFlush(new Patient("Name02", "LastName02", date_to_string, false,"patient02", "pat02"));
		doctorRepo.save(new Doctor("DocName02", "DocLastName02", date_to_string, true, "doctor02", "doc02"));
		
		date_to_string = "13-03-1983";
		patient = patientRepo.saveAndFlush(new Patient("Name03", "LastName03", date_to_string, false,"patient03", "pat03"));
		
		date_to_string = "14-04-1984";
		patient = patientRepo.saveAndFlush(new Patient("Name04", "LastName04", date_to_string, true,"patient04", "pat04"));
		
		date_to_string = "15-05-1985";
		patient = patientRepo.saveAndFlush(new Patient("Name05", "LastName05", date_to_string, true,"patient05", "pat05"));
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
