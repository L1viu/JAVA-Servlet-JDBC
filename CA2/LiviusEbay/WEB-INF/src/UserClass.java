import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class UserClass implements SessionAware {

	private String username;
	private String password;
	
	/** Store data from sessions */
	private Map<String, Object> session;

	public UserClass() {

	}

	/**register actions*/
	public String register() {
		session.put("loggedUser", username);
		return "SUCCESS";
	}

	
	public String login() {
		/**testing the login before I add database functionality*/
		if (username != null && username.equalsIgnoreCase("liviu")) {
			session.put("loggedUser", username);
			return "SUCCESS";
		} else {
			return "FAILURE";
		}
	}

	
	public String logout() {
		session.remove("loggedUser");
		return "SUCCESS";
	}

	@Override
	public void setSession(Map map) {
		session = map;
	}

	/**Getters + setters*/
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}