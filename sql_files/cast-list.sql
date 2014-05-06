SELECT booking_id, hotel, check_in, check_out, size, quantity, pin, url, extra_bed from Bookings;
SELECT * from HOTELS;
SELECT * FROM BOOKINGS;
SELECT * FROM ROOMS;

SELECT room_num from rooms where availability = 'Occupied';

-- select room number with "OCCUPIED"
select room_num, hotel from rooms where availability = 'Occupied';

SELECT room_num,hotel,size,price,availability from rooms where hotel = 1;

select * from discounts;
delete from discounts;


-- ****************    Hotel Owner Page **********************

-- 4 Single rooms booked in Hotel 1


-- change hotel 1 room 1,2,3,4 to 'occupied'
UPDATE Rooms
SET availability='Occupied'
WHERE room_num = 1
AND hotel = 1;	

UPDATE rooms
SET availability = 'Occupied'
WHERE room_num= 2 AND hotel = 1;

UPDATE rooms
SET availability = 'Occupied'
WHERE room_num= 3 AND hotel = 1;

UPDATE rooms
SET availability = 'Occupied'
WHERE room_num= 4 AND hotel = 1;

-- change hotel 1 - twin room 16,17  to 'occupied'
UPDATE Rooms
SET availability='Occupied'
WHERE room_num = 16
AND hotel = 1;	

UPDATE Rooms
SET availability='Occupied'
WHERE room_num = 17
AND hotel = 1;	

-- change hotel 1 - Queen room 31,32  to 'occupied'

UPDATE Rooms
SET availability='Occupied'
WHERE room_num = 31
AND hotel = 1;

UPDATE Rooms
SET availability='Occupied'
WHERE room_num = 32
AND hotel = 1;


-- change hotel 1 - Executive room 36,37  to 'occupied'
UPDATE Rooms
SET availability='Occupied'
WHERE room_num = 36
AND hotel = 1;

UPDATE Rooms
SET availability='Occupied'
WHERE room_num = 37
AND hotel = 1;


-- change hotel 1 - Suite room 39  to 'occupied'
UPDATE Rooms
SET availability='Occupied'
WHERE room_num = 39
AND hotel = 1;




-- select last hotel id
SELECT max(id) FROM hotels;

-- get city
SELECT city from hotels where id = 1;

-- count no. 'occupied' for SINGLE room
select count(availability) from rooms where hotel = 1 AND size = 'Single' AND availability = 'Occupied';

-- count no. 'available' for SINGLE room
select count(availability) from rooms where hotel = 1 AND size = 'Single' AND availability = 'Available';

SELECT COUNT(AVAILABILITY) FROM ROOMS WHERE HOTEL = 1 AND SIZE = 'Single' AND AVAILABILITY = 'Available'

