CREATE TABLE IF NOT EXISTS reward_points (
  id uuid primary key,
  transaction_id uuid not null,
  user_id uuid not null,
  points integer not null
);