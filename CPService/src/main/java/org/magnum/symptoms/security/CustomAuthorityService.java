package org.magnum.symptoms.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.magnum.symptoms.service.repository.Doctor;
import org.magnum.symptoms.service.repository.DoctorRepository;
import org.magnum.symptoms.service.repository.Patient;
import org.magnum.symptoms.service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomAuthorityService implements UserDetailsService{
	
	@Autowired
	PatientRepository patientRepo;
	@Autowired
	DoctorRepository doctorRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		UserDetails user = null;
		
		Collection<Patient> patientCollection = patientRepo.findByUsername(username);
		Collection<Doctor> doctorCollection = doctorRepo.findByUsername(username);
		
		if(patientCollection != null) //Then again... this must assume that the userName is unique...
		{
			List<Patient> list = new ArrayList<Patient>(patientCollection);
			user = (Patient) list.get(0);
		}
		else if(doctorCollection != null)
		{
			List<Doctor> list = new ArrayList<Doctor>(doctorCollection);
			user = (Doctor) list.get(0);	
		}
		
		return user;
	}
}
