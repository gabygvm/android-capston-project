package org.magnum.symptoms.service.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A simple object to represent a Patient
 * @author Gabriela Vera
 */
@Entity
public class Patient extends Person implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Transient
	Collection<RolePatient> authorities = new ArrayList<RolePatient>();

//	private Collection<Recipe> recipes;

	public Patient() {
		super();
		authorities.add(new RolePatient());
	}
	
	public Patient(String name, String lastName, Date birthDate, boolean isFemale,
			String username, String password)
	{
		super(name, lastName, birthDate, isFemale, username, password);
		authorities.add(new RolePatient());
	}

	public long getId() {
		return this.id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	@Override
	public String getPassword() {
		return this.getPassword();
	}
	@Override
	public String getUsername() {
		return this.getUsername();
	}
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}

	public class RolePatient implements GrantedAuthority{
		
		@Override
		public String getAuthority() {
			return "ROLE_PATIENT";
		}	
	}

}


