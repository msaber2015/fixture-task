
# Fixture Project

This project read all data from source a and b from Fixture server
and categorize the data between joined and orphaned and defective 
then psh this data to sink Api on Fixture server.


## Installation

you need to below to run this spring boot app

- Java: 17
- Spring boot: 3.2.1
- Fixture Server (Running) 

    
## Running

- Run this Spring boot app
- Call API [http://localhost:8080/sync]


## API Reference

#### our custom api to proccess data
```http
  POST http://localhost:8080/sync
```
#### Get data from source a

```http
  GET http://localhost:7299/source/a
```
#### Get data from source b

```http
  GET http://localhost:7299/source/b
```
#### push data to fixture server
```http
  POST http://localhost:7299/sink/a
```


## Authors

- [@mohamed.sleem] https://github.com/msaber2015/fixture-task

