<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link rel="stylesheet" href="css/style.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    </head>
    <body>
        <%@include file="menu.jsp"%>
        <div class="content">
            <h2>Dashboard</h2>
            <p>Welcome, ${sessionScope.user.name}!</p>

            <div class="cards">
                <div class="card">Total Volunteers<h2>${totalVolunteers}</h2></div>
                <div class="card">Total Events<h2>${totalEvents}</h2></div>
                <div class="card">${sessionScope.user.role == 'Admin' ? 'Total Applications' : 'My Applications'}<h2>${totalApplications}</h2></div>
                <div class="card">Pending Applications<h2>${pending}</h2></div>
            </div>

            <div class="charts">
                <div class="chart">
                    <h3>Events by Category</h3>
                    <div style="height:280px;"><canvas id="categoryChart"></canvas></div>
                </div>
                <div class="chart">
                    <h3>Applications by Status</h3>
                    <div style="height:280px;"><canvas id="statusChart"></canvas></div>
                </div>
            </div>
        </div>

        <script>
            var catLabels = [<c:forEach var="x" items="${catLabels}">"${x}",</c:forEach>];
            var catValues = [<c:forEach var="x" items="${catValues}">${x},</c:forEach>];
            var statusLabels = [<c:forEach var="x" items="${statusLabels}">"${x}",</c:forEach>];
            var statusValues = [<c:forEach var="x" items="${statusValues}">${x},</c:forEach>];

            new Chart(document.getElementById("categoryChart"), {
                type: "bar",
                data: { labels: catLabels, datasets: [{ label: "Events", data: catValues, backgroundColor: "#0f766e" }] },
                options: { maintainAspectRatio: false, scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } } }
            });

            new Chart(document.getElementById("statusChart"), {
                type: "pie",
                data: { labels: statusLabels, datasets: [{ data: statusValues, backgroundColor: ["#16a34a", "#f59e0b", "#dc2626"] }] },
                options: { maintainAspectRatio: false }
            });
        </script>
    </body>
</html>
