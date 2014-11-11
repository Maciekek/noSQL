# Table of content

- [System info](#system-info)
- [Short replies](#short-replies)
- [Starting MongoDB](#starting-mongodb)
- [Extended Replies](#extended-replies)
    

# System info

```bash
MongoDB shell version: 2.6.5
Processor: Intel Core i3-330M 2.13 GHz
RAM: 4GB
```



# Short replies
##1a
> ```text 
> Zadanie 1a polega na zaimportowaniu, do baz danych
> uruchomionych na swoim komputerze, danych z pliku Train.csv 
> ```
> 
> ```  
>  $ time 2unix.sh Train.csv TrainPrepare.csv   
>  $ time mongoimport -c train --type csv --headerline --file TrainPrepared.csv 
> ```



##1b

> ```
> Zliczyć liczbę zaimportowanych rekordów
> ```
> ```   
> $ db.train.count()
> ```

##1c
>```
>Zamienić string zawierający tagi na tablicę napisów z tagami następnie zliczyć wszystkie tagi i wszystkie różne tagi.
>``` 
>```
> $ npm install
> $ node skrypt.js
>```

##1d

> ```
> Wyszukać w sieci dane zawierające obiekty GeoJSON. Następnie dane
> zapisać w bazie MongoDB.
> 
> Dla zapisanych danych przygotować co najmniej 6 różnych geospatial
> queries (w tym, co najmniej po jednym, dla obiektów Point, LineString
> i Polygon).
> ```


#Starting MongoDB

```bash
mongod --smallfiles --dbpath path_to_cluster
```

# Extended Replies

##1a

> ```  
>  $ time 2unix.sh Train.csv TrainPrepare.csv   
> ```
>  
>  real  22m2.507s  
>  user  3m32.282s  
>  sys   2m22.363s
>  


> ```  
>   $ time mongoimport -c train --type csv --headerline --file TrainPrepared.csv 
> ```
>  
>  real  uzup
>  user uzup
>  sys uzup
>  

## 1c

> UZUP
> 


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
Przygotowanie kolekcji ``geo`` [convertToJSON.js]()
```
$ db.geoAlabama.ensureIndex({"loc" : "2dsphere"})
$ var punkt = {type: "Point", coordinates: [ -86.8877693,33.4698294]} 
```
#1
Znajdz punkty w odległość od 100 do 65000
Query:
```
$ db.geoAlabama.find({ loc: {$nearSphere:{$geometry:punkt,$minDistance:100,$maxD
istance:65000}}})
```

Wynik: [zobacz](json/1.json)

#2
Znajdź punkty w określonym współrzędnymi polu:
```
$ db.geoAlabama.find({ loc:{$geoWithin: {$polygon:[ [-88.3822308,33.3579777],[-8
6.2504893,33.5718352],[-86.7569171,34.9495258],[ -84.8747566,34.7098128]  ] }}})
```
Wynik: [zobacz](json/2.json)


#3
Znajdź 3 najbliższe punkty typu rzeka/strumień
```
$ db.geoAlabama.find({loc: {$near: {$geometry: punkt}}, type:"Stream"}).limit(3)
```

Wynik: [zobacz](json/3.json)


