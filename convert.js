var mongo = require('mongodb');
var db = new mongo.Db('test', new mongo.Server('localhost', 27017), {
  safe: true
});

var tagsCount = 0;

db.open(function(err) {
  if (err) {
    console.log(err);
  } else {
    console.log('MongoDB Połączono!');

    db.collection('train', function(err, coll) {
      if (err) {
        db.close();
        console.log(err);
      } else {
        var cursor = coll.find();
        var tagsSplited = [];

        cursor.each(function(err, item) {
          if (err) {
            db.close();
            console.log(err);
            return;
          } else if (item !== null) {

            if (item.Tags.constructor === String) {
              tagsSplited = item.Tags.split(" ");
              item.Tags = tagsSplited;
              tagsCount++;

              coll.update({
                "_id": item["_id"]
              }, item, function(err, item) {})

            }

          };
          console.log(tagsCount);

        });

      }
    });

  }

});