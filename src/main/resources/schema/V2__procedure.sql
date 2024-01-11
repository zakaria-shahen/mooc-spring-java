create or replace procedure update_bootcamp_average_cost(
    bootcamp_id_ number
) is
    new_average_cost number;
begin
    select NVL(avg(c.COST), 0) into new_average_cost from COURSE c where c.BOOTCAMP_ID = bootcamp_id_;
    update BOOTCAMP b
    set AVERAGE_COST = new_average_cost
    where b.ID = bootcamp_id_;
end;

create or replace procedure update_bootcamp_rating_review(
    course_id number
) is
    bootcamp_id number;
begin
    -- use `max` to handle null value (no data found exception) credit -> https://stackoverflow.com/a/24534777/21199737
    select max(COURSE.BOOTCAMP_ID) into bootcamp_id from COURSE where COURSE.ID = course_id;
    if bootcamp_id is null then
        return;
    end if;
    update BOOTCAMP
    set AVERAGE_RATING = (select NVL(avg(REVIEW.RATING), 0)
             from REVIEW join COURSE on COURSE.ID = REVIEW.COURSE_ID
             where COURSE.BOOTCAMP_ID = bootcamp_id)
    where BOOTCAMP.ID = bootcamp_id;
end;
