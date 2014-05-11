package edu.unsw.comp9321.jdbc;

import java.util.ArrayList;

public interface UserDAO {
	// Verify user login details
	Boolean verifyLogin(UserDTO user);	
}
