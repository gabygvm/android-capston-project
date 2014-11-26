package org.magnum.symptoms.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long>{
	public Medicine findByMedicine(@Param("medicine")String medName);
}
