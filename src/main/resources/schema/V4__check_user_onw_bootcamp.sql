create or replace function check_user_onw_bootcamp(
    bootcamp_id number,
    user_id_ number,
    is_admin number
) return number is
    is_owner number;
    zero_rows number;
begin
    select count(*) into is_owner from BOOTCAMP b where b.USER_ID = user_id_ and b.id = bootcamp_id;
    if  is_owner = 1 then
        return 1;
    elsif is_admin = 1 then
        select count(*) into is_owner from BOOTCAMP b where b.id = bootcamp_id;
        if zero_rows != 1 then
            return  1;
        end if;
    end if;
    raise_application_error(-20000, 'check_user_onw_bootcamp: invalid operation.');
    return 0;
end check_user_onw_bootcamp;
