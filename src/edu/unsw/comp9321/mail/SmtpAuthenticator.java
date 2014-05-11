package edu.unsw.comp9321.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {
	public SmtpAuthenticator() {
	
	    super();
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
	 String username = "ctat882";
	 String password = "loki2586";
	    if ((username != null) && (username.length() > 0) && (password != null) 
	      && (password.length   () > 0)) {
	
	        return new PasswordAuthentication(username, password);
	    }
	
	    return null;
	}
}
