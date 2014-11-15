var Db = require('mongodb').Db,
  MongoClient = require('mongodb').MongoClient,
  Server = require('mongodb').Server,
  Q = require('q');
var db = new Db('test', new Server('localhost', 27017), {
  safe: true
});

var getCollectionCount = function(collection) {
  console.log("\n-----------------------------------");
  var deferred = Q.defer();
  collection.count(function(err, count) {
    collectionCount = count;
    console.log("Collection count = " + collectionCount);
    deferred.resolve(collectionCount);



  });
  return deferred.promise;
}

var getCancelledByAirLine = function(collection) {

  collection.aggregate([{
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
  }], function(err, airs) {
    console.log("\n-----------------------------------");
    console.log("Samoloty anulowane ogółem \n");
    for (air in airs) {
      var air = airs[air];
      console.log("Samolot numer: " + air["_id"] + " anulowany: " + air["cancelled"] + " razy");
    }

  });

}

var getCancelledByAriLineInMonth = function(collection) {


  collection.aggregate([{
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
    }],
    function(err, airs) {
      console.log("\n-----------------------------------");
      console.log("Samoloty anulowane w danym miesiacu \n");
      for (air in airs) {
        var air = airs[air];
        console.log("Samolot numer: " + air["_id"] + " anulowany: " + air["cancelled"] + " razy");
      }

    });

}

var getMostPopularityAirPort = function(collection) {

  collection.aggregate([{
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
    }],
    function(err, airPorts) {
      console.log("\n-----------------------------------");
      console.log("Najpopularniejsze porty lotnicze \n");

      for (airPort in airPorts) {
        var airPort = airPorts[airPort];
        console.log("Port: " + airPort["_id"] + " lotów: " + airPort["value"]);
      }

    });

}

var getMostPopularityAirLine = function(collection) {


  collection.aggregate([{
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
    }],
    function(err, airLines) {
      console.log("\n-----------------------------------");
      console.log("Najpopularniejsze linie lotnicze \n");

      for (airLine in airLines) {
        var airLine = airLines[airLine];
        console.log("Numer lini: " + airLine["_id"] + " wykonanych lotów: " + airLine["value"]);
      }

    });

}

var getCancelledByAirPort = function(collection) {


  collection.aggregate([{
      $match: {
        AIRLINE_ID: {
          $ne: ""
        }
      }
    }, {
      $group: {
        _id: "$DEST_CITY_NAME",
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
    }],
    function(err, airs) {
      console.log("\n-----------------------------------");
      console.log("Porty lotnicze z najwieksza iloscia anulowanych lotow\n");

      //console.log(airs);
      for (air in airs) {
        var air = airs[air];

        console.log("Port lotniczy: " + air["_id"] + " liczba anulowań: " + air["cancelled"] + " razy");
      }

    });
}

var getDelayAirLine = function(collection) {


  collection.aggregate([{
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
    }],
    function(err, airLines) {
      console.log("\n-----------------------------------");
      console.log("Liczba opóźnień spowodowych pogodą w długości opóźnienia pomiędzy 15 - 20 minut \n");

      console.log("Liczba opóźnień " + airLines[0]["count"]);
    });

}

var getAvgDelayByCity = function(collection) {


  collection.aggregate([{
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
    }],
    function(err, airLines) {
      console.log("\n-----------------------------------");
      console.log("Największe średnie opóźnienia w miastach portów lotniczych \n");

      for (air in airLines) {
        var air = airLines[air];
        console.log("Miasto:  " + air["_id"]["city"] + " średnia liczba: " + air["avg_score"]);
      }
    });

}

var getCollAgg1 = function(collection) {
  collection.aggregate([{
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
  }], function(err, result) {
    console.log("\n-----------------------------------");
    console.dir(result);

  });


} 

db.open(function(err) {
  var collection = db.collection('airline');
  if (err) {
    console.log(err);
  } else {
    console.log('MongoDB Połączono!');

  }
  getCancelledByAriLineInMonth(collection);
  getCancelledByAirPort(collection);
  getCollectionCount(collection);
  getCancelledByAirLine(collection);
  //getCollAgg1(collection);

  getMostPopularityAirPort(collection);
  getMostPopularityAirLine(collection);
  getDelayAirLine(collection);
  getAvgDelayByCity(collection);


});