CREATE TABLE organizations (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               description TEXT
);

CREATE TABLE teams (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       description TEXT,
                       organization_id BIGINT NOT NULL,

                       CONSTRAINT fk_team_organization
                           FOREIGN KEY (organization_id)
                               REFERENCES organizations(id)
);

CREATE TABLE team_users (
                            team_id BIGINT NOT NULL,
                            user_id BIGINT NOT NULL,

                            PRIMARY KEY (team_id, user_id),

                            CONSTRAINT fk_team_users_team
                                FOREIGN KEY (team_id)
                                    REFERENCES teams(id)
                                    ON DELETE CASCADE,

                            CONSTRAINT fk_team_users_user
                                FOREIGN KEY (user_id)
                                    REFERENCES users(id)
                                    ON DELETE CASCADE
);