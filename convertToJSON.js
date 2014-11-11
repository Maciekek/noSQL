var mongo = db.geo.find();
var records = 0;
mongo.forEach(function(record) {
	var place = {
		"id": record["FEATURE_ID"],
		"name": record["FEATURE_NAME"],
		"type": record["FEATURE_CLASS"],
		"country_name": record["COUNTRY_NAME"],
		"loc": {
			"type": "Point",
			"coordinates": [record["PRIM_LONG_DEC"], record["PRIM_LAT_DEC"]]
		}
	};
	db.geoAlabama.insert(place);
	records++;
});
print(records);