create or replace trigger update_bootcamp_average_cost
    for insert or update or delete
    on COURSE
    compound trigger
        bootcamp_id number;
    after each row is
    begin
        bootcamp_id := NVL(:OLD.BOOTCAMP_ID, :new.BOOTCAMP_ID);
    end after each row ;

    after statement is
    begin
        update BOOTCAMP b
        set AVERAGE_COST = (select NVL(avg(c.COST), 0) from COURSE c where c.BOOTCAMP_ID = bootcamp_id)
        where b.ID = bootcamp_id;
    end after statement ;
end update_bootcamp_average_cost;

/

create or replace trigger update_bootcamp_rating_review
    for insert or update or delete
    on REVIEW
    COMPOUND TRIGGER
        bootcamp_id number;
        course_id number;
    after each row is
    begin
        course_id := NVL(:new.COURSE_ID, :OLD.COURSE_ID);
        select COURSE.BOOTCAMP_ID into bootcamp_id from COURSE where COURSE.ID = course_id;
    end after each row;

    after statement is
    begin
        update BOOTCAMP
        set AVERAGE_RATING = coalesce(
                (select avg(REVIEW.RATING)
                 from REVIEW
                          join COURSE on COURSE.ID = REVIEW.COURSE_ID
                 where COURSE.BOOTCAMP_ID = bootcamp_id)
            , 0)
        where BOOTCAMP.ID = bootcamp_id;
    end after statement;
end update_bootcamp_rating_review;