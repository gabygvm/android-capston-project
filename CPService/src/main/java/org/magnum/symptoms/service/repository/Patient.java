package org.magnum.symptoms.service.repository;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.security.oauth2.common.util.JsonDateSerializer;

/**
 * A simple object to represent a Patient
 * 
 * @author Gabriela Vera
 */
@Entity
public class Patient extends Person /* implements UserDetails */{


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	public Patient() {
		super();
	}

	public Patient(String name, String lastName, String birthDate,
			boolean isFemale, String username, String password) {
		super(name, lastName, birthDate, isFemale, username, password);
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/*@Override
	public Date getBirthDate() {
		// TODO Auto-generated method stub
		return super.getBirthDate();
	}*/
	/*
	@Transient
	public String getBirthDateString(){
		return new SimpleDateFormat("dd/MM/yyyy").format(this.getBirthDate());
	}
	
	public void setBirthDateString(String date){
		try {
			this.setBirthDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/*
	 * 
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
	 * public class RolePatient implements GrantedAuthority{
	 * 
	 * @Override public String getAuthority() { return "ROLE_PATIENT"; } }
	 */

}
