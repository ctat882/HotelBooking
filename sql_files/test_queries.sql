SELECT * FROM Rooms r
	JOIN Hotels h on r.hotel = h.id
	WHERE h.city = 'Sydney'
;

SELECT count(*) FROM DISCOUNTS d
	JOIN Hotels h ON d.hotel = h.id 
	WHERE h.city = 'Sydney'
		AND (d.start_date BETWEEN DATE('2015-07-02') AND DATE('2015-07-05')
		OR d.end_date BETWEEN DATE ('2015-07-02') AND DATE('2015-07-05') )
		
		
SELECT * FROM Discounts;

SELECT * FROM Discounts d 
	JOIN Hotels h ON d.hotel = h.id 
	WHERE h.city = 'Sydney'
		AND d.start_date BETWEEN DATE('2015-07-02') AND DATE('2015-07-05')
		OR d.end_date BETWEEN DATE ('2015-07-02') AND DATE('2015-07-05')
		

SELECT * FROM BOOKINGS B
	JOIN HOTELS H ON B.HOTEL = H.ID
	WHERE H.CITY = 'Sydney'
		AND B.C