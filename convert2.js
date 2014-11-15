var Db = require('mongodb').Db,
  MongoClient = require('mongodb').MongoClient,
  Server = require('mongodb').Server;

var db = new Db('test', new Server('localhost', 27017), {
  safe: true
});


// db.trainTag.drop();

var itemCount = 0;
var tagsCount = 0;
var collectionCount = 0;
var items = [];
var updatesCount = 0;


var insertData = function(items) {
  db.collection('trainConvertedTag', function(err, coll) {
    if (err) {
      db.close();
      console.log(err);
    } else {
      coll.insert(items, function(err, item) {
        console.log(items.length);
        console.log("Wykonano " + items.length + " insertów.");
      });
    }
  });
}


db.open(function(err) {
  var collection = db.collection('train2');
  collection.count(function(err, count) {
    collectionCount = count;
    console.log("Collection count = " + collectionCount);
  })
  if (err) {
    console.log(err);
  } else {
    console.log('MongoDB Połączono!');

    db.collection('train2', function(err, coll) {

      if (err) {
        db.close();
        console.log(err);
      } else {
        var cursor = coll.find();
        var tagsSplited = [];

        cursor.each(function(err, item) {
          updatesCount++;
          if (err) {
            db.close();
            console.log(err);
            return;
          } else if (item !== null) {

            if (item.Tags.constructor === String) {
              tagsSplited = item.Tags.split(" ");
              item.Tags = tagsSplited;
              tagsCount++;

              items.push(item);

            }

            if (updatesCount % 1000 === 0 || updatesCount === collectionCount) {
              insertData(items);
              console.log(collectionCount + "      !!!!!!!!!!!");
              items = [];
            }

          };

          console.log(tagsCount);

        });

      }
    });

  }
});

//for (i = 0; i < 1010; i++) {    db.test.insert({"id":i,"Title":"test", "Body":"ttest","Tags":"Test test test"});  }