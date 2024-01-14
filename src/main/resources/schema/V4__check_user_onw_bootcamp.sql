create or replace function check_user_onw_bootcamp(
    bootcamp_id number,
    user_id_ number,
    is_admin number
) return number is
     is_owner number;
begin
    select count(*) into is_owner from BOOTCAMP b where b.USER_ID = user_id_ and b.id = bootcamp_id;
    if is_admin = 1 or is_owner = 1 then
        return 1;
    else
        raise_application_error(-20000, 'check_user_onw_bootcamp: invalid operation.');
        return 0;
    end if;
end check_user_onw_bootcamp;
