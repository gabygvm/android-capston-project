package org.magnum.symptoms.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store PatientsRecords objects 
 * 
 * @author Gabriela Vera
 */
@Repository
public interface PatientRecordRepository extends JpaRepository<PatientRecord, Long>  {
	@Query(value="from PatientRecord pr where pr.doctor.id = :docId")
	public List<PatientRecord> findByDoctorId(@Param("docId") long docId);
	//public List<Patient> findByPatient(@Param(UserSvcApi.USERNAME_PARAMETER)String username);
	
}
