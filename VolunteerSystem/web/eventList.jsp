<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Events</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <%@include file="menu.jsp"%>
        <div class="content">
            <h2>Volunteer Events</h2>

            <c:if test="${sessionScope.user.role == 'Admin'}">
                <p><a href="EventServlet?action=add" class="btn">+ Add Event</a></p>
            </c:if>

            <table>
                <tr>
                    <th>No</th>
                    <th>Title</th>
                    <th>Category</th>
                    <th>Location</th>
                    <th>Date</th>
                    <th>Volunteers</th>
                    <th>Description</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="e" items="${events}" varStatus="i">
                    <tr>
                        <td>${i.count}</td>
                        <td>${e.title}</td>
                        <td>${e.categoryName}</td>
                        <td>${e.location}</td>
                        <td>${e.eventDate}</td>
                        <td>${e.joined} / ${e.slots}</td>
                        <td>${e.description}</td>
                        <td>
                            <c:choose>
                                <c:when test="${sessionScope.user.role == 'Admin'}">
                                    <a href="EventServlet?action=edit&id=${e.eventId}" class="btn">Edit</a>
                                    <a href="EventServlet?action=delete&id=${e.eventId}" class="btn btn-red"
                                       onclick="return confirm('Delete this event?');">Delete</a>
                                </c:when>
                                <c:when test="${e.joined >= e.slots}">
                                    Full
                                </c:when>
                                <c:otherwise>
                                    <a href="ApplicationServlet?action=apply&eventId=${e.eventId}" class="btn"
                                       onclick="return confirm('Apply for this event?');">Apply</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </body>
</html>
