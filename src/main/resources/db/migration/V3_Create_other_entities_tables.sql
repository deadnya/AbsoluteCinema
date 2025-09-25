CREATE TABLE films (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    duration_minutes INTEGER NOT NULL,
    age_rating VARCHAR(16) NOT NULL
);

CREATE TABLE halls (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    number INTEGER NOT NULL
);

CREATE TABLE seat_categories (
    id UUID PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    price_cents INTEGER NOT NULL
);

CREATE TABLE seats (
    id UUID PRIMARY KEY,
    hall_id UUID NOT NULL REFERENCES halls(id) ON DELETE CASCADE,
    row_number INTEGER NOT NULL,
    seat_number INTEGER NOT NULL,
    category_id UUID NOT NULL REFERENCES seat_categories(id),
    CONSTRAINT uq_seat UNIQUE (hall_id, row_number, seat_number)
);

CREATE TABLE sessions (
    id UUID PRIMARY KEY,
    film_id UUID NOT NULL REFERENCES films(id),
    hall_id UUID NOT NULL REFERENCES halls(id),
    start_at TIMESTAMP NOT NULL,
    slot_start_at TIMESTAMP NOT NULL,
    slot_end_at TIMESTAMP NOT NULL
);

CREATE TABLE purchases (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL REFERENCES users(id),
    total_cents INTEGER NOT NULL,
    status VARCHAR(16) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tickets (
    id UUID PRIMARY KEY,
    session_id UUID NOT NULL REFERENCES sessions(id) ON DELETE CASCADE,
    seat_id UUID NOT NULL REFERENCES seats(id),
    category_id UUID NOT NULL REFERENCES seat_categories(id),
    price_cents INTEGER NOT NULL,
    status VARCHAR(16) NOT NULL,
    reserved_until TIMESTAMP NULL,
    purchase_id NULL REFERENCES purchases(id) ON DELETE SET NULL,
    CONSTRAINT uq_ticket UNIQUE (session_id, seat_id)
);

CREATE TABLE reviews (
    id UUID PRIMARY KEY,
    film_id UUID NOT NULL REFERENCES films(id) ON DELETE CASCADE,
    client_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL,
    text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_review UNIQUE (film_id, client_id)
);