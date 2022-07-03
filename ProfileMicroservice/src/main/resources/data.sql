INSERT INTO users (ID, BIOGRAPHY, DATE_OF_BIRTH, EMAIL, FIRST_NAME, GENDER, LAST_NAME, PHONE_NUMBER, USERNAME, PUBLIC_PROFILE)
VALUES ('d12602fd-b7af-4da1-b1ca-bad8166d1fb3', 'aaa', '05-05-2005', 'a@a.com', 'aca', 'MALE', 'aca', '+32242342', 'peraperic', true);
INSERT INTO users (ID, BIOGRAPHY, DATE_OF_BIRTH, EMAIL, FIRST_NAME, GENDER, LAST_NAME, PHONE_NUMBER, USERNAME, PUBLIC_PROFILE)
VALUES ('d12602fd-b7af-4da1-b1ca-bad8166d1fb2', 'aaa', '05/05/2005', 'jankovicapoteka@gmail.com', 'Aca', 'MALE', 'Aca', '+32242342', 'username1', true);
INSERT INTO users (ID, BIOGRAPHY, DATE_OF_BIRTH, EMAIL, FIRST_NAME, GENDER, LAST_NAME, PHONE_NUMBER, USERNAME, PUBLIC_PROFILE)
VALUES ('d12602fd-b7af-4da1-b1ca-bad8166d1fb4', 'Frontend Engineer', '05/05/1986', 'Nikola@gmail.com', 'Nikola', 'MALE', 'Nikola', '+322323423', 'username3', true);

INSERT INTO interest (ID, DESCRIPTION)
VALUES (101, 'Swimming');

INSERT INTO users_interests (USER_ID, INTERESTS_ID)
VALUES ('d12602fd-b7af-4da1-b1ca-bad8166d1fb4', 101)
