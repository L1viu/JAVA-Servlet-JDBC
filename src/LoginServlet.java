import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        /** Make the response type to html */
        response.setContentType("text/html");

        /** Send text back to browser */
        PrintWriter out = response.getWriter();

        /** Get the data from the login.html file */
        String gamerTag = request.getParameter("gamerTag");
        String password = request.getParameter("password");

        /** Ensure gamer tag and password are not left empty */
        if (gamerTag == null || password == null || gamerTag.isEmpty() || password.isEmpty()) {
            out.println("<html><body>");
            out.println("<h2>Fields must not be empty.</h2>");
            out.println("<a href='Login.html'>Go back</a>");
            out.println("</body></html>");
            return;
        }

        try {
            /** Load the MySQL JDBC driver */
            Class.forName("com.mysql.cj.jdbc.Driver");

            /** Establish a connection to mysql and gamingportal database */
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gamingportal?serverTimezone=UTC", "root", "root");

            /** Check if gamerTag and password match in the database */
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT credits FROM players WHERE gamerTag = ? AND password = ?");
            stmt.setString(1, gamerTag);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            /** If a match is found, login worked */
            if (rs.next()) {
                int credits = rs.getInt("credits");

                out.println("<html><body>");
                out.println("<h2>Welcome back, " + gamerTag + "!</h2>");
                out.println("<p>Your credit balance is: <b>" + credits + "</b></p>");
                out.println("<a href='Dashboard.html'>Go to Dashboard</a>");
                out.println("</body></html>");
            } else {
                /** If no match is found, login failed */
                out.println("<html><body>");
                out.println("<h2>Invalid gamer tag or password.</h2>");
                out.println("<a href='Login.html'>Try again</a>");
                out.println("</body></html>");
            }

            /** Close the connection */
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            /** Catch SQL or connection issues */
            out.println("<html><body>");
            out.println("<h2>Database issue or syntax error.</h2>");
            out.println("<a href='Login.html'>Try again</a>");
            out.println("</body></html>");
        } catch (ClassNotFoundException e) {
            /** Catch if JDBC driver is not found */
            out.println("<html><body>");
            out.println("<h2>Error: MySQL Driver did not connect to the database</h2>");
            out.println("</body></html>");
        }
    }
}
