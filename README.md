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
1
Find points beetwen 100 and 65000 meters
Query:
