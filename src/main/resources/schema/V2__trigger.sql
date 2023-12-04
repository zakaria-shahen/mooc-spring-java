alter table COURSE
    add cost number default 0 not null;

create or replace trigger bootcamp_average_cost_after_insert_update_delete
    after insert or update or delete
    on COURSE
    for each row
begin
    update BOOTCAMP
    set AVERAGE_COST = (select avg(c.COST) from COURSE c where c.BOOTCAMP_ID = BOOTCAMP_ID)
    where BOOTCAMP.ID = bootcamp_id;
end;


create or replace trigger bootcamp_rating_cost_after_insert_update_delete
    after insert or update or delete
    on REVIEW
    for each row
declare
    bootcamp_id number;
begin
    bootcamp_id := (select BOOTCAMP_ID from COURSE where COURSE.BOOTCAMP_ID = COURSE_ID);
    update BOOTCAMP
    set AVERAGE_RATING = (select avg(REVIEW.RATING)
                          from REVIEW
                                   join COURSE on COURSE.ID = COURSE_ID
                          where COURSE.BOOTCAMP_ID = BOOTCAMP_ID)
    where BOOTCAMP.ID = bootcamp_id;
end;
