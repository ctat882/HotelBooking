package edu.unsw.comp9321.logic;

import java.sql.Date;

public class InputValidator {

	public InputValidator(){};
	
	/**
	 * Check if a date is correct and in a sql format
	 * @param date
	 * @return True if the date is valid, false otherwise
	 */
	public boolean isValidDate (Date date) {
		boolean valid = false;
		//TODO
		return valid;
	}
	
	/**
	 * Check if a string contains only alpha-numeric characters.
	 * TODO this is UNTESTED! 
	 * @param string
	 * @return True if the string is valid, false otherwise.
	 */
	public boolean isValidText (String string) {
		boolean valid = false;
		if(string.matches("^[a-zA-Z0-9]+$")) valid = true;
		return valid;		
	}
	
	/**
	 * Check if the string is a valid email string, and does not contain
	 * any forbidden characters.
	 * @param email
	 * @return True if the email is valid, false otherwise.
	 */
	public boolean isValidEmail (String email) {
		boolean valid = false;
		//TODO
		return valid;
	}
	
	/**
	 * Check if the string is a valid url string.
	 * @param url
	 * @return True if the url is valid, false otherwise.
	 */
	public boolean isValidURL (String url) {
		boolean valid = false;
		//TODO
		return valid;
	}
}
