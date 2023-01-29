<h1>
<img align="left" height="30" src=""> Clossyne
</h1>

Clossyne – short for Cloud Storage System and named after Mnemosyne, the greek goddess 
of memory – is a distributed key-value storage based on binary search-tress. 
It can be easily accessed by a dictionary-based data access API and is written in Scala with the Akka framework 
incorporating the Actor model.

It allows for fast and efficient data storage and retrieval by utilizing a binary search tree structure and 
assigning each node of the tree to an individual actor.

## Building
To build Clossyne, you will need to have sbt installed. Once you have sbt, simply navigate to the root directory of the Clossyne project and run the following command:

```
sbt assembly
```

This will create a jar file in the target/scala-x.x/ directory that you can run to start the Clossyne service.

Alternatively, you can also build a Docker image of Clossyne by running:

```
sbt docker:publishLocal
```

## Running
To run Clossyne, you can execute the jar file that was created during the building process. By default, Clossyne will run on localhost and port 4297, but you can also specify the host and port number via environment variables CLOSSYNE_HOST and CLOSSYNE_PORT.

For example, to run Clossyne on "localhost" and port "4297", you can use the following command:

```
export CLOSSYNE_HOST=localhost
export CLOSSYNE_PORT=4297
java -jar target/scala-2.12/clossyne.jar
```

Alternatively, you can also run the Docker image of Clossyne by using the following command:

```
docker run -e CLOSSYNE_HOST=localhost -e CLOSSYNE_PORT=4297 -p 4297:4297 clossyne:latest
```

This will start the Clossyne service on the specified host and port, and make it accessible via the specified port on the Docker host.