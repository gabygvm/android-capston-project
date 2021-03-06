package org.magnum.symptoms.service.repository;

import java.util.List;

import org.magnum.symptoms.service.client.UserSvcApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store Doctor objects and allowed them to be searched.
 * 
 * @author Gabriela Vera
 *
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	public List<Doctor> findByUsername(@Param(UserSvcApi.USERNAME_PARAMETER)String username);
	
	public List<Doctor> findByPatientRecordPatientId(@Param("patient_id")long patientId);
}
