# BearBot
*VERSION ALPHA 1.0.0.5*<br />
A bot for discord containing everything you see in need of a bot.
## Dependencies
The bot use the maven repositories for download all dependencies.<br />
But here are some links used in the project.

1. [JDA](https://github.com/DV8FromTheWorld/JDA)
2. [LavaPlayer](https://github.com/sedmelluq/lavaplayer)
3. [MySql Connector](https://mvnrepository.com/artifact/mysql/mysql-connector-java)

## DataBase Connection

If you use another database, change the driver to the desired one, and also include it in the maven dependencies.<br />
In this project we are using MYSQL, if you use the same just fill the fields with the connection data.<br />

Path from Connection class `BearBot/src/main/java/br/com/bearbot/DAO/ConnectionDAO.java`

```java
private final String URL = "<URL>";
private final String driver = "com.mysql.jdbc.Driver";
private final String user = "<USER>";
private final String password = "<PASSWORD";
private Connection connection;
```
