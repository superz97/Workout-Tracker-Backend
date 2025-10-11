CREATE TABLE IF NOT EXISTS workout_plans (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    scheduled_date DATE,
    scheduled_time TIME,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    duration_minutes INTEGER,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP
);

CREATE INDEX idx_workout_plans_user_id ON workout_plans(user_id);
CREATE INDEX idx_workout_plans_user_date ON workout_plans(user_id, scheduled_date);
CREATE INDEX idx_workout_plans_status ON workout_plans(status);

CREATE TABLE IF NOT EXISTS workout_exercises (
    id BIGSERIAL PRIMARY KEY,
    workout_plan_id BIGINT NOT NULL REFERENCES workout_plans(id) ON DELETE CASCADE,
    exercise_id BIGINT NOT NULL REFERENCES exercises(id),
    sets INTEGER NOT NULL,
    reps INTEGER NOT NULL,
    weight DECIMAL(6,2),
    rest_seconds INTEGER,
    order_index INTEGER,
    notes TEXT
);

CREATE INDEX idx_workout_exercises_workout_plan ON workout_exercises(workout_plan_id);
CREATE INDEX idx_workout_exercises_order ON workout_exercises(workout_plan_id, order_index);

CREATE TABLE IF NOT EXISTS workout_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    workout_plan_id BIGINT REFERENCES workout_plans(id),
    workout_exercise_id BIGINT REFERENCES workout_exercises(id),
    exercise_id BIGINT REFERENCES exercises(id),
    actual_sets INTEGER,
    actual_reps INTEGER,
    actual_weight DECIMAL(6,2),
    logged_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

CREATE INDEX idx_workout_logs_user_id ON workout_logs(user_id);
CREATE INDEX idx_workout_logs_user_date ON workout_logs(user_id, logged_at);

CREATE TABLE IF NOT EXISTS user_progress (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    exercise_id BIGINT NOT NULL REFERENCES exercises(id),
    max_weight DECIMAL(6,2),
    max_reps INTEGER,
    total_volume DECIMAL(10,2),
    last_performed TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, exercise_id)
);

CREATE INDEX idx_user_progress_user_exercise ON user_progress(user_id, exercise_id);