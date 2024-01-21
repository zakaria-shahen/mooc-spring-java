--
---- Tables: bootcamp, career, bootcamp_career, bootcamp_photo, course, review, user, config
---- Repos:  BootcampRepo, CareerRepo, CourseRepo, ReviewRepo, UserRepo
CREATE TABLE USER_
(
    id             NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    name           VARCHAR2(25) NOT NULL,
    email          VARCHAR2(50) NOT NULL,
    password       VARCHAR2(70) NOT NULL,
    status         NUMBER(1) DEFAULT 1,
    login_attempts NUMBER(1) DEFAULT 0,
    role           VARCHAR2(15) CHECK (role IN ('ADMIN', 'PUBLISHER', 'USER')),
    CONSTRAINT user_email_unique UNIQUE (email),
    CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE BOOTCAMP
(
    id             NUMBER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name           VARCHAR2(50)                            NOT NULL,
    description    VARCHAR2(255)                           NOT NULL,
    website        VARCHAR2(50)                            NOT NULL,
    phone          VARCHAR2(20)                            NOT NULL,
    email          VARCHAR2(50)                            NOT NULL,
    address        VARCHAR2(100)                           NOT NULL,
    housing        NUMBER(1)                               NOT NULL,
    job_assistance NUMBER(1),
    job_guarantee  NUMBER(1),
    average_cost   NUMBER, -- of courses
    average_rating NUMBER, -- of reviews
    user_id        NUMBER,
    CONSTRAINT bootcamp_pk PRIMARY KEY (id),
    CONSTRAINT bootcamp_user_fk FOREIGN KEY (user_id) REFERENCES user_ (id)
);

CREATE TABLE CAREER
(
    id   NUMBER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR2(50)                            NOT NULL,
    CONSTRAINT career_name_unique UNIQUE (name),
    CONSTRAINT career_pk PRIMARY KEY (id)
);

CREATE TABLE BOOTCAMP_CAREER
(
    career_id   NOT NULL
        CONSTRAINT bc_career_fk REFERENCES CAREER (id),
    bootcamp_id NOT NULL
        CONSTRAINT bc_bootcamp_fk REFERENCES BOOTCAMP (id),
    CONSTRAINT bootcamp_career_pk PRIMARY KEY (
                                               bootcamp_id,
                                               career_id
        )
);

CREATE TABLE BOOTCAMP_PHOTO
(
    id         NUMBER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    photo_path VARCHAR2(255)                           NOT NULL,
    bootcamp_id                                        NOT NULL
        CONSTRAINT bp_bootcamp_fk REFERENCES BOOTCAMP (id),
    CONSTRAINT bootcamp_photo_unique UNIQUE (
                                             photo_path,
                                             bootcamp_id
        ),
    CONSTRAINT bootcamp_photo_pk PRIMARY KEY (id)
);

CREATE TABLE COURSE
(
    id            NUMBER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title         VARCHAR2(50)                            NOT NULL,
    description   VARCHAR2(255)                           NOT NULL,
    weeks         NUMBER                                  NOT NULL,
    tuition       NUMBER                                  NOT NULL,
    minimum_skill VARCHAR2(15) CHECK (
        minimum_skill IN (
                          'BEGINNER',
                          'INTERMEDIATE',
                          'ADVANCED'
            )
        ),
    cost          NUMBER                                  NOT NULL,
    bootcamp_id                                           NOT NULL
        CONSTRAINT course_bootcamp_fk REFERENCES BOOTCAMP (id),
    user_id                                               NOT NULL
        CONSTRAINT course_user_fk REFERENCES USER_ (id),
    CONSTRAINT course_user_unique UNIQUE (user_id, title), /* Why? */
    CONSTRAINT course_pk PRIMARY KEY (id)
);


CREATE TABLE REVIEW
(
    id     NUMBER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR2(50)                            NOT NULL,
    text   VARCHAR2(255)                           NOT NULL,
    rating NUMBER                                  NOT NULL CHECK (
        rating BETWEEN 0
            AND 5
        ),
    course_id                                      NOT NULL
        CONSTRAINT review_course_fk REFERENCES COURSE (id),
    user_id                                        NOT NULL
        CONSTRAINT review_user_fk REFERENCES USER_ (id),
    CONSTRAINT review_user_unique UNIQUE (user_id, course_id),
    CONSTRAINT review_pk PRIMARY KEY (id)
);


-- application level config (maybe replaced by parameters in properties file!)
CREATE TABLE CONFIG
(
    id    NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,
    param varchar2(30) NOT NULL,
    val   varchar2(15) NOT NULL,
    CONSTRAINT config_unique UNIQUE (param),
    CONSTRAINT config_pk PRIMARY KEY (id)
);
