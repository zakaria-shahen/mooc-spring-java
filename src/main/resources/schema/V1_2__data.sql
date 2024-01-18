insert into user_(name, email, password, role) values ('admin', 'admin@admin', '{bcrypt}$2a$10$4vPtlu/7ULWOIKhzJERDKul2EnzN1ziqezUXSERkXh86izeKvND7K', 'ROLE_ADMIN');
insert into CAREER(name) values ('Java');

INSERT INTO BOOTCAMP (NAME, DESCRIPTION, WEBSITE, PHONE, EMAIL, ADDRESS, HOUSING, JOB_ASSISTANCE,
                      JOB_GUARANTEE, AVERAGE_COST, AVERAGE_RATING, USER_ID)
VALUES ('testing', 'testing', 'testing', 'testing', 'testing@test.com', 'testing', 1, 1, 1, 1, null, 1);

insert into BOOTCAMP_CAREER(career_id, bootcamp_id) values (1, 1);
insert into COURSE(title, description, weeks, tuition, minimum_skill, cost, bootcamp_id, user_id)
VALUES ('kotlin', 'learn kotlin as back-end/java developer', 8, 1, 'BEGINNER', 200, 1, 1);
insert into REVIEW(title, text, rating, course_id, user_id) VALUES ('very good', 'very good', 5, 1, 1);