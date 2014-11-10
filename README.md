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

