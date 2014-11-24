package org.magnum.symptoms.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.magnum.autograder.junit.Rubric;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
//import org.magnum.symptoms.service.TestData;
import org.magnum.symptoms.service.client.SecuredRestBuilder;
import org.magnum.symptoms.service.client.UserSvcApi;
import org.magnum.symptoms.service.repository.Doctor;
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
}
