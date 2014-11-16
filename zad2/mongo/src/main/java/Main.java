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
    

    }
}
