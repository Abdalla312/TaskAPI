-- V2__Create_tasks_table.sql
CREATE TYPE TaskStatus AS ENUM('TODO','IN_PROGRESS', 'DONE');
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL REFERENCES users(id),
    title VARCHAR(50),
    description TEXT NOT NULL,
    status TaskStatus,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_task_status on tasks(status);

