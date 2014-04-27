
SELECT * FROM ROOMS R
	JOIN HOTELS H on R.hotel = H.ID
	WHERE H.CITY = 'Melbourne'
;