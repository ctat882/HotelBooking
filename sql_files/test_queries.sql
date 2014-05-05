SELECT * FROM Rooms r
	JOIN Hotels h on r.hotel = h.id
	WHERE h.city = 'Sydney'
;

SELECT * FROM BOOKINGS B
	JOIN HOTELS H ON B.HOTEL = H.ID
	WHERE H.CITY = 'Sydney'
		AND B.C