import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

public class UserClass implements SessionAware {

	private String username;
	private String password;

	/** Store data from sessions */
	private Map<String, Object> session;

	public UserClass() {

	}

	/** register actions */
	public String register() {

		try {
			/** connect to my database */
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StrutsCA2?serverTimezone=UTC",
					"root", "root");

			/** simple sql to check if username is already taken */
			PreparedStatement checkUser = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
			checkUser.setString(1, username);

			ResultSet rset = checkUser.executeQuery();

			if (rset.next()) {
				/** if somebody took the username the registration will fail */
				rset.close();
				checkUser.close();
				conn.close();
				return "FAILURE";
			}

			/** insert the new user into my database */
			PreparedStatement insertUser = conn
					.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
			insertUser.setString(1, username);
			insertUser.setString(2, password);
			insertUser.executeUpdate();

			insertUser.close();
			conn.close();

			/** store user in session */
			session.put("loggedUser", username);

			return "SUCCESS";

		} catch (SQLException e) {
			e.printStackTrace();
			return "FAILURE";
		}
	}

	public String login() {

		try {
			/** connect to my database */
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/StrutsCA2?serverTimezone=UTC",
					"root", "root");

			/** check if name and password match the database */
			PreparedStatement checkLogin = conn
					.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
			checkLogin.setString(1, username);
			checkLogin.setString(2, password);

			ResultSet rset = checkLogin.executeQuery();

			if (rset.next()) {
				/** if matches succesful */
				session.put("loggedUser", username);

				rset.close();
				checkLogin.close();
				conn.close();

				return "SUCCESS";

			} else {
				/** not found in database than fail */
				rset.close();
				checkLogin.close();
				conn.close();

				return "FAILURE";
			}

		} catch (SQLException e) {
			e.printStackTrace();
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

	/** Getters + setters */
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
