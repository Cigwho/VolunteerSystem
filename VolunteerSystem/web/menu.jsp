<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="navbar">
    <a href="DashboardServlet">Dashboard</a>
    <a href="EventServlet">Events</a>
    <a href="ApplicationServlet">${sessionScope.user.role == 'Admin' ? 'Applications' : 'My Applications'}</a>
    <a href="LogoutServlet" onclick="return confirm('Logout?');">Logout</a>
    <span>${sessionScope.user.name} (${sessionScope.user.role})</span>
</div>
<c:if test="${not empty sessionScope.msg}">
    <div class="content"><p class="success">${sessionScope.msg}</p></div>
    <c:remove var="msg" scope="session"/>
</c:if>
