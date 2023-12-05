# Environment - Dockerized local deployment

The local deployment environment. which is configured in the **docker-compose.yml** file, contains the following docker containers:
- ***Zookeeper***
- ***Kafka***
- ***Kafka UI***
- ***Debezium***
- ***MongoDB Replica Set (composed of 2 instances)***

All services run on the same bridge network, named **ms-network**.

## MongoDB Replica Set
The replica set is composed of 2 instances of MongoDB v5.0.22. The 1<sup>st</sup> instance (mongodb1) runs on the
default port 27017 while the 2<sup>nd</sup> instance (mongodb2) runs on 27018.
Both ports are exposed on the host. In order to be able to connect from Studio 3T to the replica set, it is mandatory to
add the names of the 2 mongo services to the hosts file:

*127.0.0.1 mongodb1 mongodb2*

The location of the hosts file is :

**Windows** - *"C:\Windows\System32\Drivers\etc\hosts"*

**Mac & Linux** - *"/etc/hosts"*

Both containers have a healthcheck definition, in order to be fully initialized before Debezium.

## Kafka & Zookeeper

Even though Kafka can run without Zookeeper since version 3.2.0, Debezium requires a Zookeeper installation alongside
Kafka.
The environment uses Zookeeper v3.9.1 and Kafka v3.6.0. Kafka runs on port 9093 (external access) and 9092 for inter
container communication, while Zookeeper runs on 2181.
9093 and 2181 ports are exposed on the host.
Both services have healthcheck definitions, in order for both of them to be fully initialized before Debezium.

## Debezium

The environment uses Debezium v2.4.0.Final. Since Debezium uses Kafka Connect compatible connectors to connect databases
to Kafka, it means that it should be the last service that gets initialized.
Debezium runs on port 8083. Debezium also has a healthcheck definition that is used by Kafka UI to know when to start.
While there is no direct relationship between Kafka UI and Debezium, this helps us know when the environment is fully
initialized, as Debezium takes longer to start than all other services.
After the Debezium is fully initialized, it is mandatory that the MongoDb connector configuration is added to Debezium.
A POST request with the contents of the **mongo-db-connector.json** file needs to executed on *
*http://localhost:8083/connectors/**
## Kafka UI
Kafka UI is the last service that gets initialized. It runs on port 8080, which is also exported on the host. 


## Using the bash scripts

In order to use start/stop/remove scripts, you must be able to execute bash scripts. This means that
on Windows, you need to add git bash to PATH environment variable (path entry should be something like "C:\Program Files\Git\bin").
The scripts need docker-compose v2 in order to work, so please make sure you have docker-compose v2 activated.
The scripts were tested on docker v24.0.2 with docker-compose v2.19.1.

To start the environment run the sh script "**start.sh**"

To stop the environment run the sh script "**stop.sh**"

To remove the environment run the sh script "**remove.sh**"

## Starting the environment for the first time
1. Execute the **start.sh** (*sh start.sh*) script. All the images will be downloaded and containers will be created. If everything is smooth, you will see the following output:
    ```
    Starting environment...
   [+] Running 7/7
   ✔ Network ms-network   Created                                                                                                                                        0.0s
   ✔ Container mongodb1   Healthy                                                                                                                                       12.0s
   ✔ Container zookeeper  Healthy                                                                                                                                       22.7s
   ✔ Container kafka      Healthy                                                                                                                                       22.7s
   ✔ Container mongodb2   Healthy                                                                                                                                       11.9s
   ✔ Container debezium   Healthy                                                                                                                                       52.6s
   ✔ Container kafka-ui   Started                                                                                                                                       52.7s
   Waiting for Kafka UI...........
   Environment started
    ```
2. Create a replica set connection in Studio 3T. The replica set name should be **local-mongodb-replica-set** and the members should be **mongodb1:27017** and **mongodb2:27018**. Add the name of the replica set as the connection name also. If everything is smooth, you should be able to connect to the replica set and see the 2 members.
3. Use Postman to Execute a POST request at **http://localhost:8083/connectors/** with the body containing the json configuration defined in the **mongo-db-connector.txt** file.

## Stopping the environment
Execute the **stop.sh** (*sh stop.sh*) script. If everything is smooth, you will see the following output:
   ```
   Stopping environment...
   [+] Stopping 6/6
    ✔ Container kafka-ui   Stopped                                                                                                                                        2.2s 
    ✔ Container debezium   Stopped                                                                                                                                        0.6s 
    ✔ Container mongodb2   Stopped                                                                                                                                       10.2s 
    ✔ Container kafka      Stopped                                                                                                                                        1.0s 
    ✔ Container zookeeper  Stopped                                                                                                                                        0.5s 
    ✔ Container mongodb1   Stopped                                                                                                                                       10.2s 
   Environment stopped
   ```

## Removing the environment
Execute the **remove.sh** (*sh remove.sh*) script. This script stops the environment if it is started, removing the containers afterwards. If everything is smooth, you will see the following output:
   ```
   Are you sure you want to remove the environment ? (y/n) y
   Removing environment...
   ms-network
   Going to remove kafka-ui, debezium, mongodb2, kafka, zookeeper, mongodb1
   [+] Removing 6/0
    ✔ Container mongodb2   Removed                                                                                                                                        0.0s 
    ✔ Container zookeeper  Removed                                                                                                                                        0.0s 
    ✔ Container kafka      Removed                                                                                                                                        0.0s 
    ✔ Container debezium   Removed                                                                                                                                        0.0s 
    ✔ Container kafka-ui   Removed                                                                                                                                        0.0s 
    ✔ Container mongodb1   Removed                                                                                                                                        0.0s 
   Environment removed
   ```

## Known issues
* Sometimes, for no apparent reason, Debezium hangs at startup. Stop the environment using **stop.sh** script and try to increase the healthcheck interval for Debezium service. As a last resort, recreate the environment by running **remove.sh** and **start.sh** scripts, one after another.

