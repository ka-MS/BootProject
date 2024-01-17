DROP TABLE IF EXISTS post;

CREATE TABLE post (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      uuid VARCHAR(36) NOT NULL,
                      title VARCHAR(200),
                      content VARCHAR(1000),
                      created_at TIMESTAMP,
                      updated_at TIMESTAMP,
                      deleted_at TIMESTAMP,
                      CONSTRAINT uuid_unique UNIQUE (uuid)
);