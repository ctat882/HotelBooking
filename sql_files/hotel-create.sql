-- Not sure why, but apparently have to execute this statement before
-- creating any tables.
CREATE SCHEMA "USER";

-- Hotel Table
CREATE TABLE Hotels (
	id				INTEGER GENERATED ALWAYS AS IDENTITY,
	city			VARCHAR(20),
	
	PRIMARY KEY (id)
);

-- Room Table
CREATE TABLE Rooms (
	room_num		INTEGER,
	hotel			INTEGER,
	size			VARCHAR(20) NOT NULL,
	price			DECIMAL(5,2) NOT NULL,
	-- Constraints
	CONSTRAINT size_ck CHECK (size IN ('Single', 'Twin', 'Queen', 'Executive', 'Suite')),
	CONSTRAINT price_ck CHECK (price IN (70.00,120.00,180.00,300.00)),
	-- Keys
	FOREIGN KEY (hotel) REFERENCES Hotels(id),
	PRIMARY KEY(room_num,hotel)
);

-- Booking Table
CREATE TABLE Bookings (
	hotel			INTEGER UNIQUE, 
	room_num		INTEGER UNIQUE,
	booked_for		DATE,
	extra_bed		INTEGER DEFAULT 0,
	-- Constraints
	CONSTRAINT extra_bed_ck CHECK (extra_bed IN (0,1)),
	-- because the single rooms are numbered 1 - 15, can check that single room
	-- cannot have extra bed
	--CONSTRAINT single_extra_ck CHECK(room_num < 16 AND extra_bed NOT IN (1));
	-- Keys
	FOREIGN KEY (room_num,hotel) REFERENCES Rooms(room_num,hotel),
	PRIMARY KEY(hotel,room_num,booked_for)
);

