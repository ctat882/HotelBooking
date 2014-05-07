package edu.unsw.comp9321.jdbc;

public class UserDTO {

	String username;
	String password;
	String hoteLoc;
	
	public UserDTO(String username, String password, String hotelLoc){
		this.username = username;
		this.password = password;
		this.hoteLoc = hotelLoc;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}	
		
	public String getHoteLoc() {
		return hoteLoc;
	}
	

	
}
