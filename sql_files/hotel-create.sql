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
	availability	VARCHAR(20) DEFAULT 'Available',
	-- Constraints
	CONSTRAINT availability_ck CHECK (availability IN ('Occupied', 'Maintenance', 'Available')),
	CONSTRAINT size_ck CHECK (size IN ('Single', 'Twin', 'Queen', 'Executive', 'Suite')),
	CONSTRAINT price_ck CHECK (price IN (70.00,120.00,180.00,250.00,300.00)),
	-- Keys
	FOREIGN KEY (hotel) REFERENCES Hotels(id),
	PRIMARY KEY(room_num,hotel)
);

-- Booking Table
CREATE TABLE Bookings (
	booking_id		INTEGER GENERATED ALWAYS AS IDENTITY,
	hotel			INTEGER, 
	check_in		DATE NOT NULL,
	check_out		DATE NOT NULL,
	size			VARCHAR(20) NOT NULL,
	quantity		INTEGER NOT NULL,
	pin				INTEGER NOT NULL,
	url				VARCHAR(30) NOT NULL,
	extra_bed		INTEGER DEFAULT 0,

	-- Constraints	
	CONSTRAINT booking_size_ck CHECK (size IN ('Single', 'Twin', 'Queen', 'Executive', 'Suite')),
	CONSTRAINT extra_bed_ck CHECK (extra_bed IN (0,1)),	
	CONSTRAINT pin_ck CHECK (pin > 999 AND pin <= 9999),
	-- because the single rooms are numbered 1 - 15, can check that single room
	-- cannot have extra bed
	--CONSTRAINT single_extra_ck CHECK(room_num < 16 AND extra_bed NOT IN (1));
	-- Keys
	FOREIGN KEY (hotel) REFERENCES Hotels(id),
	PRIMARY KEY(booking_id)
);

-- Discount Table
 CREATE TABLE Discounts (
 	discount_id		INTEGER GENERATED ALWAYS AS IDENTITY,
 	hotel			INTEGER,
 	room_type		VARCHAR(20) NOT NULL,
 	start_date		DATE NOT NULL,
 	end_date		DATE NOT NULL,
 	discount		INTEGER NOT NULL,
 	-- Constraints
 	-- Ensure that the discount is not zero and is not greater than 100
 	CONSTRAINT discounts_disc_ck CHECK (discount <= 100 AND discount > 0),
 	CONSTRAINT discounts_room_type_ck CHECK (room_type IN ('Single', 'Twin', 'Queen', 'Executive', 'Suite')),
 	-- Ensure that start date is before or equal to the end date
 	CONSTRAINT discounts_start_ck CHECK (start_date <= end_date),
 	-- Ensure that the end date is after or equal to the start date
 	CONSTRAINT discount_end_ck CHECK (end_date >= start_date),
 	-- Keys
 	FOREIGN KEY(hotel) REFERENCES Hotels(id),
 	PRIMARY KEY(hotel,room_type,start_date,end_date) 	
 );
 
 --User Table 
 CREATE TABLE Users (
 	hotel_id INTEGER,
 	username VARCHAR(20) NOT NULL,
 	password VARCHAR(20) NOT NULL,
 
 	FOREIGN KEY(hotel_id) REFERENCES Hotels(id),
 	PRIMARY KEY (hotel_id)
 );