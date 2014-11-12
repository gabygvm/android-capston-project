package org.magnum.mobilecloud.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface for a repository that can store UserInfo
 * objects and allow them to be searched by Name.
 * 
 * @author Gabriela Vera
 *
 */
@Repository
public interface RepositoryUserInfo extends CrudRepository<UserInfo, Long>
{
	// Find all videos with a matching title (e.g., Video.name)
//	public Collection<UserInfo> findByName(String name, String lastName);
	
//	public Collection<UserInfo> findByDurationLessThan(long duration);

}
