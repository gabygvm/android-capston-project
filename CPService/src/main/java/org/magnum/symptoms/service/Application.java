package org.magnum.symptoms.service;

import java.io.File;
import java.sql.Date;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.magnum.symptoms.security.CustomAuthorityService;
import org.magnum.symptoms.service.auth.OAuth2SecurityConfiguration;
import org.magnum.symptoms.service.repository.Doctor;
import org.magnum.symptoms.service.repository.DoctorRepository;
import org.magnum.symptoms.service.repository.Patient;
import org.magnum.symptoms.service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//Tell Spring to automatically inject any dependencies that are marked in
//our classes with @Autowired
@EnableAutoConfiguration
//Tell Spring to automatically create a JPA implementation of our
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
//We use the @Import annotation to include our OAuth2SecurityConfiguration
//as part of this configuration so that we can have security and oauth
//setup by Spring
@Import(OAuth2SecurityConfiguration.class)
public class Application /*extends RepositoryRestMvcConfiguration*/ {

	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	//    1. Run->Run Configurations
	//    2. Under Java Applications, select your run configuration for this app
	//    3. Open the Arguments tab
	//    4. In VM Arguments, provide the following information to use the
	//       default keystore provided with the sample code:
	//
	//       -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit
	//
	//    5. Note, this keystore is highly insecure! If you want more securtiy, you 
	//       should obtain a real SSL certificate:
	//
	//       http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//
	// Tell Spring to launch our app!
	public static void main(String[] args) {
		
		Date birthday1 = new Date(1981, 01, 11);
		Date birthday2 = new Date(1982, 02, 12);
		Date birthday3 = new Date(1983, 03, 13);
		Date birthday4 = new Date(1984, 04, 14);
		Date birthday5 = new Date(1985, 05, 15);
		
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		
		PatientRepository patientRepo = context.getBean(PatientRepository.class);
		DoctorRepository doctorRepo = context.getBean(DoctorRepository.class);
		
		patientRepo.save(new Patient("Name01", "LastName01", birthday1, false, "patient01", "pat01"));
		patientRepo.save(new Patient("Name02", "LastName02", birthday2, false, "patient02", "pat02"));
		patientRepo.save(new Patient("Name03", "LastName03", birthday3, false, "patient03", "pat03"));
		patientRepo.save(new Patient("Name04", "LastName04", birthday4, true, "patient04", "pat04"));
		patientRepo.save(new Patient("Name05", "LastName05", birthday5, true, "patient05", "pat05"));
		
		doctorRepo.save(new Doctor("DocName01", "DocLastName01", birthday1, false, "doctor01", "doc01"));
		doctorRepo.save(new Doctor("DocName02", "DocLastName02", birthday2, true, "doctor02", "doc02"));
	}

    // This version uses the Tomcat web container and configures it to
	// support HTTPS. The code below performs the configuration of Tomcat
	// for HTTPS. Each web container has a different API for configuring
	// HTTPS. 
	//
	// The app now requires that you pass the location of the keystore and
	// the password for your private key that you would like to setup HTTPS
	// with. In Eclipse, you can set these options by going to:
	//    1. Run->Run Configurations
	//    2. Under Java Applications, select your run configuration for this app
	//    3. Open the Arguments tab
	//    4. In VM Arguments, provide the following information to use the
	//       default keystore provided with the sample code:
	//
	//       -Dkeystore.file=src/main/resources/private/keystore -Dkeystore.pass=changeit
	//
	//    5. Note, this keystore is highly insecure! If you want more securtiy, you 
	//       should obtain a real SSL certificate:
	//
	//       http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html
	//
	
	@Bean
	public UserDetailsService getUserDetailSvc()
	{
		return new CustomAuthorityService();
	}
}
