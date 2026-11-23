-- Volunteer Management System (VolunteerDB, user: app, password: app)
-- If tables already exist, run these 4 lines first:
-- DROP TABLE APPLICATION;
-- DROP TABLE EVENT;
-- DROP TABLE CATEGORY;
-- DROP TABLE USERS;

CREATE TABLE USERS (
    USER_ID  INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    NAME     VARCHAR(100) NOT NULL,
    EMAIL    VARCHAR(100) NOT NULL UNIQUE,
    PASSWORD VARCHAR(50)  NOT NULL,
    PHONE    VARCHAR(15)  NOT NULL,
    ROLE     VARCHAR(10)  NOT NULL,
    PRIMARY KEY (USER_ID)
);

CREATE TABLE CATEGORY (
    CATEGORY_ID   INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    CATEGORY_NAME VARCHAR(50) NOT NULL,
    PRIMARY KEY (CATEGORY_ID)
);

CREATE TABLE EVENT (
    EVENT_ID    INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    TITLE       VARCHAR(100) NOT NULL,
    CATEGORY_ID INTEGER NOT NULL,
    LOCATION    VARCHAR(150) NOT NULL,
    EVENT_DATE  DATE NOT NULL,
    SLOTS       INTEGER NOT NULL,
    DESCRIPTION VARCHAR(500),
    PRIMARY KEY (EVENT_ID),
    FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY (CATEGORY_ID)
);

CREATE TABLE APPLICATION (
    APP_ID     INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    USER_ID    INTEGER NOT NULL,
    EVENT_ID   INTEGER NOT NULL,
    APPLY_DATE DATE DEFAULT CURRENT_DATE,
    STATUS     VARCHAR(10) NOT NULL,
    PRIMARY KEY (APP_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS (USER_ID) ON DELETE CASCADE,
    FOREIGN KEY (EVENT_ID) REFERENCES EVENT (EVENT_ID) ON DELETE CASCADE
);

INSERT INTO USERS (NAME, EMAIL, PASSWORD, PHONE, ROLE) VALUES
('Admin', 'admin@gmail.com', 'admin123', '012-3456789', 'Admin'),
('Nur Aisyah', 'aisyah@gmail.com', 'aisyah123', '013-2345678', 'Volunteer'),
('Muhammad Haziq', 'haziq@gmail.com', 'haziq123', '011-2345678', 'Volunteer'),
('Tan Wei Ling', 'weiling@gmail.com', 'weiling123', '016-7788990', 'Volunteer');

INSERT INTO CATEGORY (CATEGORY_NAME) VALUES
('Environment'), ('Education'), ('Community'), ('Health');

INSERT INTO EVENT (TITLE, CATEGORY_ID, LOCATION, EVENT_DATE, SLOTS, DESCRIPTION) VALUES
('Beach Clean-up', 1, 'Pantai Morib, Banting', '2027-01-16', 30, 'Collect rubbish along the beach.'),
('Weekend Tuition', 2, 'SK Seksyen 9, Shah Alam', '2027-01-23', 10, 'Teach Maths and English to primary pupils.'),
('Food Bank Packing', 3, 'Dewan Seksyen 7, Shah Alam', '2027-02-06', 20, 'Pack food boxes for B40 families.'),
('Blood Donation Drive', 4, 'Dataran UiTM Shah Alam', '2027-02-20', 15, 'Help with donor registration.'),
('Tree Planting', 1, 'Taman Botani Shah Alam', '2027-03-06', 40, 'Plant tree seedlings.');

INSERT INTO APPLICATION (USER_ID, EVENT_ID, STATUS) VALUES
(2, 1, 'Approved'),
(3, 1, 'Approved'),
(4, 2, 'Pending'),
(2, 3, 'Pending'),
(3, 4, 'Rejected');
