<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <div class="box">
            <h2>Volunteer Management System</h2>
            <h3>Login</h3>

            <% if (request.getAttribute("error") != null) { %>
                <p class="error"><%= request.getAttribute("error") %></p>
            <% } %>
            <% if (request.getAttribute("success") != null) { %>
                <p class="success"><%= request.getAttribute("success") %></p>
            <% } %>

            <form action="LoginServlet" method="post">
                Email:
                <input type="email" name="email" required>
                Password:
                <input type="password" name="password" required>
                <input type="submit" value="Login" class="btn">
            </form>
            <p>No account? <a href="register.jsp">Register here</a></p>
        </div>
    </body>
</html>
