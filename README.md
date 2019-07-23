# BearBot
*VERSION 4.1.0.1*<br />
A bot for discord containing everything you see in need of a bot.

## IDE
*This project used the eclipse IDE for development.*

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

## Token for yout bot
* Create your token here [Discord Developer Portal](https://discordapp.com/developers) <br />

*Class to change token*<br /> `BearBot/src/main/java/br/com/bearbot/utils/CONFIGS.java`

```java
public class CONFIGS {
    public static String TOKEN = "<Token here>";
    public static String PREFIX = "!bb";
}
```

## SQL Scripts for create data base

*Create table from guilds*
```sql
create table guildserver
(
    serverid varchar(200) not null
        primary key
);
```

*Create table from users*

```sql
create table users
(
    userid      varchar(200) not null,
    serverid    varchar(200) not null,
    msg_counter int(200)     not null,
    primary key (userid, serverid),
    constraint server_fk
        foreign key (serverid) references guildserver (serverid)
);
```
