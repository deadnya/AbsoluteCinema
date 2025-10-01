CREATE TABLE payments (
    id UUID PRIMARY KEY,
    purchase_id UUID NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL,
    CONSTRAINT fk_payment_purchase FOREIGN KEY (purchase_id) REFERENCES purchases(id) ON DELETE CASCADE
);

CREATE INDEX idx_payments_purchase_id ON payments (purchase_id);
CREATE INDEX idx_payments_status ON payments (status);