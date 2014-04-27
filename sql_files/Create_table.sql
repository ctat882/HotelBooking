
CREATE domain RoomSize AS 
	varchar(20) CHECK (value IN ('Single', 'Twin', 'Queen', 'Executive', 'Suite'));
	
CREATE domain RoomPrice AS
	decimal(5,2) CHECK (value IN ('70','120','180','300'));


CREATE TABLE Hotels (
	id				serial,
	city			VARCHAR(20),
	
	PRIMARY KEY (id)
);

CREATE TABLE Room (
	room_num		serial,
	hotel			serial REFERENCES Hotels(id),
	size			RoomSize NOT NULL,
	price			RoomPrice NOT NULL,
	PRIMARY KEY(number,hotel)
);


CREATE TABLE Booking (
	hotel			serial REFERENCES Room(hotel),
	room_number		serial REFERENCES Room(room_num),
	booked_for		date,
	extra_bed		boolean DEFAULT false,
	PRIMARY KEY(hotel,room_number,booked_for)
);

