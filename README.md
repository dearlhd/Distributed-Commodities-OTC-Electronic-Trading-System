# Distributed-Commodities-OTC-Electronic-Trading-System
Project of course EIS of SE in SJTU, cooperated with Morgan Stanley.

# Intro
    It implements a system of OTC commodities trading, 
    which contains two subsystem, trader gateway and broker gateway.
    The trader gateway is a subsystem in which trader can broswer market info, 
         deliver different types of orders, query order blotters and so on.
    The broker gateway is a subsystem where deal with the orders posted from trader gateway,
        including make a match of orders and store unfinished orders.
    
# System Architectrue
    Both of the subsystem are implemented in RESTful style.
    Subsystem exchange data by http request.
    The trader gateway is implemented with springMVC + spring + hibernate.
    The broker gateway is implemented with springMVC + spring + mybatis.
    
# Relative Technology
    Framework: spring, hibernate, mybatis
    Database: mysql, redis
    Others: apache-activemq
