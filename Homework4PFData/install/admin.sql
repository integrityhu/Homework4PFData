CREATE TABLE systemuser
(
  userid bigserial NOT NULL,
  casemanager boolean,
  emailaddress character varying(255),
  enabled boolean NOT NULL,
  pincode character varying(255),
  username character varying(255),
  userpassword character varying(255),
  CONSTRAINT systemuser_pkey PRIMARY KEY (userid)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE systemuser
  OWNER TO pzoli;

INSERT INTO systemuser (userid, emailAddress, enabled, userpassword, username) VALUES
(4, 'a@b.hu', true, 'jjXCzTv2ZBvbDiBQt2kyy7LmA0oN2swdm+qCprpX988=', 'terc'),
(10, 'c.d@exec.hu', true, 'jjXCzTv2ZBvbDiBQt2kyy7LmA0oN2swdm+qCprpX988=', 'porc');

CREATE TABLE IF NOT EXISTS user_join_group (
  user_name varchar(255) NOT NULL,
  group_name varchar(64) NOT NULL,
  PRIMARY KEY (user_name,group_name)
)WITH (
  OIDS=FALSE
);
ALTER TABLE user_join_group
  OWNER TO pzoli;


INSERT INTO user_join_group (user_name, group_name) VALUES
('a@b.hu', 'ROLE_USER'),
('c.d@exec.hu', 'ROLE_ADMIN');

CREATE VIEW v_active_user AS select u.emailAddress AS username,u.userpassword AS password from systemuser u where (u.enabled = true);
