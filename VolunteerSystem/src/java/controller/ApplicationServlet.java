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
import model.Application;
import model.User;
import util.DBConnection;

@WebServlet("/ApplicationServlet")
public class ApplicationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        boolean isAdmin = user.getRole().equals("Admin");
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try (Connection con = DBConnection.getConnection()) {

            if (action.equals("apply") && !isAdmin) {
                // CREATE: volunteer applies for an event
                int eventId = Integer.parseInt(request.getParameter("eventId"));
                PreparedStatement check = con.prepareStatement(
                        "SELECT * FROM APPLICATION WHERE USER_ID = ? AND EVENT_ID = ?");
                check.setInt(1, user.getUserId());
                check.setInt(2, eventId);
                if (check.executeQuery().next()) {
                    request.getSession().setAttribute("msg", "You already applied for this event.");
                } else {
                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO APPLICATION (USER_ID, EVENT_ID, STATUS) VALUES (?, ?, 'Pending')");
                    ps.setInt(1, user.getUserId());
                    ps.setInt(2, eventId);
                    ps.executeUpdate();
                    request.getSession().setAttribute("msg", "Application submitted. Please wait for approval.");
                }
                response.sendRedirect("ApplicationServlet");

            } else if (action.equals("cancel") && !isAdmin) {
                // DELETE: volunteer cancels own application
                PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM APPLICATION WHERE APP_ID = ? AND USER_ID = ?");
                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                ps.setInt(2, user.getUserId());
                ps.executeUpdate();
                request.getSession().setAttribute("msg", "Application cancelled.");
                response.sendRedirect("ApplicationServlet");

            } else if ((action.equals("approve") || action.equals("reject")) && isAdmin) {
                // UPDATE: admin approves or rejects
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE APPLICATION SET STATUS = ? WHERE APP_ID = ?");
                ps.setString(1, action.equals("approve") ? "Approved" : "Rejected");
                ps.setInt(2, Integer.parseInt(request.getParameter("id")));
                ps.executeUpdate();
                response.sendRedirect("ApplicationServlet");

            } else {
                // READ: admin sees all, volunteer sees own
                String sql = "SELECT a.*, u.NAME, e.TITLE, e.EVENT_DATE FROM APPLICATION a "
                        + "JOIN USERS u ON a.USER_ID = u.USER_ID "
                        + "JOIN EVENT e ON a.EVENT_ID = e.EVENT_ID ";
                if (!isAdmin) {
                    sql += "WHERE a.USER_ID = " + user.getUserId() + " ";
                }
                sql += "ORDER BY a.APP_ID DESC";

                ArrayList<Application> list = new ArrayList<>();
                ResultSet rs = con.prepareStatement(sql).executeQuery();
                while (rs.next()) {
                    Application a = new Application();
                    a.setAppId(rs.getInt("APP_ID"));
                    a.setApplyDate(rs.getDate("APPLY_DATE"));
                    a.setStatus(rs.getString("STATUS"));
                    a.setVolunteerName(rs.getString("NAME"));
                    a.setEventTitle(rs.getString("TITLE"));
                    a.setEventDate(rs.getDate("EVENT_DATE"));
                    list.add(a);
                }
                request.setAttribute("applications", list);
                request.getRequestDispatcher("applicationList.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
