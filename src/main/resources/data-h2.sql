INSERT INTO ROLE (CREATION_DATE, ROLE) VALUES (CURRENT_TIMESTAMP(), 'ADMIN');
INSERT INTO ROLE (CREATION_DATE, ROLE) VALUES (CURRENT_TIMESTAMP(), 'TEST');

INSERT INTO APP_USER (CREATION_DATE, FIRST_NAME, LAST_NAME, USERNAME, PASSWORD) VALUES (CURRENT_TIMESTAMP(), 'admin', 'admin', 'admin', '$2a$10$ylq6SoBpp/5455N3.YgveepJtb4zHa4k6H/zRoeOHWXX0bpxID3gC');

INSERT INTO USERS_ROLES (USER_ID, ROLE_ID) VALUES (1, 1);

INSERT INTO ACCOUNT (CREATION_DATE, IBAN, DESCRIPTION, CURRENCY, USER_ID) VALUES (CURRENT_TIMESTAMP(), 'ES24 9793 2718 0376 1154 8947', 'Test account', 'EUR', 1);

INSERT INTO ACCOUNT_BALANCE (BALANCE, DATE, ACCOUNT_ID) VALUES (0, CURRENT_TIMESTAMP(), 1);

INSERT INTO OPERATION_CATEGORY (NAME, DESCRIPTION, ICON, CREATION_DATE) VALUES
    ('Savings', 'Savings', 'money bill alternate outline', CURRENT_TIMESTAMP()),
    ('Home', 'Home', 'home', CURRENT_TIMESTAMP()),
    ('Transport', 'Transport', 'car', CURRENT_TIMESTAMP()),
    ('Bike stuff', 'Bike stuff', 'bicycle', CURRENT_TIMESTAMP()),
    ('Restaurant', 'Restaurant', 'utensils', CURRENT_TIMESTAMP());