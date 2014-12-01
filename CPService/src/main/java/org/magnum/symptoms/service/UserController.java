/*
 * 
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package org.magnum.symptoms.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.magnum.symptoms.service.client.UserSvcApi;
import org.magnum.symptoms.service.repository.Answer;
import org.magnum.symptoms.service.repository.AnswerRepository;
import org.magnum.symptoms.service.repository.CheckIn;
import org.magnum.symptoms.service.repository.CheckInRepository;
import org.magnum.symptoms.service.repository.Doctor;
import org.magnum.symptoms.service.repository.DoctorRepository;
import org.magnum.symptoms.service.repository.Medicine;
import org.magnum.symptoms.service.repository.MedicineRepository;
import org.magnum.symptoms.service.repository.Patient;
import org.magnum.symptoms.service.repository.PatientRecord;
import org.magnum.symptoms.service.repository.PatientRecordRepository;
import org.magnum.symptoms.service.repository.PatientRepository;
import org.magnum.symptoms.service.repository.Recipe;
import org.magnum.symptoms.service.repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import retrofit.http.Body;
import retrofit.http.POST;

@Controller
public class UserController {

	@Autowired
	private EntityManager entityMng;

	@Autowired
	private PatientRepository patientRepo;

	@Autowired
	private DoctorRepository doctorRepo;

	@Autowired
	private PatientRecordRepository patientRecordRepo;

	@Autowired
	private RecipeRepository recipeRepo;

	@Autowired
	private MedicineRepository medicineRepo;

	@Autowired
	private CheckInRepository checkInRepo;

	@Autowired
	private AnswerRepository answerRepo;

	@RequestMapping(value = UserSvcApi.ROLE_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody String getUserRole(HttpServletResponse response) {
		GrantedAuthority authority = null;
		if (!SecurityContextHolder.getContext().getAuthentication()
				.getAuthorities().isEmpty()) {
			authority = (GrantedAuthority) SecurityContextHolder.getContext()
					.getAuthentication().getAuthorities().toArray()[0];
		}

		if (authority == null) {
			SendError(response, 404);
		} else
			response.setStatus(HttpServletResponse.SC_OK);
		entityMng.clear();
		return authority.getAuthority();
	}

	@RequestMapping(value = UserSvcApi.USER_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody String getUserUserName(HttpServletResponse response) {
		entityMng.clear();
		return GetUsername();
	}

	@RequestMapping(value = UserSvcApi.PATIENT_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Patient getPatientInfo(HttpServletResponse response) {
		if (GetUsername() != null) {
			List<Patient> patList = patientRepo.findByUsername(GetUsername());
			if (patList.size() > 0) {
				response.setStatus(HttpServletResponse.SC_OK);
				patList.get(0).setPatientRecord(null);
				
				try {
					patList.get(0).setImageBase64(FileUtils.readFileToString(new File(patList.get(0).getId()+".patient")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				entityMng.clear();
				return patList.get(0);
			}
		}
		SendError(response, 404);
		entityMng.clear();
		return null;
	}

	@RequestMapping(value = UserSvcApi.PATIENT_SVC_PATH + "/{id}", method = RequestMethod.GET)
	public @ResponseBody Patient getPatientById(
			@PathVariable(UserSvcApi.ID_PARAMETER) long id,
			HttpServletResponse response) {
		Patient patient = null;

		patient = patientRepo.findOne(id);

		if (patient == null) {
			SendError(response, 404);
		} else
			response.setStatus(HttpServletResponse.SC_OK);

		patient.setPatientRecord(null);
		entityMng.clear();
		return patient;
	}

	@RequestMapping(value = UserSvcApi.PATIENT_DOCTORS_PATH, method = RequestMethod.GET)
	public @ResponseBody List<Doctor> getDoctorsFromPatientId(
			@PathVariable(UserSvcApi.ID_PARAMETER) long id,
			HttpServletResponse response) {

		List<Doctor> docList = doctorRepo.findByPatientRecordPatientId(id);

		if (docList != null) {
			if (docList.size() != 0) {
				for (int i = 0; i < docList.size(); i++) {
					docList.get(i).setPatientRecord(null);
				}
				response.setStatus(HttpServletResponse.SC_OK);
				entityMng.clear();
				return docList;
			}
		}
		entityMng.clear();
		SendError(response, 404);
		return null;
	}

	@RequestMapping(value = UserSvcApi.PATIENT_LAST_RECIPE_FROM_DOC_PATH, method = RequestMethod.GET)
	public @ResponseBody Recipe getCurrentPatientLastRecipeByDoctorId(
			@RequestParam("docId") long docId, HttpServletResponse response) {
		List<Patient> patList;
		List<Recipe> recipeList;
		Recipe patRecipe;

		if (GetUsername() != null) {
			patList = patientRepo.findByUsername(GetUsername());
			if (patList.size() != 0) {
				
				if(docId == 0)
				{	
					List<Doctor> docList = doctorRepo.findByPatientRecordPatientId(patList.get(0).getId());

					recipeList = recipeRepo.findByPatRecordDoctorIdAndPatRecordPatientIdOrderByIdDesc(
						docList.get(0).getId(), patList.get(0).getId());
				}
				else{
					recipeList = recipeRepo.findByPatRecordDoctorIdAndPatRecordPatientIdOrderByIdDesc(
								docId, patList.get(0).getId());
				}
				if (recipeList.size() != 0) {
					patRecipe = recipeList.get(0);
					patRecipe.getPatRecord().setRecipeList(null);
					patRecipe.getPatRecord().setDoctor(null);
					patRecipe.getPatRecord().setPatient(null);

					response.setStatus(HttpServletResponse.SC_OK);
					entityMng.clear();
					return patRecipe;
				}
			}
		}
		SendError(response, 404);
		entityMng.clear();
		return null;
	}

	@RequestMapping(value = UserSvcApi.PATIENT_CHECKIN_TO_DOC, method = RequestMethod.POST)
	public @ResponseBody CheckIn AddPatientCheckIn(
			@RequestParam("patRecordId") long patRecordId,
			@RequestBody List<Answer> answers, HttpServletResponse response) {

		for (int i = 0; i < answers.size(); i++) {
			answers.set(i, answerRepo.saveAndFlush(answers.get(i)));
		}

		CheckIn checkIn = new CheckIn(patientRecordRepo.findOne(patRecordId), answers);
		checkIn = checkInRepo.saveAndFlush(checkIn);

		checkIn.setPatRecord(null);
		entityMng.clear();
		return checkIn;
	}

	@RequestMapping(value = UserSvcApi.PATIENT_PHOTO, method = RequestMethod.POST)
	public @ResponseBody Patient SavePatientPhoto(@RequestBody String photo, HttpServletResponse response)
	{
		Patient patient = null;
		if (GetUsername() != null) {
			List<Patient> patList = patientRepo.findByUsername(GetUsername());
			if (patList.size() > 0) {
				response.setStatus(HttpServletResponse.SC_OK);
				
				patient = patientRepo.findOne(patList.get(0).getId());
				
				//patient.setImageBase64(photo);
				
				patient = patientRepo.saveAndFlush(patient);
				
				try {
					FileUtils.writeStringToFile(new File(patient.getId() + ".patient"), photo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				patient.setPatientRecord(null);
				patient.setImageBase64(null);
				entityMng.clear();
				return patient;
			}
		}
		SendError(response, 404);
		entityMng.clear();
		return patient;
	}
	
	@RequestMapping(value = UserSvcApi.PATIENT_IMAGE_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody byte[] getPatientImageById(
			@PathVariable(UserSvcApi.ID_PARAMETER) long id,
			HttpServletResponse response) {
		
		Patient patient = null;
		patient = patientRepo.findOne(id);

		if (patient == null) {
			SendError(response, 404);
		} else
			response.setStatus(HttpServletResponse.SC_OK);

		entityMng.clear();
		return null;
	}

	@RequestMapping(value = UserSvcApi.DOCTOR_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Doctor getDoctorInfo(HttpServletResponse response) {
		List<Doctor> doctList;

		if (GetUsername() != null) {
			doctList = doctorRepo.findByUsername(GetUsername());
			if (doctList.size() > 0) {
				response.setStatus(HttpServletResponse.SC_OK);
				doctList.get(0).setPatientRecord(null);
				entityMng.clear();
				return doctList.get(0);
			}
		}
		SendError(response, 404);
		entityMng.clear();
		return null;
	}

	@RequestMapping(value = UserSvcApi.DOCTOR_SVC_PATH + "/{id}", method = RequestMethod.GET)
	public @ResponseBody Doctor getDoctorById(
			@PathVariable(UserSvcApi.ID_PARAMETER) long id,
			HttpServletResponse response) {
		Doctor doctor = null;

		doctor = doctorRepo.findOne(id);

		if (doctor == null) {
			SendError(response, 404);
		} else
			response.setStatus(HttpServletResponse.SC_OK);

		doctor.setPatientRecord(null);
		entityMng.clear();
		return doctor;
	}

	@RequestMapping(value = UserSvcApi.DOCTOR_FIND_PATIENTS_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody List<Patient> getPatientsByDoctor(
			HttpServletResponse response) {
		List<Patient> patientList;
		List<Doctor> doctList;

		if (GetUsername() != null) {
			doctList = doctorRepo.findByUsername(GetUsername());
			patientList = patientRepo.findByPatientRecordDoctorId(doctList.get(
					0).getId());

			if (patientList.size() != 0) {
				for (int i = 0; i < patientList.size(); i++) {
					patientList.get(i).setPatientRecord(null);
				//	patientList.get(i).setImage(null);
				}
				response.setStatus(HttpServletResponse.SC_OK);
				entityMng.clear();
				return patientList;
			}
		}
		SendError(response, 404);
		entityMng.clear();
		return null;
	}

	@RequestMapping(value = UserSvcApi.DOCTOR_FIND_PATIENTS_NAME_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody List<Patient> getPatientsByDoctorWithNameAndLastName(
			@RequestParam("name") String name,
			@RequestParam("lastname") String lastname,
			HttpServletResponse response) {
		List<Doctor> doctList;

		if (GetUsername() != null) {
			List<Patient> patientList;
			doctList = doctorRepo.findByUsername(GetUsername());

			if (((name != "") || (lastname != "")) && (doctList != null)) {
				if (name == "") {
					patientList = patientRepo
							.findByLastNameAndPatientRecordDoctorId(lastname
									.toUpperCase(), doctList.get(0).getId());
				} else if (lastname == "") {
					patientList = patientRepo
							.findByNameAndPatientRecordDoctorId(
									name.toUpperCase(), doctList.get(0).getId());
				} else {
					patientList = patientRepo
							.findByNameAndLastNameAndPatientRecordDoctorId(
									name.toUpperCase(), lastname.toUpperCase(),
									doctList.get(0).getId());
				}

				if (patientList.size() != 0) {
					for (int i = 0; i < patientList.size(); i++)
					{
						patientList.get(i).setPatientRecord(null);
					//	patientList.get(i).setImage(null);
					}
					response.setStatus(HttpServletResponse.SC_OK);
					entityMng.clear();
					return patientList;
				}
			}
		}
		SendError(response, 404);
		entityMng.clear();
		return null;
	}

	@RequestMapping(value = UserSvcApi.DOCTOR_PATIENT_RECIPES_PATH, method = RequestMethod.GET)
	public @ResponseBody Recipe getPatRecipesByPatIdAndCurrentDoc(
			@RequestParam(UserSvcApi.ID_PARAMETER) long patId,
			HttpServletResponse response) {
		List<Doctor> doctList;
		List<Recipe> recipeList;
		Recipe patRecipe;

		if (GetUsername() != null) {
			doctList = doctorRepo.findByUsername(GetUsername());
			if (doctList.size() != 0) {
				recipeList = recipeRepo
						.findByPatRecordDoctorIdAndPatRecordPatientIdOrderByIdDesc(
								doctList.get(0).getId(), patId);
				if (recipeList.size() != 0) {
					patRecipe = recipeList.get(0);
					patRecipe.setPatRecord(null);

					response.setStatus(HttpServletResponse.SC_OK);
					entityMng.clear();
					return patRecipe;
				}
			}
		}
		SendError(response, 404);
		entityMng.clear();
		return null;
	}

	@RequestMapping(value = UserSvcApi.DOCTOR_PATIENT_ADD_MED_PATH, method = RequestMethod.POST)
	public @ResponseBody Recipe AddMedFromRecipe(@RequestBody Recipe recipe,
			HttpServletResponse response) {

		Recipe recipeUpdate;
		Medicine medicine;

		recipeUpdate = recipeRepo.findOne(recipe.getId());

		for (int i = 0; i < recipe.getMedicines().size(); i++) {
			if (recipe.getMedicines().get(i).getId() == 0) {
				medicine = medicineRepo.findByMedicine(recipe.getMedicines()
						.get(i).getMedicine().toUpperCase());
				if (medicine == null)
					medicineRepo.saveAndFlush(recipe.getMedicines().get(i));
				else
					recipe.getMedicines().set(i, medicine);
			}
		}
		recipeUpdate.setMedicines(recipe.getMedicines());
		recipeUpdate = recipeRepo.saveAndFlush(recipeUpdate);
		recipeUpdate.setPatRecord(null);
		entityMng.clear();
		response.setStatus(HttpServletResponse.SC_OK);
		return recipeUpdate;
	}

	@RequestMapping(value = UserSvcApi.DOCTOR_PATIENT_DELETE_MED_PATH, method = RequestMethod.POST)
	public @ResponseBody Recipe DeleteMedFromRecipe(
			@RequestParam("recipeId") long recipeId,
			@RequestParam("medicineId") long medId, HttpServletResponse response) {

		Recipe recipeUpdate = null;

		recipeUpdate = recipeRepo.findOne(recipeId);
		for (int i = 0; i < recipeUpdate.getMedicines().size(); i++) {
			if (recipeUpdate.getMedicines().get(i).getId() == medId)
				recipeUpdate.getMedicines().remove(i);
		}
		recipeUpdate = recipeRepo.saveAndFlush(recipeUpdate);
		recipeUpdate.setPatRecord(null);

		entityMng.clear();

		response.setStatus(HttpServletResponse.SC_OK);
		return recipeUpdate;
	}
	
	@RequestMapping(value = UserSvcApi.DOCTOR_PATIENT_CHECKINS_PATH, method = RequestMethod.GET)
	public @ResponseBody List<CheckIn> getPatCheckInsForCurrentDoc(
			@RequestParam(UserSvcApi.ID_PARAMETER) long patId,
			HttpServletResponse response) {
		
		List<Doctor> doctList;
		List<CheckIn> checkInList;
		PatientRecord patRecord;
		
		
		if (GetUsername() != null) {
			doctList = doctorRepo.findByUsername(GetUsername());
			if (doctList.size() != 0) {
				
				patRecord = patientRecordRepo.findByDoctorIdAndPatientId(doctList.get(0).getId(), patId);
				checkInList = checkInRepo.findByPatRecordId(patRecord.getId());				
				
				if (checkInList.size() != 0) {
					for(int i = 0; i < checkInList.size(); i++){
						checkInList.get(i).getPatRecord().setPatient(null);
						checkInList.get(i).getPatRecord().setRecipeList(null);
						checkInList.get(i).getPatRecord().setDoctor(null);
						checkInList.get(i).setAnswers(null);
					}
					response.setStatus(HttpServletResponse.SC_OK);
					entityMng.clear();
					return checkInList;
				}
			}
		}
		SendError(response, 404);
		entityMng.clear();
		return null;
	}
	
	@RequestMapping(value = UserSvcApi.DOCTOR_PATIENT_CHECKIN_PATH, method = RequestMethod.GET)
	public @ResponseBody CheckIn getPatCheckInByCheckInId(
			@PathVariable(UserSvcApi.ID_PARAMETER) long checkInId,
			HttpServletResponse response) {
		
		CheckIn checkIn;
		
		checkIn = checkInRepo.findOne(checkInId);
		if(checkIn != null)
		{
			checkIn.setPatRecord(null);
			
			response.setStatus(HttpServletResponse.SC_OK);
			entityMng.clear();
			return checkIn;
		}
		
		SendError(response, 404);
		entityMng.clear();
		return null;
	}
	

	private void SendError(HttpServletResponse response, int errorCode) {
		try {
			response.sendError(errorCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String GetUsername() {
		String username = SecurityContextHolder.getContext()
				.getAuthentication().getName();

		return username;
	}
}
