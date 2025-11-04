import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class DashboardServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        /** Make the response type to html */
        response.setContentType("text/html");
        /**Send text back to browser*/
        PrintWriter out = response.getWriter();

        /**Get data from the dashboard.html file*/
        String gamerTag = request.getParameter("gamerTag");
        String action = request.getParameter("action");
        String amountStr = request.getParameter("amount");

        /**Ensure all fields are filled in*/
        if (gamerTag == null || action == null || amountStr == null ||
            gamerTag.isEmpty() || amountStr.isEmpty()) {

            out.println("<html><body>");
            out.println("<h2>All fields are required!</h2>");
            out.println("<a href='Dashboard.html'>Go back</a>");
            out.println("</body></html>");
            return;
        }

        int amount = Integer.parseInt(amountStr);

        try {
            /**Load MySQL JDBC driver*/
            Class.forName("com.mysql.cj.jdbc.Driver");

            /**Establish connection to gamingportal database*/
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/gamingportal?serverTimezone=UTC", "root", "root");

            /**Check if gamerTag exists and get their credits*/
            PreparedStatement checkPlayer = conn.prepareStatement(
                "SELECT credits FROM players WHERE gamerTag = ?");
            checkPlayer.setString(1, gamerTag);
            ResultSet rs = checkPlayer.executeQuery();

            if (rs.next()) {
                int credits = rs.getInt("credits");

                /**Earn and Spend credits*/
                if (action.equals("earn")) {
                    credits += amount;
                } else if (action.equals("spend")) {
                    if (credits - amount < 0) {
                        out.println("<html><body>");
                        out.println("<h2>Error: You don't have enough credits!</h2>");
                        out.println("<a href='Dashboard.html'>Go back</a>");
                        out.println("</body></html>");
                        rs.close();
                        checkPlayer.close();
                        conn.close();
                        return;
                    } else {
                        credits -= amount;
                    }
                }

                /**Update player credits in the database*/
                PreparedStatement updateCredits = conn.prepareStatement(
                    "UPDATE players SET credits = ? WHERE gamerTag = ?");
                updateCredits.setInt(1, credits);
                updateCredits.setString(2, gamerTag);
                updateCredits.executeUpdate();

                /**Display updated balance*/
                out.println("<html><body>");
                out.println("<h2>Hi " + gamerTag + "!</h2>");
                out.println("<p>Your new game credit balance is: <b>" + credits + "</b></p>");
                out.println("<a href='Dashboard.html'>Go back</a>");
                out.println("</body></html>");

                updateCredits.close();
            } else {
                out.println("<html><body>");
                out.println("<h2>Error: Gamer tag not found. Please log in again.</h2>");
                out.println("<a href='Login.html'>Login</a>");
                out.println("</body></html>");
            }

            rs.close();
            checkPlayer.close();
            conn.close();

        } catch (SQLException e) {
            out.println("<html><body>");
            out.println("<h2>Error: Database issue or invalid data.</h2>");
            out.println("<a href='Dashboard.html'>Try again</a>");
            out.println("</body></html>");
        } catch (ClassNotFoundException e) {
            out.println("<html><body>");
            out.println("<h2>Error: MySQL JDBC Driver not found.</h2>");
            out.println("<a href='Dashboard.html'>Try again</a>");
            out.println("</body></html>");
        }
    }
}
