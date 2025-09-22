CREATE TABLE tokens (
    id UUID PRIMARY KEY,
    access_token TEXT NOT NULL,
    is_logged_out BOOLEAN NOT NULL DEFAULT FALSE,
    user_id UUID NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_tokens_access_token ON tokens (access_token);
CREATE INDEX idx_tokens_user_id ON tokens (user_id);
CREATE INDEX idx_tokens_expiration_date ON tokens (expiration_date);