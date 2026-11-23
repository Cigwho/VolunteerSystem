package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Category;
import model.Event;
import model.User;
import util.DBConnection;

@WebServlet("/EventServlet")
public class EventServlet extends HttpServlet {

    // READ (list), show ADD / EDIT form, DELETE
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

            if (action.equals("delete") && isAdmin) {
                PreparedStatement ps = con.prepareStatement("DELETE FROM EVENT WHERE EVENT_ID = ?");
                ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                ps.executeUpdate();
                response.sendRedirect("EventServlet");

            } else if ((action.equals("add") || action.equals("edit")) && isAdmin) {
                // categories for the drop-down list
                ArrayList<Category> categories = new ArrayList<>();
                ResultSet rs = con.prepareStatement("SELECT * FROM CATEGORY").executeQuery();
                while (rs.next()) {
                    Category c = new Category();
                    c.setCategoryId(rs.getInt("CATEGORY_ID"));
                    c.setCategoryName(rs.getString("CATEGORY_NAME"));
                    categories.add(c);
                }
                request.setAttribute("categories", categories);

                if (action.equals("edit")) {
                    PreparedStatement ps = con.prepareStatement("SELECT * FROM EVENT WHERE EVENT_ID = ?");
                    ps.setInt(1, Integer.parseInt(request.getParameter("id")));
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        Event e = new Event();
                        e.setEventId(rs.getInt("EVENT_ID"));
                        e.setTitle(rs.getString("TITLE"));
                        e.setCategoryId(rs.getInt("CATEGORY_ID"));
                        e.setLocation(rs.getString("LOCATION"));
                        e.setEventDate(rs.getDate("EVENT_DATE"));
                        e.setSlots(rs.getInt("SLOTS"));
                        e.setDescription(rs.getString("DESCRIPTION"));
                        request.setAttribute("event", e);
                    }
                }
                request.getRequestDispatcher("eventForm.jsp").forward(request, response);

            } else {
                ArrayList<Event> events = new ArrayList<>();
                ResultSet rs = con.prepareStatement(
                        "SELECT e.*, c.CATEGORY_NAME, "
                        + "(SELECT COUNT(*) FROM APPLICATION a WHERE a.EVENT_ID = e.EVENT_ID AND a.STATUS = 'Approved') AS JOINED "
                        + "FROM EVENT e JOIN CATEGORY c ON e.CATEGORY_ID = c.CATEGORY_ID "
                        + "ORDER BY e.EVENT_DATE").executeQuery();
                while (rs.next()) {
                    Event e = new Event();
                    e.setEventId(rs.getInt("EVENT_ID"));
                    e.setTitle(rs.getString("TITLE"));
                    e.setCategoryName(rs.getString("CATEGORY_NAME"));
                    e.setLocation(rs.getString("LOCATION"));
                    e.setEventDate(rs.getDate("EVENT_DATE"));
                    e.setSlots(rs.getInt("SLOTS"));
                    e.setDescription(rs.getString("DESCRIPTION"));
                    e.setJoined(rs.getInt("JOINED"));
                    events.add(e);
                }
                request.setAttribute("events", events);
                request.getRequestDispatcher("eventList.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    // CREATE and UPDATE
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null || !user.getRole().equals("Admin")) {
            response.sendRedirect("login.jsp");
            return;
        }

        String eventId = request.getParameter("eventId");
        String title = request.getParameter("title");
        String categoryId = request.getParameter("categoryId");
        String location = request.getParameter("location");
        String eventDate = request.getParameter("eventDate");
        String slots = request.getParameter("slots");
        String description = request.getParameter("description");

        try (Connection con = DBConnection.getConnection()) {
            PreparedStatement ps;
            if (eventId == null || eventId.isEmpty()) {
                ps = con.prepareStatement("INSERT INTO EVENT (TITLE, CATEGORY_ID, LOCATION, EVENT_DATE, SLOTS, DESCRIPTION) "
                        + "VALUES (?, ?, ?, ?, ?, ?)");
            } else {
                ps = con.prepareStatement("UPDATE EVENT SET TITLE = ?, CATEGORY_ID = ?, LOCATION = ?, "
                        + "EVENT_DATE = ?, SLOTS = ?, DESCRIPTION = ? WHERE EVENT_ID = ?");
                ps.setInt(7, Integer.parseInt(eventId));
            }
            ps.setString(1, title);
            ps.setInt(2, Integer.parseInt(categoryId));
            ps.setString(3, location);
            ps.setDate(4, Date.valueOf(eventDate));
            ps.setInt(5, Integer.parseInt(slots));
            ps.setString(6, description);
            ps.executeUpdate();

            response.sendRedirect("EventServlet");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
