--Database creation
DO {
DROP TABLE IF EXISTS users, tasks, comments CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id  BIGSERIAL ,
    names  VARCHAR(200) NOT NULL ,
    email VARCHAR(254) NOT NULL ,
    password VARCHAR(512)  NOT NULL ,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS tasks (
    id BIGSERIAL ,
    title VARCHAR(254) NOT NULL ,
    description VARCHAR(512) ,
    status_name VARCHAR(20) NOT NULL,
    priority VARCHAR(20) NOT NULL ,
    creater_id BIGINT NOT NULL ,
    executor_id BIGINT NOT NULL,
    CONSTRAINT pk_tasks PRIMARY KEY (id),
    FOREIGN KEY (creater_id) REFERENCES users (id) ,
    FOREIGN KEY (executor_id) REFERENCES users (id)
)

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL ,
    content VARCHAR(512) NOT NULL ,
    creater_id BIGINT NOT NULL ,
    task_id BIGINT NOT NULL ,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    FOREIGN KEY (creater_id) REFERENCES users (id) ,
    FOREIGN KEY (task_id) REFERENCES tasks (id)
)
}