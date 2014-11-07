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
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.magnum.symptoms.service.client.UserSvcApi;
import org.magnum.symptoms.service.repository.Doctor;
import org.magnum.symptoms.service.repository.DoctorRepository;
import org.magnum.symptoms.service.repository.Patient;
import org.magnum.symptoms.service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

	@Autowired
	PatientRepository patientRepo;
	@Autowired
	DoctorRepository doctorRepo;

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

		return patient;
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

		return doctor;
	}

	@RequestMapping(value = UserSvcApi.USER_SVC_PATH, method = RequestMethod.GET)
	public @ResponseBody String getUserRole(HttpServletResponse response) {
		GrantedAuthority authority = null;
		if(!SecurityContextHolder.getContext().getAuthentication().getAuthorities().isEmpty()) {
			authority = (GrantedAuthority) SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray()[0];
		}

		if (authority == null) {
			SendError(response, 404);
		}else
			response.setStatus(HttpServletResponse.SC_OK);
		
		return authority.getAuthority();
	}

	private void SendError(HttpServletResponse response, int errorCode) {
		try {
			response.sendError(errorCode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
