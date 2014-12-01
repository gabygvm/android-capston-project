package org.magnum.symptoms.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store CheckIns and search for them
 * 
 * @author Gabriela Vera
 *
 */

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long>{

	public List<CheckIn> findByPatRecordId(@Param("patRecordId")long patId);
}
