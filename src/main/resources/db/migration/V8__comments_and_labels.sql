CREATE TABLE comments (
                          id BIGSERIAL PRIMARY KEY,
                          content TEXT NOT NULL,
                          task_id BIGINT NOT NULL,
                          user_id BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,

                          CONSTRAINT fk_comments_task
                              FOREIGN KEY (task_id)
                                  REFERENCES tasks(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_comments_user
                              FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);

CREATE TABLE labels (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE task_labels (
                             task_id BIGINT NOT NULL,
                             label_id BIGINT NOT NULL,

                             PRIMARY KEY (task_id, label_id),

                             CONSTRAINT fk_task_labels_task
                                 FOREIGN KEY (task_id)
                                     REFERENCES tasks(id)
                                     ON DELETE CASCADE,

                             CONSTRAINT fk_task_labels_label
                                 FOREIGN KEY (label_id)
                                     REFERENCES labels(id)
                                     ON DELETE CASCADE
);