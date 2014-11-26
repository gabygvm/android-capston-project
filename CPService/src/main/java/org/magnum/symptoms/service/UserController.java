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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import org.magnum.symptoms.service.client.UserSvcApi;
import org.magnum.symptoms.service.repository.Doctor;
import org.magnum.symptoms.service.repository.DoctorRepository;
import org.magnum.symptoms.service.repository.Medicine;
import org.magnum.symptoms.service.repository.MedicineRepository;
import org.magnum.symptoms.service.repository.Patient;
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

	
	@RequestMapping(value = UserSvcApi.ROLE_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody String getUserRole(HttpServletResponse response) {
		GrantedAuthority authority = null;
		if(!SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty()) {
			authority = (GrantedAuthority) SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0];
		}

		if (authority == null) {
			SendError(response, 404);
		}else
			response.setStatus(HttpServletResponse.SC_OK);
		entityMng.clear();
		return authority.getAuthority();
	}
	@RequestMapping(value = UserSvcApi.USER_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody String getUserUserName(HttpServletResponse response)
	{
		entityMng.clear();
		return GetUsername();
	}
	
	@RequestMapping(value = UserSvcApi.PATIENT_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Patient getPatientInfo(HttpServletResponse response)
	{
		if(GetUsername() != null)
		{
			List<Patient> patList = patientRepo.findByUsername(GetUsername());
			if(patList.size() > 0)
			{
				response.setStatus(HttpServletResponse.SC_OK);
				patList.get(0).setPatientRecord(null);
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
		}else
			response.setStatus(HttpServletResponse.SC_OK);

		patient.setPatientRecord(null);
		entityMng.clear();
		return patient;
	}
	/*@RequestMapping(value = UserSvcApi.PATIENT_BY_USERNAME_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody List<Patient> getPatientByUsername(
			@RequestParam(UserSvcApi.USERNAME_PARAMETER) String username,
			HttpServletResponse response)
	{
		return patientRepo.findByUsername(username);
	}*/

	@RequestMapping(value = UserSvcApi.DOCTOR_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody Doctor getDoctorInfo(HttpServletResponse response)
	{
		List<Doctor> doctList;
		
		if(GetUsername() != null)
		{
			doctList = doctorRepo.findByUsername(GetUsername());
			if(doctList.size() > 0)
			{
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
		}else
			response.setStatus(HttpServletResponse.SC_OK);
		
		doctor.setPatientRecord(null);
		entityMng.clear();
		return doctor;
	}
	@RequestMapping(value = UserSvcApi.DOCTOR_FIND_PATIENTS_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody List<Patient> getPatientsByDoctor(HttpServletResponse response)
	{
		List<Patient> patientList;
		List<Doctor> doctList;
		
		if(GetUsername() != null)
		{
			doctList = doctorRepo.findByUsername(GetUsername());
			patientList = patientRepo.findByPatientRecordDoctorId(doctList.get(0).getId());
			
			if(patientList.size() != 0)
			{
				for(int i = 0; i < patientList.size(); i++)
				{
					patientList.get(i).setPatientRecord(null);
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
			@RequestParam("name")String name, @RequestParam("lastname")String lastname,
			HttpServletResponse response)
	{
		List<Doctor> doctList;
		
		if(GetUsername() != null)
		{
			List<Patient> patientList;
			doctList = doctorRepo.findByUsername(GetUsername());
			
			if(((name != "") || (lastname != "")) && (doctList != null))
			{
				if(name == ""){
					patientList = patientRepo.findByLastNameAndPatientRecordDoctorId(
							lastname.toUpperCase(), doctList.get(0).getId());
				}
				else if(lastname == ""){
					patientList = patientRepo.findByNameAndPatientRecordDoctorId(
							name.toUpperCase(), doctList.get(0).getId());
				}
				else{
					patientList = patientRepo.findByNameAndLastNameAndPatientRecordDoctorId(
						name.toUpperCase(), lastname.toUpperCase(), doctList.get(0).getId());
				}
				
				if(patientList.size() != 0)
				{
					for(int i = 0; i < patientList.size(); i++)
						patientList.get(i).setPatientRecord(null);
				
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
	public @ResponseBody Recipe getPatRecipesByPatIdAndCurrentDoc(@RequestParam(
			UserSvcApi.ID_PARAMETER) long patId, HttpServletResponse response)
	{
		List<Doctor> doctList;
		List<Recipe> recipeList;
		Recipe patRecipe;
		
		if(GetUsername() != null)
		{
			doctList = doctorRepo.findByUsername(GetUsername());
			if(doctList.size() != 0)
			{
				recipeList = recipeRepo.findByPatRecordDoctorIdAndPatRecordPatientIdOrderByIdDesc(doctList.get(0).getId(), patId);
				if(recipeList.size() != 0)
				{
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
	
	@RequestMapping(value=UserSvcApi.DOCTOR_PATIENT_ADD_MED_PATH, method=RequestMethod.POST)
	public @ResponseBody Recipe AddMedFromRecipe(@RequestBody Recipe recipe, HttpServletResponse response){
	
		Recipe recipeUpdate;
		Medicine medicine;
		
		recipeUpdate = recipeRepo.findOne(recipe.getId());
		
		for(int i = 0; i < recipe.getMedicines().size(); i++)
		{
			if(recipe.getMedicines().get(i).getId() == 0)
			{
				medicine = medicineRepo.findByMedicine(recipe.getMedicines().get(i).getMedicine().toUpperCase());
				if(medicine == null)
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
	
	@RequestMapping(value=UserSvcApi.DOCTOR_PATIENT_DELETE_MED_PATH, method=RequestMethod.POST)
	public @ResponseBody Recipe DeleteMedFromRecipe(
			@RequestParam("recipeId") long recipeId, @RequestParam("medicineId") long medId,
			HttpServletResponse response){
		
		Recipe recipeUpdate = null;
		List<Medicine> medList;
		List<Recipe> recipeNew;
		
		recipeUpdate = recipeRepo.findOne(recipeId);
		for(int i = 0; i < recipeUpdate.getMedicines().size(); i++)
		{
			if(recipeUpdate.getMedicines().get(i).getId() == medId)
				recipeUpdate.getMedicines().remove(i);
		}
		recipeUpdate = recipeRepo.saveAndFlush(recipeUpdate);
		recipeUpdate.setPatRecord(null);
		
		entityMng.clear();
		
		
		medList = medicineRepo.findAll();
		recipeNew = recipeRepo.findAll();
		response.setStatus(HttpServletResponse.SC_OK);
		return recipeUpdate;
	}
	
	/*@RequestMapping(value = UserSvcApi.DOCTOR_BY_USERNAME_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody List<Doctor> getDoctorByUsername(
			@RequestParam(UserSvcApi.USERNAME_PARAMETER) String username,
			HttpServletResponse response)
	{
		return doctorRepo.findByUsername(username);
	}*/

/*	@RequestMapping(value = UserSvcApi.DOCTOR_FIND_PAT_RECORDS_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody List<PatientRecord> getPatientRecordByDoctor(HttpServletResponse response)
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		List<Doctor> doctList;
		List<PatientRecord> patRecord;

		if(username != null)
		{
			doctList = doctorRepo.findByUsername(username);
			
			if(doctList.size() > 0)
			{
				patRecord = patientRecordRepo.findByDoctorId(doctList.get(0).getId());
				
				if(patRecord.size() > 0)
				{
					response.setStatus(HttpServletResponse.SC_OK);
					return patRecord;
				}
			}
		}
		SendError(response, 404);
		return null;
	}*/
	
	
	private void SendError(HttpServletResponse response, int errorCode) {
		try {
			response.sendError(errorCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String GetUsername()
	{
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		return username;
	}
}
