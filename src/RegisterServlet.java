import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class RegisterServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        /** Make the response type to html*/
        response.setContentType("text/html");
        /**Send text back to browser*/
        PrintWriter out = response.getWriter();

        /**Get the data from the register.html file*/
        String gamerTag = request.getParameter("gamerTag");
        String password1 = request.getParameter("password1");
        String password2 = request.getParameter("password2");

        /**Ensure passwords are not left empty and that passwords are matching*/
        if (gamerTag == null || password1 == null || password2 == null || !password1.equals(password2)) {
            out.println("<html><body>");
            out.println("<h2>Passwords are not matching or fields must not be empty.</h2>");
            out.println("<a href='Register.html'>Go back</a>");
            out.println("</body></html>");
            return;
        }

        try {
            /** Load the MySQL JDBC driver (required for database connection) */
            Class.forName("com.mysql.cj.jdbc.Driver");

            /**establish a connection to mysql and gamingportal database*/
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gamingportal?serverTimezone=UTC", "root", "root");

            /**Insert data for new player in the database, all players will start with 500 credits*/
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO players (gamerTag, password, credits) VALUES (?, ?, 500)");
            stmt.setString(1, gamerTag);
            stmt.setString(2, password1);

            int rows = stmt.executeUpdate(); /**This will execute the insert*/

            /** If the insert is successful display a welcome message to the new player. */
            if (rows > 0) {
                out.println("<html><body>");
                out.println("<h2>Welcome, " + gamerTag + "!</h2>");
                out.println("<p>Your account has been created, you have 500 game credits.</p>");
                out.println("<a href='Login.html'>Login now</a>");
                out.println("</body></html>");
            }

            /**Close the connection to the database*/
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            /** This will catch 2 simple errors if they occur. This will catch duplicates or issues with the database such as syntax errors and will display a message*/
            out.println("<html><body>");
            out.println("<h2>Error: Gamer Tag is already in use or otherwise database issue</h2>");
            out.println("<a href='Register.html'>Try again</a>");
            out.println("</body></html>");
        } catch (ClassNotFoundException e) {
            /** This will catch the error if the JDBC driver is missing */
            out.println("<html><body>");
            out.println("<h2>Error: MySQL Driver did not connect to the database.</h2>");
            out.println("</body></html>");
        }
    }
}
