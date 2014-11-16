import com.mongodb.*;

public class Main {
    public static MongoClient getConnection() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        return mongoClient;
    }

    public static void getAirLineIDByMonth(DBCollection coll) {
        System.out.println("Wynik: ");
        DBObject match = new BasicDBObject("$match", new BasicDBObject("MONTH", 1));
        DBObject groupFields = new BasicDBObject("_id", "$AIRLINE_ID");
        groupFields.put("cancelled", new BasicDBObject("$sum", "$CANCELLED"));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("cancelled", -1));

        AggregationOutput output = coll.aggregate(match, group, sort);
        System.out.println(output.results());
    }

    public static void getAirLineMostCancelled(DBCollection coll) {
        System.out.println("Najwieksza liczba anulowań ");
        DBObject match = new BasicDBObject("$match", new BasicDBObject("AIRLINE_ID", new BasicDBObject("$ne", "")));

        DBObject groupFields = new BasicDBObject("_id", "$AIRLINE_ID");
        groupFields.put("cancelled", new BasicDBObject("$sum", "$CANCELLED"));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("cancelled", -1));
        DBObject limit = new BasicDBObject("$limit", 5);

        AggregationOutput output = coll.aggregate(match, group, limit, sort);
        System.out.println(output.results());

    }
    public static void getAirPortMostCancelled(DBCollection coll) {
        System.out.println("Najwieksza liczba anulowań ");
        DBObject match = new BasicDBObject("$match", new BasicDBObject("AIRLINE_ID", new BasicDBObject("$ne", "")));

        DBObject groupFields = new BasicDBObject("_id", "$DEST_CITY_NAME");
        groupFields.put("cancelled", new BasicDBObject("$sum", "$CANCELLED"));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("cancelled", -1));
        DBObject limit = new BasicDBObject("$limit", 5);

        AggregationOutput output = coll.aggregate(match, group, limit, sort);
        System.out.println(output.results());
    }

    public static void getMostPopularityAirPort(DBCollection coll){
        System.out.println("Najpopularniejsze porty lotnicze");

        DBObject groupFields = new BasicDBObject("_id", "$DEST_CITY_NAME");
        groupFields.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("value", -1));
        DBObject limit = new BasicDBObject("$limit", 5);

        AggregationOutput output = coll.aggregate(group, limit, sort);
        System.out.println(output.results());

    }

    public static void getMostPopularityAirLine(DBCollection coll){
        System.out.println("Najpopularniejsze linie lotnicze");

        DBObject groupFields = new BasicDBObject("_id", "$AIRLINE_ID");
        groupFields.put("value", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("value", -1));
        DBObject limit = new BasicDBObject("$limit", 5);

        AggregationOutput output = coll.aggregate(group, limit, sort);
        System.out.println(output.results());

    }

    public static void getDelayByWeather(DBCollection coll) {
        System.out.println("Opóźnienia spowodowane pogodą ");

        DBObject groupMatch = new BasicDBObject("$gt",15);
        groupMatch.put("$lt", 20);

        DBObject match = new BasicDBObject("$match", new BasicDBObject("WEATHER_DELAY", groupMatch));

        DBObject groupFields = new BasicDBObject("_id", "Weather_cast");
        groupFields.put("count", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);

        AggregationOutput output = coll.aggregate(match, group);
        System.out.println(output.results());

    }




    public static void main(String[] args) throws Exception {
        System.out.println("Witaj w programie ");
        MongoClient mongoClient = getConnection();
        DB db = mongoClient.getDB("test");


        DBCollection coll = db.getCollection("airtest");
        System.out.println("Rozmiar kolekcji:  " + coll.count());

        getAirLineIDByMonth(coll);
        System.out.println();
        getAirLineMostCancelled(coll);
        System.out.println();
        getAirPortMostCancelled(coll);
        System.out.println();
        getMostPopularityAirPort(coll);
        System.out.println();
        getMostPopularityAirLine(coll);
        System.out.println();
        getDelayByWeather(coll);
        System.out.println();
        System.out.print("KONIEC");
//        System.out.println(coll.findOne());

// CHECKSTYLE:OFF
//
//
//
//            // connect to the local database server
//            MongoClient mongoClient = new MongoClient();
//
//            // get handle to "mydb"
//            DB db = mongoClient.getDB("mydb");
//
//            // Authenticate - optional
//            // boolean auth = db.authenticate("foo", "bar");
//
//            // Add some sample data
//            DBCollection coll = db.getCollection("aggregationExample");
//            coll.insert(new BasicDBObjectBuilder()
//                    .add("employee", 1)
//                    .add("department", "Sales")
//                    .add("amount", 71)
//                    .add("type", "airfare")
//                    .get());
//            coll.insert(new BasicDBObjectBuilder()
//                    .add("employee", 2)
//                    .add("department", "Engineering")
//                    .add("amount", 15)
//                    .add("type", "airfare")
//                    .get());
//            coll.insert(new BasicDBObjectBuilder()
//                    .add("employee", 4)
//                    .add("department", "Human Resources")
//                    .add("amount", 5)
//                    .add("type", "airfare")
//                    .get());
//            coll.insert(new BasicDBObjectBuilder()
//                    .add("employee", 42)
//                    .add("department", "Sales")
//                    .add("amount", 77)
//                    .add("type", "airfare")
//                    .get());
//
//            // create our pipeline operations, first with the $match
//            DBObject match = new BasicDBObject("$match", new BasicDBObject("type", "airfare"));
//
//            // build the $projection operation
//            DBObject fields = new BasicDBObject("department", 1);
//            fields.put("amount", 1);
//            fields.put("_id", 0);
//            DBObject project = new BasicDBObject("$project", fields );
//
//            // Now the $group operation
//            DBObject groupFields = new BasicDBObject( "_id", "$department");
//            groupFields.put("average", new BasicDBObject( "$avg", "$amount"));
//            DBObject group = new BasicDBObject("$group", groupFields);
//
//            // Finally the $sort operation
//            DBObject sort = new BasicDBObject("$sort", new BasicDBObject("average", -1));
//
//            // run aggregation
//            List<DBObject> pipeline = Arrays.asList(match, project, group, sort);
//            AggregationOutput output = coll.aggregate(pipeline);
//
//            // Output the results
//            for (DBObject result : output.results()) {
//                System.out.println(result);
//            }
//
//

    }
}
