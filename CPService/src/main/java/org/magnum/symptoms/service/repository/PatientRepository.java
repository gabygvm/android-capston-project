package org.magnum.symptoms.service.repository;

import java.util.List;

import org.magnum.symptoms.service.client.UserSvcApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store Patients objects and allow them to
 * be searched by username.
 * 
 * @author Gabriela Vera
 *
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
	 public List<Patient> findByUsername(@Param(UserSvcApi.USERNAME_PARAMETER)String username);
	 
	 public List<Patient> findByNameAndLastNameAndPatientRecordDoctorId(@Param("name")String name, @Param("lastname")String lastName, @Param("docId")long docId);
	 
	 public List<Patient> findByPatientRecordDoctorId(@Param("docId") long docId); 
	 
	 public List<Patient> findByNameAndPatientRecordDoctorId(@Param("name")String name, @Param("docId") long docId);
	 public List<Patient> findByLastNameAndPatientRecordDoctorId(@Param("lastname")String lastName, @Param("docId") long docId);
}
