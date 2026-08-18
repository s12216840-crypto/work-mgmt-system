CREATE TABLE projects (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          organization_id BIGINT NOT NULL,
                          owner_id BIGINT NOT NULL,
                          status VARCHAR(50) NOT NULL,
                          start_date DATE,
                          end_date DATE,

                          CONSTRAINT fk_project_organization
                              FOREIGN KEY (organization_id)
                                  REFERENCES organizations(id),

                          CONSTRAINT fk_project_owner
                              FOREIGN KEY (owner_id)
                                  REFERENCES users(id)
);

CREATE TABLE project_members (
                                 project_id BIGINT NOT NULL,
                                 user_id BIGINT NOT NULL,

                                 PRIMARY KEY (project_id, user_id),

                                 CONSTRAINT fk_project_members_project
                                     FOREIGN KEY (project_id)
                                         REFERENCES projects(id)
                                         ON DELETE CASCADE,

                                 CONSTRAINT fk_project_members_user
                                     FOREIGN KEY (user_id)
                                         REFERENCES users(id)
                                         ON DELETE CASCADE
);