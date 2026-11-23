<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Event Form</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <%@include file="menu.jsp"%>
        <div class="content">
            <h2>${empty event ? 'Add Event' : 'Edit Event'}</h2>

            <form action="EventServlet" method="post" class="form">
                <input type="hidden" name="eventId" value="${event.eventId}">

                Title:
                <input type="text" name="title" value="${event.title}" required>

                Category:
                <select name="categoryId" required>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat.categoryId}" ${cat.categoryId == event.categoryId ? 'selected' : ''}>
                            ${cat.categoryName}
                        </option>
                    </c:forEach>
                </select>

                Location:
                <input type="text" name="location" value="${event.location}" required>

                Date:
                <input type="date" name="eventDate" value="${event.eventDate}" required>

                Number of Volunteers Needed:
                <input type="number" name="slots" value="${event.slots}" min="1" required>

                Description:
                <textarea name="description" rows="4">${event.description}</textarea>

                <input type="submit" value="Save" class="btn">
                <a href="EventServlet">Cancel</a>
            </form>
        </div>
    </body>
</html>
