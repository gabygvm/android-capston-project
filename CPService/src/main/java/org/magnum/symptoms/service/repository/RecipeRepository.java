package org.magnum.symptoms.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long>{
	
	public List<Recipe> findByPatRecordDoctorIdAndPatRecordPatientIdOrderByIdDesc(
			@Param("docId") long docId, @Param("patId") long patId);
}
