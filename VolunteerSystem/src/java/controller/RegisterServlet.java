package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.DBConnection;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name").trim();
        String email = request.getParameter("email").trim();
        String phone = request.getParameter("phone").trim();
        String password = request.getParameter("password");
        String confirm = request.getParameter("confirm");

        // validation
        String error = null;
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            error = "All fields are required.";
        } else if (!email.matches("^[\\w.-]+@[\\w-]+(\\.[\\w-]+)+$")) {
            error = "Invalid email format.";
        } else if (!phone.matches("^01[0-9]-?[0-9]{7,8}$")) {
            error = "Invalid phone number. Example: 012-3456789";
        } else if (password.length() < 6) {
            error = "Password must be at least 6 characters.";
        } else if (!password.equals(confirm)) {
            error = "Password and confirm password do not match.";
        }

        try (Connection con = DBConnection.getConnection()) {
            if (error == null) {
                PreparedStatement check = con.prepareStatement("SELECT * FROM USERS WHERE EMAIL = ?");
                check.setString(1, email);
                ResultSet rs = check.executeQuery();
                if (rs.next()) {
                    error = "Email is already registered.";
                }
            }

            if (error != null) {
                request.setAttribute("error", error);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO USERS (NAME, EMAIL, PASSWORD, PHONE, ROLE) VALUES (?, ?, ?, ?, 'Volunteer')");
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, phone);
            ps.executeUpdate();

            request.setAttribute("success", "Registration successful. Please log in.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
