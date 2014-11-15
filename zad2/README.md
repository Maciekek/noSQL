#Zadanie 2
---

##Dane 

Zbiorem danych jakim się posługuje przy tym zadaniu są dane na temat ruchu lotniczego w Stanach Zjednoczonych. Średnio jeden miesiąc zawiera około 500tyś. rekordów. Przy moich agregacjach użyje danych z okresu 3 miesięcy.

##Liczba rekordów

```
1406309
```

##Agregacje

###1 Agregacja pokazująca Linie lotniczą z największą liczbą anulowanych lotów

```js
db.airtest.aggregate({
	$match: {
		AIRLINE_ID: {
			$ne: ""
		}
	}
}, {
	$group: {
		_id: "$AIRLINE_ID",
		cancelled: {
			$sum: "$CANCELLED"
		}
	}
}, {
	$sort: {
		cancelled: -1
	}
}, {
	$limit: 5
})
```

####Wynik:

```json
{ "_id" : 20366, "cancelled" : 18432 }
{ "_id" : 20398, "cancelled" : 8358 }
{ "_id" : 19393, "cancelled" : 8275 }
{ "_id" : 20304, "cancelled" : 6073 }
{ "_id" : 19790, "cancelled" : 5709 }
```

-----

###2 Agregacja pokazująca numery lini lotniczych z największą liczbą anulowań w miesiącu styczniu

```js
db.airtest.aggregate({
	$match: {
		MONTH: 1
	}
}, {
	$group: {
		_id: "$AIRLINE_ID",
		cancelled: {
			$sum: "$CANCELLED"
		}
	}
}, {
	$sort: {
		cancelled: -1
	}
}, {
	$limit: 5
})
```

####Wynik

```json
{ "_id" : 20366, "cancelled" : 8987 }
{ "_id" : 19393, "cancelled" : 4439 }
{ "_id" : 20398, "cancelled" : 4205 }
{ "_id" : 19790, "cancelled" : 2997 }
{ "_id" : 20304, "cancelled" : 2663 }
```



###3 Porty lotnicze z największą liczbą anulowanych lotów

```json
db.airline.aggregate({
	$match: {
		AIRLINE_ID: {
			$ne: ""
		}
	}
}, {
	"$group": {
		"_id": "$DEST_CITY_NAME",
		"cancelled": {
			"$sum": "$CANCELLED"
		}
	}
}, {
	$sort: {
		cancelled: -1
	}
}, {
	$limit: 5
})
```

####Wynik

```json
{ "_id" : "Chicago, IL", "cancelled" : 7573 }
{ "_id" : "Atlanta, GA", "cancelled" : 5192 }
{ "_id" : "New York, NY", "cancelled" : 3863 }
{ "_id" : "Dallas/Fort Worth, TX", "cancelled" : 2874 }
{ "_id" : "Newark, NJ", "cancelled" : 2809 }
```


###4 Najpopularniejsze porty lotnicze

```json
db.airline.aggregate([{
	"$group": {
		"_id": "$DEST_CITY_NAME",
		"value": {
			"$sum": 1
		}
	}
}, {
	"$sort": {
		"value": -1
	}
}, {
	"$limit": 5
}]);
```

####Wynik

```json
{ "_id" : "Atlanta, GA", "value" : 91468 }
{ "_id" : "Chicago, IL", "value" : 84317 }
{ "_id" : "Dallas/Fort Worth, TX", "value" : 68481 }
{ "_id" : "Houston, TX", "value" : 58076 }
{ "_id" : "Los Angeles, CA", "value" : 54081 }
```


###5 Najpopularniejsze linie lotnicze

```json
db.airline.aggregate([{
	"$group": {
		"_id": "$AIRLINE_ID",
		"value": {
			"$sum": 1
		}
	}
}, {
	"$sort": {
		"value": -1
	}
}, {
	"$limit": 5
}]);
```

####Wynik

```json
{ "_id" : 19393, "value" : 277527 }
{ "_id" : 19790, "value" : 180224 }
{ "_id" : 20366, "value" : 173956 }
{ "_id" : 20304, "value" : 148886 }
{ "_id" : 19805, "value" : 132707 }
```

###6 Liczba opóźnień spowodowanych złymi warunkami atmosferycznymi (do 15 do 20 minut)


```json
db.airline.aggregate([{
	$match: {
		"WEATHER_DELAY": {
			$gt: 15,
			$lte: 20
		}
	}
}, {
	$group: {
		_id: "Weather_cast",
		count: {
			$sum: 1
		}
	}
}]);
```

####Wynik

```json
{ "_id" : "Weather_cast", "count" : 2225 }
```

###7 Miasta portów lotniczych z największym średnim czasem opóźnienia

```json
db.airline.aggregate([{
	$group: {
		_id: {
			city: "$ORIGIN_CITY_NAME"
		},
		avg_score: {
			$avg: "$WEATHER_DELAY"
		}
	}
}, {
	$sort: {
		avg_score: -1
	}
}, {
	$limit: 5
}]);
```

####Wynik
```json
{ "_id" : { "city" : "Klamath Falls, OR" }, "avg_score" : 50.875 }
{ "_id" : { "city" : "Bangor, ME" }, "avg_score" : 25.958333333333332 }
{ "_id" : { "city" : "Yakutat, AK" }, "avg_score" : 24.666666666666668 }
{ "_id" : { "city" : "Santa Maria, CA" }, "avg_score" : 21.70212765957447 }
{ "_id" : { "city" : "Sitka, AK" }, "avg_score" : 18.61111111111111 }
```


```


