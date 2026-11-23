<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Applications</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <%@include file="menu.jsp"%>
        <div class="content">
            <h2>${sessionScope.user.role == 'Admin' ? 'Volunteer Applications' : 'My Applications'}</h2>

            <table>
                <tr>
                    <th>No</th>
                    <th>Volunteer</th>
                    <th>Event</th>
                    <th>Event Date</th>
                    <th>Applied On</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
                <c:forEach var="a" items="${applications}" varStatus="i">
                    <tr>
                        <td>${i.count}</td>
                        <td>${a.volunteerName}</td>
                        <td>${a.eventTitle}</td>
                        <td>${a.eventDate}</td>
                        <td>${a.applyDate}</td>
                        <td>${a.status}</td>
                        <td>
                            <c:choose>
                                <c:when test="${sessionScope.user.role == 'Admin'}">
                                    <a href="ApplicationServlet?action=approve&id=${a.appId}" class="btn">Approve</a>
                                    <a href="ApplicationServlet?action=reject&id=${a.appId}" class="btn btn-orange">Reject</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="ApplicationServlet?action=cancel&id=${a.appId}" class="btn btn-red"
                                       onclick="return confirm('Cancel this application?');">Cancel</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <c:if test="${empty applications}">
                <p>No applications yet.</p>
            </c:if>
        </div>
    </body>
</html>
