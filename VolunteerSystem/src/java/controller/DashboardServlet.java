package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.User;
import util.DBConnection;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    private int count(Connection con, String sql) throws Exception {
        ResultSet rs = con.prepareStatement(sql).executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {
            // Admin sees all applications, volunteer sees only their own
            String filter = user.getRole().equals("Admin") ? "" : " AND USER_ID = " + user.getUserId();

            request.setAttribute("totalVolunteers", count(con, "SELECT COUNT(*) FROM USERS WHERE ROLE = 'Volunteer'"));
            request.setAttribute("totalEvents", count(con, "SELECT COUNT(*) FROM EVENT"));
            request.setAttribute("totalApplications", count(con, "SELECT COUNT(*) FROM APPLICATION WHERE 1=1" + filter));
            request.setAttribute("pending", count(con, "SELECT COUNT(*) FROM APPLICATION WHERE STATUS = 'Pending'" + filter));

            // Chart 1: number of events per category
            ArrayList<String> catLabels = new ArrayList<>();
            ArrayList<Integer> catValues = new ArrayList<>();
            ResultSet rs = con.prepareStatement(
                    "SELECT c.CATEGORY_NAME, COUNT(e.EVENT_ID) FROM CATEGORY c "
                    + "LEFT JOIN EVENT e ON c.CATEGORY_ID = e.CATEGORY_ID "
                    + "GROUP BY c.CATEGORY_NAME").executeQuery();
            while (rs.next()) {
                catLabels.add(rs.getString(1));
                catValues.add(rs.getInt(2));
            }
            request.setAttribute("catLabels", catLabels);
            request.setAttribute("catValues", catValues);

            // Chart 2: applications by status
            ArrayList<String> statusLabels = new ArrayList<>();
            ArrayList<Integer> statusValues = new ArrayList<>();
            rs = con.prepareStatement("SELECT STATUS, COUNT(*) FROM APPLICATION WHERE 1=1" + filter
                    + " GROUP BY STATUS ORDER BY STATUS").executeQuery();
            while (rs.next()) {
                statusLabels.add(rs.getString(1));
                statusValues.add(rs.getInt(2));
            }
            request.setAttribute("statusLabels", statusLabels);
            request.setAttribute("statusValues", statusValues);

            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
