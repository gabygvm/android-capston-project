package org.magnum.symptoms.service.repository;

import java.util.Collection;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * An interface for a repository that can store Video
 * objects and allow them to be searched by title.
 * 
 * @author Gabriela Vera
 *
 */
@Repository
public interface PatientRepository extends CrudRepository<Patient, Long>
{
	public Collection<Patient> findByUsername(String username);
}
