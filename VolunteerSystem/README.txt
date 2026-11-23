VOLUNTEER MANAGEMENT SYSTEM - CSC584
NetBeans 8.2 + GlassFish 4.1.1 + Java DB

HOW TO RUN
1. NetBeans: File > Open Project > select the VolunteerSystem folder.
2. Services > Databases > Java DB > Start Server.
3. Java DB > Create Database: name VolunteerDB, user app, password app.
4. Right-click the VolunteerDB connection > Connect > Execute Command.
   Paste everything from database/VolunteerDB.sql and click Run SQL.
5. Right-click the project > Run.

LOGIN
Admin     : admin@gmail.com  / admin123
Volunteer : aisyah@gmail.com / aisyah123

MVC
Model      : src/java/model (User, Category, Event, Application)
View       : web/*.jsp
Controller : src/java/controller (Login, Logout, Register, Dashboard, Event, Application servlets)
Database   : src/java/util/DBConnection.java
