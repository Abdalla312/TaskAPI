-- V3__align_tasks_schema_with_jpa.sql
-- Fix task table types to match JPA mappings used by Task entity.

-- BIGSERIAL on a foreign key is incorrect; keep it as plain BIGINT.
ALTER TABLE tasks
    ALTER COLUMN user_id TYPE BIGINT,
    ALTER COLUMN user_id DROP DEFAULT;

-- JPA @Enumerated(EnumType.STRING) writes VARCHAR values like 'TODO'.
ALTER TABLE tasks
    ALTER COLUMN status TYPE VARCHAR(20) USING status::text;

-- Keep values constrained after switching away from postgres enum.
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'chk_tasks_status'
    ) THEN
        ALTER TABLE tasks
            ADD CONSTRAINT chk_tasks_status
            CHECK (status IN ('TODO', 'IN_PROGRESS', 'DONE'));
    END IF;
END $$;

-- Drop enum type only if nothing uses it anymore.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM pg_type
        WHERE typname = 'taskstatus'
    ) THEN
        DROP TYPE taskstatus;
    END IF;
END $$;

