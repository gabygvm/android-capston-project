package org.magnum.symptoms.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store Video objects and allow them to
 * be searched by title.
 * 
 * @author Gabriela Vera
 *
 */
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	// public Collection<Doctor> findByUsername(String username);
}
