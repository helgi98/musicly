CREATE SEQUENCE IF NOT EXISTS musicly_streaming.user_id_seq;

CREATE TABLE IF NOT EXISTS musicly_streaming.APPLICATION_USER
(
  ID       NUMERIC PRIMARY KEY DEFAULT nextval('musicly_streaming.user_id_seq'),
  USERNAME VARCHAR(255) NOT NULL UNIQUE,
  EMAIL    VARCHAR(255) NOT NULL UNIQUE,
  PASSWORD VARCHAR(255) NOT NULL
);

CREATE SEQUENCE IF NOT EXISTS musicly_streaming.role_id_seq;
CREATE TABLE IF NOT EXISTS musicly_streaming.ROLE
(
  ID   NUMERIC PRIMARY KEY DEFAULT nextval('musicly_streaming.role_id_seq'),
  ROLE VARCHAR(255) UNIQUE
);

CREATE TABLE IF NOT EXISTS musicly_streaming.USER_ROLE
(
  USER_ID NUMERIC REFERENCES APPLICATION_USER (ID),
  ROLE_ID NUMERIC REFERENCES ROLE (ID)
);

INSERT INTO musicly_streaming.role (role)
VALUES ('APP');
INSERT INTO musicly_streaming.role (role)
VALUES ('ADMIN');