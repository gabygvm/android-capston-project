package org.magnum.symptoms.service.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A simple object to represent a Patient
 * 
 * @author Gabriela Vera
 */
@Entity
public class Doctor extends Person /* implements UserDetails */{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/*
	 * @Transient Collection<RoleDoctor> authorities = new
	 * ArrayList<RoleDoctor>();
	 */
	public Doctor() {
		super();
		// authorities.add(new RoleDoctor());
	}

	public Doctor(String name, String lastName, String birthDate,
			boolean isFemale, String username, String password) {
		super(name, lastName, birthDate, isFemale, username, password);
		// authorities.add(new RoleDoctor());
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/*
	 * @Override public Collection<? extends GrantedAuthority> getAuthorities()
	 * { return authorities; }
	 * 
	 * @Override public String getPassword() { return this.getPassword(); }
	 * 
	 * @Override public String getUsername() { return this.getUsername(); }
	 * 
	 * @Override public boolean isAccountNonExpired() { return true; }
	 * 
	 * @Override public boolean isAccountNonLocked() { return true; }
	 * 
	 * @Override public boolean isCredentialsNonExpired() { return true; }
	 * 
	 * @Override public boolean isEnabled() { return true; }
	 * 
	 * public class RoleDoctor implements GrantedAuthority{
	 * 
	 * @Override public String getAuthority() { return "ROLE_DOCTOR"; } }
	 */
}
