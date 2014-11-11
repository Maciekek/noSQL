# Table of content

- [System info](#system-info)
- [Starting MongoDB](#starting-mongodb)
- [Short replies](#short-replies)
- [Extended Replies](#extended-replies)
    - [1a](#1a-1)
    - [1c](#1c-1)
    - [1d](#1d-1)
- [Podsumowanie czasów](#podsumowanie-czas%C3%B3w)
    

# System info

```bash
MongoDB shell version: 2.6.5
Processor: Intel Core i3-330M 2.13 GHz
RAM: 4GB
```


#Starting MongoDB

```bash
mongod --smallfiles --dbpath path_to_cluster
```

# Short replies
##1a
```text 
Zadanie 1a polega na zaimportowaniu, do baz danych
uruchomionych na swoim komputerze, danych z pliku Train.csv 
```

```  
 $ time 2unix.sh Train.csv TrainPrepare.csv   
 $ time mongoimport -c train --type csv --headerline --file TrainPrepared.csv 
```



##1b

 ```
 Zliczyć liczbę zaimportowanych rekordów
 ```
 ```   
 $ db.train.count()
 ```

##1c
```
Zamienić string zawierający tagi na tablicę napisów z tagami następnie zliczyć wszystkie tagi i wszystkie różne tagi.
``` 
```
 $ npm install
 $ node skrypt.js
```

##1d

```
Wyszukać w sieci dane zawierające obiekty GeoJSON. Następnie dane
zapisać w bazie MongoDB.
 
Dla zapisanych danych przygotować co najmniej 6 różnych geospatial
queries (w tym, co najmniej po jednym, dla obiektów Point, LineString
i Polygon).
```



# Extended Replies

##1a
##### Zmiana kodowanie
```  
 $ time 2unix.sh Train.csv TrainPrepare.csv   
```
 
 real  22m2.507s  
 user  3m32.282s  
 sys   2m22.363s
 
Jak widać proces zmiany znaków zajął dość sporo czasu (też słaby komputer do takich operacji), jednak jak widać po zdjęciu system zostawił sobie trochę pamięci to innych procesów:
![zużycie pamięci przez 2unixUsage](img/2unixUsage.png)


##### Import danych do bazy
```  
  $ time mongoimport -c train --type csv --headerline --file TrainPrepared.csv 
```

real 39m2.516s
user 11m45.381s
sys  3m11m127s
 
Całkowity czas imporotowania do bazy to: 39m16s


Na monitorze zasobów start importera jest bardzo dobrze widoczny: 
![import- start](img/mongoimportStart.png)
Przy importowaniu komputer był bardzo obciążony. Praktycznie przez cały proces importu nie dało się z niego korzystać.
![import- pratyczne zawalenie komputer](img/afterImport.png)
Co ciekawe, nawet po zakończonym imporcie, proces mongoimport nie zwolnił używanej przez siebie pamięci. Nawet godzinę po zakończonym imporcie komputer był bardzo spowolniony. Pomógł dopiero restart systemu.

Ciekawą rzecz można również zaobserwować gdy odłączy się laptopa od zasilacza. Ewidentnie spada wtedy wydajność komputera co widać na obrazku:
![komputer na baterii](img/unplaged.png)


 

## 1c

[Skrypt](convert.js) 

Skrypt zmieniający format danych String na tablice tagów, wykrywanie odbywa się po spacji
```
 $ node convert.js    
```
```
real  14m31.145s                                                                                                          
user  09m11.126s                                                                                                          
sys   0m11.127s                                                                                                                  
```


## 1d

Zbiór danych: http://geonames.usgs.gov/docs/stategaz/AL_Features_20141005.zip

Przygotowanie danych: 

```
$ tr tr '|' ',' < AL_Features_20141005.txt > geo.txt
```

Import danych do bazy:
```
$mongoimport -c geo -type csv -file geo.txt --headerline
```
Przygotowanie kolekcji ``geo`` [convertToJSON.js](convertToJSON.js)
```
$ db.geoAlabama.ensureIndex({"loc" : "2dsphere"})
$ var punkt = {type: "Point", coordinates: [ -86.8877693,33.4698294]} 
```
##1
Znajdz punkty w odległość od 100 do 65000
Query:
```
$ db.geoAlabama.find({ loc: {$nearSphere:{$geometry:punkt,$minDistance:100,$maxD
istance:65000}}})
```

Wynik: [zobacz](json/1.json)
Mapka: [zobacz](geojson/1near.geojson)

##2
Znajdź punkty w określonym współrzędnymi polu:
```
$ db.geoAlabama.find({
    loc: {
        $geoWithin: {
            $polygon: [
                [
                    -88.3822308,
                    33.3579777
                ],
                [
                    -86.2504893,
                    33.5718352
                ],
                [
                    -86.7569171,
                    34.9495258
                ],
                [
                    -84.8747566,
                    34.7098128
                ]
            ]
        }
    }
})
```
Wynik: [zobacz](json/2.json)
Mapka: [zobacz](geojson/2polygon.geojson)

##3
Znajdź 3 najbliższe punkty typu rzeka/strumień
```
$ db.geoAlabama.find({
    loc: {
        $near: {
            $geometry: punkt
        }
    },
    type: "Stream"
}).limit(3)
```

Wynik: [zobacz](json/3.json)
Mapka: [zobacz](geojson/nearestPoint.geojson)


##4
Znajdz puknty w wycinku kołowym określonym stopniami 
```
$ db.geoAlabama.find({
    loc: {
        $geoWithin: {
            $centerSphere: [
                [
                    -86.5683234,
                    33.985376
                ],
                13/3959
            ]
        }
    }
})
```


Wynik: [zobacz](json/4.json)
Mapka: [zobacz](geojson/circlePoints.geojson)


##5
Punkty typu rzeka/strumień znajdujące się powyżej 230m
```
$ db.geoAlabama.find({
    loc: {
        $near: {
            $geometry: punkt
        }
    },
    type: "Stream",
    height: {
        $gt: 230
    }
})
```

Wynik: [zobacz](json/5.json)
Mapka: [zobacz](geojson/streamWithGt200.geojson)

##6
Punkty w wycinku kołowym z wykorzystaniem "center"
``` 
$ db.geoAlabama.find({
    loc: {
        $geoWithin: {
            $center: [
                [
                    -90.5683234,
                    31.985376
                ],
                10/4
            ]
        }
    }
})
```

Wynik: [zobacz](json/5.json)
Mapka: [zobacz](geojson/circle.geojson)

#Dodatkowe informacje o kolekcji geoAlabama:
```
{
        "ns" : "test.geoAlabama",
        "count" : 51,
        "size" : 12240,
        "avgObjSize" : 240,
        "storageSize" : 22507520,
        "numExtents" : 7,
        "nindexes" : 2,
        "lastExtentSize" : 11325440,
        "paddingFactor" : 1,
        "systemFlags" : 1,
        "userFlags" : 1,
        "totalIndexSize" : 16352,
        "indexSizes" : {
                "_id_" : 8176,
                "loc_2dsphere" : 8176
        },
        "ok" : 1
}
```


# Podsumowanie czasów

|  Czynność             | Czas rzeczywisty  | 
|-----------------------|-------------------|
|Zmiana kodowania       |   22m2.507s       | 
|Import                 |   39m2.516s       |  
|Konwertowanie          |   14m31.145s      |  

