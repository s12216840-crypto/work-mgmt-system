CREATE TABLE tasks (
 id BIGSERIAL PRIMARY KEY,

 title VARCHAR(255) NOT NULL,
 description TEXT,
  project_id BIGINT NOT NULL,
assignee_id BIGINT,

 reporter_id BIGINT NOT NULL,

   status VARCHAR(50) NOT NULL,

  priority VARCHAR(50) NOT NULL,

due_date DATE,
created_at TIMESTAMP NOT NULL,

  updated_at TIMESTAMP NOT NULL,

 CONSTRAINT fk_tasks_project
                           FOREIGN KEY (project_id)
                               REFERENCES projects(id),                       CONSTRAINT fk_tasks_assignee
 FOREIGN KEY (assignee_id)
                               REFERENCES users(id),

CONSTRAINT fk_tasks_reporter
                           FOREIGN KEY (reporter_id)
                               REFERENCES users(id)
);