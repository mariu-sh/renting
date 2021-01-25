CREATE TABLE IF NOT EXISTS flat(
    flat_id UUID PRIMARY KEY,
    total_surface DOUBLE PRECISION NOT NULL
);

CREATE TABLE IF NOT EXISTS room(
    room_id UUID PRIMARY KEY,
    flat_id UUID NOT NULL,
    surface DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_flat_id
        FOREIGN KEY (flat_id)
            REFERENCES flat(flat_id)
);