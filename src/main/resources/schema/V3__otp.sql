create table OTP(
    id    number generated by default as identity not null,
    receiver VARCHAR2(50) not null,
    otp   varchar(70)   not null,
    attempts number default 0 not null,
    used  number(1) default 0 not null,
    type varchar(20) default 'reset-credentials' not null,
    create_at number not null,
    expiration_after_seconds number default 600 not null
);