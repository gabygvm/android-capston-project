package org.magnum.symptom.client.gen;

import java.util.List;

import org.magnum.symptom.client.gen.entities.Doctor;
import org.magnum.symptom.client.gen.entities.Patient;
import org.magnum.symptom.client.gen.entities.Recipe;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * @author Gabriela Vera
 *
 */
public interface UserSvcApi {

	public static final String USERNAME_PARAMETER = "username";
	public static final String ID_PARAMETER = "id";

	public static final String TOKEN_PATH = "/oauth/token";
	public static final String USER_SVC_PATH = "/user";
	public static final String ROLE_SVC_PATH = "/role";
	
	
	public static final String PATIENT_SVC_PATH = "/patient";
	public static final String PATIENT_BY_USERNAME_SVC_PATH = "/patient/search/findByUsername";

	public static final String DOCTOR_SVC_PATH = "/doctor";
	public static final String DOCTOR_BY_USERNAME_SVC_PATH = "/doctor/search/findByUsername";
	public static final String DOCTOR_FIND_PATIENTS_SVC_PATH = "/doctor/search/patients";
	public static final String DOCTOR_FIND_PATIENTS_NAME_SVC_PATH = "/doctor/search/patients/name";
	public static final String DOCTOR_PATIENT_RECIPES_PATH = "/doctor/search/patient/lastRecord";
	
	@GET(ROLE_SVC_PATH)
	public String getUserRole();
	
	@GET(USER_SVC_PATH)
	public String getUserUserName();

	
	@GET(PATIENT_SVC_PATH)
	public Patient getPatientInfo();
	
	@GET(PATIENT_SVC_PATH + "/{id}")
	public Patient getPatientById(@Path(ID_PARAMETER) long id);

	@GET(DOCTOR_SVC_PATH)
	public Doctor getDoctorInfo();
	
	@GET(DOCTOR_SVC_PATH + "/{id}")
	public Doctor getDoctorById(@Path(ID_PARAMETER) long id);

	@GET(DOCTOR_FIND_PATIENTS_SVC_PATH)
	public List<Patient> getPatientsByDoctor();
	
	@GET(DOCTOR_FIND_PATIENTS_NAME_SVC_PATH)
	public List<Patient> getPatientsByDoctorWithNameAndLastName(@Query("name")String name, @Query("lastname")String lastname);
	
	@GET(DOCTOR_PATIENT_RECIPES_PATH)
	public Recipe getPatRecipesByPatIdAndCurrentDoc(@Query(ID_PARAMETER) long patId);
	
}
