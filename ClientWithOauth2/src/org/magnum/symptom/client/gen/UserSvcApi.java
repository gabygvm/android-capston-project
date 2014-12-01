package org.magnum.symptom.client.gen;

import java.util.List;

import org.magnum.symptom.client.gen.entities.Answer;
import org.magnum.symptom.client.gen.entities.CheckIn;
import org.magnum.symptom.client.gen.entities.Doctor;
import org.magnum.symptom.client.gen.entities.Patient;
import org.magnum.symptom.client.gen.entities.Recipe;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
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
	public static final String PATIENT_DOCTORS_PATH = "/patient/{id}/search/Doctor/all";
	public static final String PATIENT_LAST_RECIPE_FROM_DOC_PATH = "/patient/recipes/doctorId/last";
	public static final String PATIENT_CHECKIN_TO_DOC = "/patient/patRecord/checkin";
	public static final String PATIENT_PHOTO = "/patient/photo/update";
	public static final String PATIENT_IMAGE_SVC_PATH = "/patient/{id}/photo/update";
	
	public static final String DOCTOR_SVC_PATH = "/doctor";
	public static final String DOCTOR_BY_USERNAME_SVC_PATH = "/doctor/search/findByUsername";
	public static final String DOCTOR_FIND_PATIENTS_SVC_PATH = "/doctor/search/patients";
	public static final String DOCTOR_FIND_PATIENTS_NAME_SVC_PATH = "/doctor/search/patients/name";
	public static final String DOCTOR_PATIENT_RECIPES_PATH = "/doctor/search/patient/lastRecord";
	public static final String DOCTOR_PATIENT_ADD_MED_PATH = "/doctor/search/patient/lastRecord/addMed";
	public static final String DOCTOR_PATIENT_DELETE_MED_PATH = "/doctor/search/patient/lastRecord/deleteMed";
	public static final String DOCTOR_PATIENT_CHECKINS_PATH = "/doctor/search/patient/checkIn";
	
	public static final String DOCTOR_PATIENT_CHECKIN_PATH = "/doctor/search/patient/checkIn/{id}";
	
	@GET(ROLE_SVC_PATH)
	public String getUserRole();
	
	@GET(USER_SVC_PATH)
	public String getUserUserName();

	
	@GET(PATIENT_SVC_PATH)
	public Patient getPatientInfo();
	
	@GET(PATIENT_SVC_PATH + "/{id}")
	public Patient getPatientById(@Path(ID_PARAMETER) long id);

	@GET(PATIENT_DOCTORS_PATH)
	public List<Doctor> getDoctorsFromPatientId(@Path(ID_PARAMETER) long id);

	@GET(PATIENT_LAST_RECIPE_FROM_DOC_PATH)
	public Recipe getCurrentPatientLastRecipeByDoctorId(@Query("docId")long docId);
	
	@POST(PATIENT_CHECKIN_TO_DOC)
	public CheckIn AddPatientCheckIn(@Query("patRecordId") long patRecordId, @Body List<Answer> answers);
	
	@POST(PATIENT_PHOTO)
	public Patient SavePatientPhoto(@Body String photo);
	
	@GET(PATIENT_IMAGE_SVC_PATH)
	public byte[] getPatientImageById(@Path(UserSvcApi.ID_PARAMETER) long id);
	
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
	
	@GET(DOCTOR_PATIENT_CHECKINS_PATH)
	public List<CheckIn> getPatCheckInsForCurrentDoc(@Query(ID_PARAMETER) long patId);
	
	@GET(DOCTOR_PATIENT_CHECKIN_PATH)
	public CheckIn getPatCheckInByCheckInId(@Path(ID_PARAMETER) long checkInId);
	
	@POST(DOCTOR_PATIENT_ADD_MED_PATH)
	public Recipe AddMedFromRecipe(@Body Recipe recipe);
	
	@POST(DOCTOR_PATIENT_DELETE_MED_PATH)
	public Recipe DeleteMedFromRecipe(@Query("recipeId") long recipeId, @Query("medicineId") long medId);

}
