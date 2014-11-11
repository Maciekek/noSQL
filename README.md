# Table of content

- [System info](#system-info)
- [Short replies](#short-replies)
	- [1a](##1a)
	- [1b](##1b)
	- [1c](##1c)
	- [1d](##1d)
- [Starting MongoDB](#Starting-MongoDB)
- [Extended Replies](#Extended-Replies)
    - [1a](##1a)
	- [1b](##1b)
	- [1c](##1c)
	- [1d](##1d)


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

> ```  
>  Wyszukać w sieci dane zawierające obiekty GeoJSON. Następnie dane zapisać w bazie MongoDB.  
> ```
Data: http://geonames.usgs.gov/docs/stategaz/AL_Features_20141005.zip
>
Prepare data: 
```
$ tr tr '|' ',' < AL_Features_20141005.txt > geo.txt
```
>
Import:
```
$mongoimport -c geo -type csv -file geo.txt --headerline
```
Prepare collection ``geo`` [convertToJSON.js]()
```
$ db.geoAlabama.ensureIndex({"loc" : "2dsphere"})
$ var punkt = {type: "Point", coordinates: [ -86.8877693,33.4698294]} 
```
1
Find points beetwen 100 and 65000 meters
Query: 
> db.geo_points.find({ loc: {$nearSphere:{$geometry:punkt,$minDistance:100,$maxD
istance:65000}}})
Result: 
{ "_id" : ObjectId("54613818d2ed2daf885a2055"), "id" : 112874, "name" : "Thomas
Branch", "type" : "Stream", "country_name" : null, "height" : 151, "loc" : { "ty
pe" : "Point", "coordinates" : [ -86.6905413, 33.2742799 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2044"), "id" : 112857, "name" : "Island
Branch", "type" : "Stream", "country_name" : null, "height" : 67, "loc" : { "typ
e" : "Point", "coordinates" : [ -87.3586122, 33.4506682 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2039"), "id" : 112845, "name" : "Dry Cre
ek", "type" : "Stream", "country_name" : null, "height" : 149, "loc" : { "type"
: "Point", "coordinates" : [ -86.5683234, 33.985376 ] } }

View: []()
2
Find point in square:
>> db.geo_points.find({ loc:{$geoWithin: {$polygon:[ [-88.3822308,33.3579777],[-8
6.2504893,33.5718352],[-86.7569171,34.9495258],[ -84.8747566,34.7098128]  ] }}})

Result: 
{ "_id" : ObjectId("54613818d2ed2daf885a2032"), "id" : 112838, "name" : "Blue Sp
ring", "type" : "Spring", "country_name" : null, "height" : 210, "loc" : { "type
" : "Point", "coordinates" : [ -86.5938821, 34.7862014 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2038"), "id" : 112844, "name" : "Corneli
us Mountain", "type" : "Summit", "country_name" : null, "height" : 296, "loc" :
{ "type" : "Point", "coordinates" : [ -86.5366558, 33.9992646 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2039"), "id" : 112845, "name" : "Dry Cre
ek", "type" : "Stream", "country_name" : null, "height" : 149, "loc" : { "type"
: "Point", "coordinates" : [ -86.5683234, 33.985376 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a204c"), "id" : 112865, "name" : "Noahs A
rk", "type" : "Summit", "country_name" : null, "height" : 327, "loc" : { "type"
: "Point", "coordinates" : [ -86.2983168, 34.4742555 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2059"), "id" : 112878, "name" : "Aaron B
ranch", "type" : "Stream", "country_name" : null, "height" : 217, "loc" : { "typ
e" : "Point", "coordinates" : [ -86.4116516, 34.1481515 ] } }

View: []()

3
Find 3 nearest Stream 
> $ db.geo_points.find({loc: {$near: {$geometry: punkt}}, type:"Stream"}).limit(3)

Result:
{ "_id" : ObjectId("54613818d2ed2daf885a2049"), "id" : 112862, "name" : "Nabors
Branch", "type" : "Stream", "country_name" : null, "height" : 158, "loc" : { "ty
pe" : "Point", "coordinates" : [ -86.8877693, 33.4698294 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2055"), "id" : 112874, "name" : "Thomas
Branch", "type" : "Stream", "country_name" : null, "height" : 151, "loc" : { "ty
pe" : "Point", "coordinates" : [ -86.6905413, 33.2742799 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2044"), "id" : 112857, "name" : "Island
Branch", "type" : "Stream", "country_name" : null, "height" : 67, "loc" : { "typ
e" : "Point", "coordinates" : [ -87.3586122, 33.4506682 ] } }


4
Find points in circle
> $ db.geo_points.find({ loc: {$geoWithin: {$centerSphere: [  [ -86.5683234,33.985376],13/3959]}}})

Result:

{ "_id" : ObjectId("54613818d2ed2daf885a2041"), "id" : 112854, "name" : "Hales Creek", "type" : "Stream", "country_name" : null, "height" : 247, "loc" : { "type
" : "Point", "coordinates" : [ -86.3905392, 34.0064863 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2039"), "id" : 112845, "name" : "Dry Creek", "type" : "Stream", "country_name" : null, "height" : 149, "loc" : { "type"
: "Point", "coordinates" : [ -86.5683234, 33.985376 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2038"), "id" : 112844, "name" : "Cornelius Mountain", "type" : "Summit", "country_name" : null, "height" : 296, "loc" :
{ "type" : "Point", "coordinates" : [ -86.5366558, 33.9992646 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2036"), "id" : 112842, "name" : "Copeland Creek", "type" : "Stream", "country_name" : null, "height" : 190, "loc" : { "t
ype" : "Point", "coordinates" : [ -86.5811027, 34.1542634 ] } }
>


5 
> db.geo_points.find({loc: {$near: {$geometry: punkt}}, type:"Stream", height:{$gt:230}})

Result:
> db.geo_points.find({loc: {$near: {$geometry: punkt}}, type:"Stream", height:{$
gt:230}})
{ "_id" : ObjectId("54613818d2ed2daf885a2041"), "id" : 112854, "name" : "Hales C
reek", "type" : "Stream", "country_name" : null, "height" : 247, "loc" : { "type
" : "Point", "coordinates" : [ -86.3905392, 34.0064863 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2052"), "id" : 112871, "name" : "Scharna
gle Branch", "type" : "Stream", "country_name" : null, "height" : 234, "loc" : {
 "type" : "Point", "coordinates" : [ -87.7705869, 34.3737074 ] } }
>

6
> db.geo_points.find({ loc: {$geoWithin: {$center: [  [ -90.5683234,31.985376],1
0/4]}}})

Result:
{ "_id" : ObjectId("54613818d2ed2daf885a202d"), "id" : 112833, "name" : "Bear Cr
eek", "type" : "Stream", "country_name" : null, "height" : 36, "loc" : { "type"
: "Point", "coordinates" : [ -88.2389162, 32.5143039 ] } }
{ "_id" : ObjectId("54613818d2ed2daf885a2047"), "id" : 112860, "name" : "Miles C
reek", "type" : "Stream", "country_name" : null, "height" : 28, "loc" : { "type"
 : "Point", "coordinates" : [ -88.2180652, 31.4265553 ] } }
>
