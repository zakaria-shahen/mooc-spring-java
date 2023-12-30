create or replace trigger bootcamp_average_cost_after_insert_update_delete
    after insert or update or delete
    on COURSE
    for each row
begin
    update BOOTCAMP
    set AVERAGE_COST = coalesce((select avg(c.COST) from COURSE c where c.BOOTCAMP_ID = :new.BOOTCAMP_ID), 0)
    where BOOTCAMP.ID = :new.BOOTCAMP_ID;
end;


create or replace trigger bootcamp_rating_cost_after_insert_update_delete
    after insert or update or delete
    on REVIEW
    for each row
declare
    bootcamp_id number;
begin
    select COURSE.BOOTCAMP_ID into bootcamp_id from COURSE where COURSE.ID = :new.COURSE_ID;
    update BOOTCAMP
    set AVERAGE_RATING = coalesce(
            (select avg(REVIEW.RATING)
             from REVIEW
                      join COURSE on COURSE.ID = REVIEW.COURSE_ID
             where COURSE.BOOTCAMP_ID = bootcamp_id)
        , 0)
    where BOOTCAMP.ID = bootcamp_id;
end;
