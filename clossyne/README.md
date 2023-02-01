<h1>
<img align="left" height="90" style="margin: 0 21px 12px 0;" src="https://github.com/chrisonntag/clossyne/raw/main/clossyne/docs/clossyne_small.png"> Clossyne
</h1>

Clossyne – short for Cloud Storage System and named after Mnemosyne, the greek goddess 
of memory – is a distributed key-value storage based on binary search-tress. 
It can be easily accessed by a dictionary-based data access API and is written in Scala with the Akka framework 
incorporating the Actor model. This allows it to be completely asynchronous, concurrent, event-driven and distributed.

It allows for fast and efficient data storage and retrieval by utilizing a binary search tree structure and 
assigning each node of the tree to an individual actor.

## Building
To build Clossyne, you will need to have sbt installed. Once you have sbt, simply navigate to the root directory of the Clossyne project and run the following command:

```
sbt compile
sbt package
```

This will create a jar file in the target/scala-2.13/ directory that you can run to start the Clossyne service.

However, it is recommended to build a Docker image of Clossyne by running:

```
sbt docker:publishLocal
```

Alternatively, a prepared image will be pulled from the Docker registry, if you don't want to build it yourself. 

## Running
To run Clossyne, you can run the Docker image of Clossyne by using the following command:

```
docker run -v /tmp/clossyne:/tmp/clossyne -e CLOSSYNE_HOST="0.0.0.0" -p 4297:4297 christophsonntag/clossyne:1.0
```

This will start the Clossyne service (and pull it before) on the specified host and port, and make it accessible via the specified port on the Docker host.  

By default, Clossyne will run on localhost and port 4297, but you can also specify the host and port number via environment variables CLOSSYNE_HOST and CLOSSYNE_PORT.

For example, to run Clossyne on "localhost" and port "4297", you can use the following command:

```
export CLOSSYNE_HOST=localhost
export CLOSSYNE_PORT=4297
scala target/scala-2.13/clossyne_2.13-1.0.jar
```

## Logging
The log files of the server can then be found in ```/tmp/clossyne/clossyne-test.log```
