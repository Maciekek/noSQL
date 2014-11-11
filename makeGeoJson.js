var mongo = require('mongodb');
var fs = require('fs');
var db = new mongo.Db('test', new mongo.Server('localhost', 27017), {
  safe: true
});

var tagsCount = 0;
var geoJSON = {
  "type": "FeatureCollection",
  "features": []
};
db.open(function(err) {
  if (err) {
    console.log(err);
  } else {
    console.log('MongoDB Połączono!');

    db.collection('geo_points', function(err, coll) {
      if (err) {
        db.close();
        console.log(err);
      } else {
        var cursor = coll.find();
        var tagsSplited = [];

        cursor.each(function(err, item) {
          if (item !== null) {


            console.log(item);
            geoJSON.features.push({
              "type": "Feature",
              "geometry": {
                "type": "Point",
                "coordinates": [item.loc.coordinates[0], item.loc.coordinates[1]]
              },
              "properties": {
                "title": item.name,
                "description": item.type,
               
               
              }
            })
            console.dir(geoJSON);
            fs.writeFile("./geoTest.txt", JSON.stringify(geoJSON), function(err) {
              if (err) {
                console.log(err);
              } else {
                console.log("The file was saved!");
              }
            });

          }
        });



      }
    });



  }

});