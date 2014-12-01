package org.magnum.symptoms.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.magnum.autograder.junit.Rubric;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
//import org.magnum.symptoms.service.TestData;
import org.magnum.symptoms.service.client.SecuredRestBuilder;
import org.magnum.symptoms.service.client.UserSvcApi;
import org.magnum.symptoms.service.repository.Answer;
import org.magnum.symptoms.service.repository.AnswerType;
import org.magnum.symptoms.service.repository.AnswerValue;
import org.magnum.symptoms.service.repository.CheckIn;
import org.magnum.symptoms.service.repository.Doctor;
import org.magnum.symptoms.service.repository.Medicine;
import org.magnum.symptoms.service.repository.Patient;
import org.magnum.symptoms.service.repository.PatientRecord;
import org.magnum.symptoms.service.repository.Recipe;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;

/**
 * A test for the Asgn2 video service
 * 
 * @author mitchell
 */
public class AutoGradingTest {

	private class ErrorRecorder implements ErrorHandler {

		private RetrofitError error;

		@Override
		public Throwable handleError(RetrofitError cause) {
			error = cause;
			return error.getCause();
		}

		public RetrofitError getError() {
			return error;
		}
	}

	private final String TEST_URL = "https://localhost:8443";

	private final String USERNAME1 = "patient04";
	private final String USERNAME2 = "doctor02";
	private final String PASSWORD1 = "pat04";
	private final String PASSWORD2 = "doc02";

	private final String CLIENT_ID = "userAndroid";

	private UserSvcApi UserPatient04 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + UserSvcApi.TOKEN_PATH)
			.setUsername(USERNAME1).setPassword(PASSWORD1)
			.setClientId(CLIENT_ID).build().create(UserSvcApi.class);

	private UserSvcApi UserDoctor02 = new SecuredRestBuilder()
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setEndpoint(TEST_URL)
			.setLoginEndpoint(TEST_URL + UserSvcApi.TOKEN_PATH)
			.setUsername(USERNAME2).setPassword(PASSWORD2)
			.setClientId(CLIENT_ID).build().create(UserSvcApi.class);

	private UserSvcApi UserDoctor01 = new SecuredRestBuilder()
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setEndpoint(TEST_URL)
	.setLoginEndpoint(TEST_URL + UserSvcApi.TOKEN_PATH)
	.setUsername("doctor01").setPassword("doc01")
	.setClientId(CLIENT_ID).build().create(UserSvcApi.class);
	
	private UserSvcApi UserPatient01 = new SecuredRestBuilder()
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setEndpoint(TEST_URL)
	.setLoginEndpoint(TEST_URL + UserSvcApi.TOKEN_PATH)
	.setUsername("patient01").setPassword("pat01")
	.setClientId(CLIENT_ID).build().create(UserSvcApi.class);

	
	
	@Rubric(value = "Video data is preserved", goal = "The goal of this evaluation is to ensure that your Spring controller(s) "
			+ "properly unmarshall Video objects from the data that is sent to them "
			+ "and that the HTTP API for adding videos is implemented properly. The"
			+ " test checks that your code properly accepts a request body with"
			+ " application/json data and preserves all the properties that are set"
			+ " by the client. The test also checks that you generate an ID and data"
			+ " URL for the uploaded video.", points = 20.0, reference = "This test is derived from the material in these videos: "
			+ "https://class.coursera.org/mobilecloud-001/lecture/61 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/97 "
			+ "https://class.coursera.org/mobilecloud-001/lecture/99 ")
	@Test
	public void testGetUserRole_Patient() throws Exception {
		String patReceived = UserPatient04.getUserRole();
		assertEquals("ROLE_PATIENT", patReceived);
	}
	@Test
	public void testGetUserRole_Doctor() throws Exception {
		String docReceived = UserDoctor02.getUserRole();
		assertEquals("ROLE_DOCTOR", docReceived);
	}
	@Test
	public void testGetUserUserName() throws Exception {

		String username1 = UserPatient04.getUserUserName();	
		String username2 = UserDoctor02.getUserUserName();
		
		assertEquals("patient04", username1);
		assertEquals("doctor02", username2);
	}
	
	@Test
	public void testGetPatientInfo() throws Exception {
		
		Patient patReceived = UserPatient04.getPatientInfo();
		assertEquals(6, patReceived.getId());
		assertEquals("NAME04", patReceived.getName());
		assertEquals("LASTNAME04", patReceived.getLastName());
		assertEquals("14-04-1984", patReceived.getBirthDate());
		assertTrue(patReceived.getIsFemale() == true);
	}
	@Test
	public void testGetDoctoInfo() throws Exception {

		Doctor docReceived = UserDoctor02.getDoctorInfo();
		assertEquals(2, docReceived.getId());
		assertEquals("DOCNAME02", docReceived.getName());
		assertEquals("DOCLASTNAME02", docReceived.getLastName());
		assertEquals("12-02-1982", docReceived.getBirthDate());
		assertTrue(docReceived.getIsFemale() == true);
	}
	
	@Test
	public void testGetPatientDataFromId() throws Exception {
		
		Patient patReceived = UserPatient04.getPatientById(5);
		assertEquals(5, patReceived.getId());
		assertEquals("NAME03", patReceived.getName());
		assertEquals("LASTNAME03", patReceived.getLastName());
		assertEquals("13-03-1983", patReceived.getBirthDate());
		assertTrue(patReceived.getIsFemale() == false);
	}
	@Test
	public void testGetDoctorDataFromId() throws Exception {

		Doctor docReceived = UserDoctor02.getDoctorById(2);
		assertEquals(2, docReceived.getId());
		assertEquals("DOCNAME02", docReceived.getName());
		assertEquals("DOCLASTNAME02", docReceived.getLastName());
		assertEquals("12-02-1982", docReceived.getBirthDate());
		assertTrue(docReceived.getIsFemale() == true);
	}
	/*@Ignore
	@Test
	public void testGetPatientDataFromUserName() throws Exception {
		
		List<Patient> patListReceived = readWriteVideoSvcUser1.getPatientByUsername("patient05");
		Patient patReceived = patListReceived.get(0);
		
		assertEquals(5, patReceived.getId());
		assertEquals("Name05", patReceived.getName());
		assertEquals("LastName05", patReceived.getLastName());
		assertEquals("15-05-1985", patReceived.getBirthDate());
		assertTrue(patReceived.getIsFemale() == true);
	}
	@Ignore
	@Test
	public void testGetDoctorDataFromUserName() throws Exception {

		List<Doctor> docListReceived =readWriteVideoSvcUser2.getDoctorByUsername("doctor01");
		Doctor docReceived = docListReceived.get(0);
		
		assertEquals(1, docReceived.getId());
		assertEquals("DocName01", docReceived.getName());
		assertEquals("DocLastName01", docReceived.getLastName());
		assertEquals("11-01-1981", docReceived.getBirthDate());
		assertTrue(docReceived.getIsFemale() == false);
	}*/
	@Test
	public void GetPatientListFromPatientRecordOfDoc() throws Exception {

		List<Patient> patList = UserDoctor02.getPatientsByDoctor();
		
		assertEquals("LASTNAME03", patList.get(0).getLastName());
		assertEquals("LASTNAME04", patList.get(1).getLastName());
		assertEquals("LASTNAME05", patList.get(2).getLastName());
		assertEquals("LASTNAME06", patList.get(3).getLastName());
		assertEquals(4, patList.size());
	}
	@Test
	public void GetPatientListFromPatientRecordOfDocAndPatientNameAndLastName() throws Exception {

		List<Patient> patList = UserDoctor02.getPatientsByDoctorWithNameAndLastName("Name03","LastName03");
		assertEquals("NAME03", patList.get(0).getName());
		assertEquals("LASTNAME03", patList.get(0).getLastName());
		assertEquals(1, patList.size());
	}
	@Test
	public void GetPatientListFromPatientRecordOfDocAndPatientName() throws Exception {

		List<Patient> patList = UserDoctor02.getPatientsByDoctorWithNameAndLastName("Name03","");
		assertEquals("NAME03", patList.get(0).getName());
		assertEquals("LASTNAME03", patList.get(0).getLastName());
		assertEquals("NAME03", patList.get(1).getName());
		assertEquals("LASTNAME06", patList.get(1).getLastName());
		assertEquals(2, patList.size());
	}
	@Test
	public void GetPatientListFromPatientRecordOfDocAndPatientLastName() throws Exception {

		List<Patient> patList = UserDoctor02.getPatientsByDoctorWithNameAndLastName("","LastName06");
		assertEquals("NAME03", patList.get(0).getName());
		assertEquals("LASTNAME06", patList.get(0).getLastName());
		assertEquals(1, patList.size());
	}
	@Test
	public void GetRecipeFromPatIdAndDoc() throws Exception {

		Recipe recipe = UserDoctor01.getPatRecipesByPatIdAndCurrentDoc((long)3);
		
		assertEquals(1, recipe.getMedicines().size());
		assertEquals("MEDICINE01", recipe.getMedicines().get(0).getMedicine());
	}
	@Test
	public void AddPatientMedToRecipe_MedInDatabase() throws Exception {
		
		Recipe recipe = UserDoctor01.getPatRecipesByPatIdAndCurrentDoc((long)3);
		
		recipe.getMedicines().add(new Medicine("medicine04"));
		recipe = UserDoctor01.AddMedFromRecipe(recipe);
		
		assertEquals(2, recipe.getMedicines().size());
		assertEquals("MEDICINE01", recipe.getMedicines().get(0).getMedicine());
		assertEquals("MEDICINE04", recipe.getMedicines().get(1).getMedicine());
		assertEquals(1, recipe.getMedicines().get(0).getId());	
		assertEquals(4, recipe.getMedicines().get(1).getId());
		recipe = UserDoctor01.DeleteMedFromRecipe(recipe.getId(), 4);
	}
	@Test
	public void AddPatientMedToRecipe_MedNotInDatabase() throws Exception {
		
		Recipe recipe = UserDoctor01.getPatRecipesByPatIdAndCurrentDoc((long)3);
		
		assertEquals(1, recipe.getMedicines().size());
		assertEquals("MEDICINE01", recipe.getMedicines().get(0).getMedicine());
		
		recipe.getMedicines().add(new Medicine("caramelo"));
		recipe = UserDoctor01.AddMedFromRecipe(recipe);
		
		assertEquals(2, recipe.getMedicines().size());
		assertEquals("MEDICINE01", recipe.getMedicines().get(0).getMedicine());
		assertEquals("CARAMELO", recipe.getMedicines().get(1).getMedicine());
		assertEquals(1, recipe.getMedicines().get(0).getId());	
		assertEquals(6, recipe.getMedicines().get(1).getId());
		recipe = UserDoctor01.DeleteMedFromRecipe(recipe.getId(), 6);
	}
	@Test
	public void DeletePatientMedFromTheRecipe() throws Exception{
		Recipe recipe = UserDoctor01.getPatRecipesByPatIdAndCurrentDoc((long)3);
		recipe = UserDoctor01.DeleteMedFromRecipe(recipe.getId(), 1);
		assertEquals(0, recipe.getMedicines().size());
		
		recipe.getMedicines().add(new Medicine("medicine01"));
		recipe = UserDoctor01.AddMedFromRecipe(recipe);
	}
	@Test
	public void GetDoctorFromPatientId(){
		List<Doctor> doctorList = UserPatient04.getDoctorsFromPatientId(5);
		
		assertEquals(2, doctorList.size());
		assertEquals("DOCNAME01", doctorList.get(0).getName());
		assertEquals("DOCNAME02", doctorList.get(1).getName());
	}
	@Test
	public void GetPatientLastRecipeByDoctorId(){
		Recipe recipe = UserPatient01.getCurrentPatientLastRecipeByDoctorId(1);
		
		assertEquals(1, recipe.getMedicines().size());
		assertEquals("MEDICINE01", recipe.getMedicines().get(0).getMedicine());
		assertEquals(1, recipe.getPatRecord().getId());
	}
	
	@Test
	public void AddPatientRecipeAnswerTypeAndReturnItFromServer(){
		
		List<Answer> answerList = new ArrayList<Answer>();
		
		answerList.add(new Answer(AnswerType.PAIN_SORE, AnswerValue.MODERATE, null, null));
		answerList.add(new Answer(AnswerType.PAIN_MED_SPECIFIC, AnswerValue.YES, "30-11-2014/1:59", null));
		answerList.add(new Answer(AnswerType.PAIN_STOP_EATING_DRINKING, AnswerValue.I_CANT_EAT, null, null));
		
		CheckIn checkIn = UserPatient01.AddPatientCheckIn(1, answerList);
		assertEquals(1, checkIn.getId());
		assertEquals(3, checkIn.getAnswers().size());
		assertEquals(1, checkIn.getAnswers().get(0).getId());
		assertEquals(2, checkIn.getAnswers().get(1).getId());
		assertEquals(3, checkIn.getAnswers().get(2).getId());
		
		assertEquals(AnswerType.PAIN_SORE, checkIn.getAnswers().get(0).getAnswerType());
		assertEquals(AnswerValue.MODERATE, checkIn.getAnswers().get(0).getAnswerValue());
		assertEquals(null, checkIn.getAnswers().get(0).getDateAndTimeTaken());
		
		assertEquals(AnswerType.PAIN_MED_SPECIFIC, checkIn.getAnswers().get(1).getAnswerType());
		assertEquals(AnswerValue.YES, checkIn.getAnswers().get(1).getAnswerValue());
		assertEquals("30-11-2014/1:59", checkIn.getAnswers().get(1).getDateAndTimeTaken());
		
		assertEquals(AnswerType.PAIN_STOP_EATING_DRINKING, checkIn.getAnswers().get(2).getAnswerType());
		assertEquals(AnswerValue.I_CANT_EAT, checkIn.getAnswers().get(2).getAnswerValue());
		assertEquals(null, checkIn.getAnswers().get(2).getDateAndTimeTaken());
		
		List<CheckIn> checkInList = UserDoctor01.getPatCheckInsForCurrentDoc(3);
		
		assertEquals(1, checkInList.size());
		assertEquals(1, checkInList.get(0).getId());
		
		CheckIn oneCheckIn = UserDoctor01.getPatCheckInByCheckInId(1);
		assertEquals(AnswerType.PAIN_SORE, checkIn.getAnswers().get(0).getAnswerType());
		assertEquals(AnswerValue.MODERATE, checkIn.getAnswers().get(0).getAnswerValue());
		assertEquals(null, checkIn.getAnswers().get(0).getDateAndTimeTaken());
		
	}
	
	//Delete data.
}
