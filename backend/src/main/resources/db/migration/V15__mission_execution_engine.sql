-- Sprint-15: Mission Execution Engine
-- Creates all tables for the mission execution pipeline

CREATE TABLE IF NOT EXISTS missions (
    id UUID PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    user_id UUID NOT NULL,
    name VARCHAR(200) NOT NULL,
    keywords VARCHAR(200) NOT NULL,
    preferred_title VARCHAR(200),
    experience_level VARCHAR(50),
    location VARCHAR(200),
    remote BOOLEAN NOT NULL DEFAULT FALSE,
    hybrid BOOLEAN NOT NULL DEFAULT FALSE,
    salary_min INTEGER,
    currency VARCHAR(10),
    employment_type VARCHAR(30),
    platforms VARCHAR(500),
    resume_id UUID,
    schedule VARCHAR(50),
    timezone VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'CREATED'
);

CREATE INDEX IF NOT EXISTS idx_mission_user_id ON missions(user_id);
CREATE INDEX IF NOT EXISTS idx_mission_status ON missions(status);

CREATE TABLE IF NOT EXISTS mission_executions (
    id UUID PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    mission_id UUID NOT NULL REFERENCES missions(id),
    status VARCHAR(20) NOT NULL DEFAULT 'RUNNING',
    started_at TIMESTAMP WITH TIME ZONE NOT NULL,
    completed_at TIMESTAMP WITH TIME ZONE,
    duration_ms BIGINT,
    jobs_found INTEGER DEFAULT 0,
    contacts_found INTEGER DEFAULT 0,
    error_message VARCHAR(2000)
);

CREATE INDEX IF NOT EXISTS idx_execution_mission_id ON mission_executions(mission_id);
CREATE INDEX IF NOT EXISTS idx_execution_status ON mission_executions(status);

CREATE TABLE IF NOT EXISTS mission_events (
    id UUID PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    mission_id UUID NOT NULL REFERENCES missions(id),
    execution_id UUID,
    event_type VARCHAR(30) NOT NULL,
    message VARCHAR(500) NOT NULL,
    event_time TIMESTAMP WITH TIME ZONE NOT NULL,
    metadata TEXT
);

CREATE INDEX IF NOT EXISTS idx_event_mission_id ON mission_events(mission_id);
CREATE INDEX IF NOT EXISTS idx_event_execution_id ON mission_events(execution_id);
CREATE INDEX IF NOT EXISTS idx_event_type ON mission_events(event_type);

CREATE TABLE IF NOT EXISTS mission_execution_logs (
    id UUID PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    mission_id UUID NOT NULL REFERENCES missions(id),
    execution_id UUID NOT NULL,
    level VARCHAR(10) NOT NULL DEFAULT 'INFO',
    message VARCHAR(1000) NOT NULL,
    log_time TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_log_mission_id ON mission_execution_logs(mission_id);
CREATE INDEX IF NOT EXISTS idx_log_execution_id ON mission_execution_logs(execution_id);

CREATE TABLE IF NOT EXISTS discovered_jobs (
    id UUID PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    mission_id UUID NOT NULL REFERENCES missions(id),
    execution_id UUID,
    platform VARCHAR(20) NOT NULL,
    external_job_id VARCHAR(200),
    title VARCHAR(200) NOT NULL,
    company VARCHAR(200) NOT NULL,
    location VARCHAR(200),
    salary VARCHAR(100),
    description TEXT,
    job_url VARCHAR(2048),
    job_status VARCHAR(20) NOT NULL DEFAULT 'NEW'
);

CREATE INDEX IF NOT EXISTS idx_disc_job_mission_id ON discovered_jobs(mission_id);
CREATE INDEX IF NOT EXISTS idx_disc_job_platform ON discovered_jobs(platform);
CREATE INDEX IF NOT EXISTS idx_disc_job_external_id ON discovered_jobs(external_job_id);

CREATE TABLE IF NOT EXISTS mission_contacts (
    id UUID PRIMARY KEY,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    mission_id UUID NOT NULL REFERENCES missions(id),
    job_id UUID,
    execution_id UUID,
    name VARCHAR(200) NOT NULL,
    email VARCHAR(255),
    linkedin_url VARCHAR(500),
    role VARCHAR(100),
    confidence_score DOUBLE PRECISION,
    source VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN'
);

CREATE INDEX IF NOT EXISTS idx_contact_mission_id ON mission_contacts(mission_id);
CREATE INDEX IF NOT EXISTS idx_contact_job_id ON mission_contacts(job_id);
