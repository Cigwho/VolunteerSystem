<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Register</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <div class="box">
            <h2>Volunteer Registration</h2>

            <% if (request.getAttribute("error") != null) { %>
                <p class="error"><%= request.getAttribute("error") %></p>
            <% } %>

            <form action="RegisterServlet" method="post">
                Full Name:
                <input type="text" name="name" value="${param.name}" required>
                Email:
                <input type="email" name="email" value="${param.email}" required>
                Phone (e.g. 012-3456789):
                <input type="text" name="phone" value="${param.phone}" required>
                Password (min 6 characters):
                <input type="password" name="password" minlength="6" required>
                Confirm Password:
                <input type="password" name="confirm" required>
                <input type="submit" value="Register" class="btn">
            </form>
            <p>Already registered? <a href="login.jsp">Login here</a></p>
        </div>
    </body>
</html>
