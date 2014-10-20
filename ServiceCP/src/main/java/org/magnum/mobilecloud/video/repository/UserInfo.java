package org.magnum.mobilecloud.video.repository;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A simple object to represent a Patient
 * @author Gabriela Vera
 */
@Entity
public class UserInfo extends Person{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

//	private Collection<DoctorInfo> doctors;
	private Collection<Recipe> recipes;

	public UserInfo() {
		super();
	}
	public UserInfo(String name, String lastName,Date birthdate, boolean isFemale,
			Collection<Recipe> recipes) {
		super();
		this.setName(name);
		this.setLastName(lastName);
		this.setBirthDate(birthdate);
		this.setIsFemale(isFemale);
		this.recipes = recipes;
//		this.doctors = doctorCollection; Collection<DoctorInfo> doctorCollection
	}
	public UserInfo(Collection<Recipe> recipes) {
		super();
		this.recipes = recipes;
	}

	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Collection<Recipe> getRecipes() {
		return recipes;
	}
	public void setRecipes(Collection<Recipe> recipes) {
		this.recipes = recipes;
	}

/*	public Collection<DoctorInfo> getDoctors(){
		return this.doctors;
	}*/
	
	
	/*@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(name, url, duration);
	}

	/**
	 * Two Videos are considered equal if they have exactly the same values for
	 * their name, url, and duration.
	 * 
	 */
/*	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserInfo) {
			UserInfo other = (UserInfo) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(name, other.name)
					&& Objects.equal(url, other.url)
					&& duration == other.duration;
		} else {
			return false;
		}
	}
*/
}
