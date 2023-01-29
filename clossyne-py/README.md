<h1>
<img align="left" height="90" style="margin: 0 21px 12px 0;" src="https://github.com/chrisonntag/clossyne/raw/main/clossyne/docs/clossyne_small.png"> Clossyne
</h1>
Clossyne is a distributed key-value store built using Akka and Scala and named after Mnemosyne, the Greek goddess 
of memory, who represents the idea of memory and recall. 
The Clossyne Python Client allows users to interact with the Clossyne server using a simple Python API.

## Installation
The Clossyne Python Client can be installed using the included setup.py wheel. To install using pip, run the following command:

```
pip install setup.py
```

## Usage
### Basic Example
To interact with the Clossyne server, simply import the Clossyne module and create a new Clossyne object. The object should be used within a with context so that the connection is closed when the context ends.

Here is a basic example of setting a key-value pair and retrieving its value:

```
>>> import clossyne
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.set("hello", "world")
        if (c.set("hello", "world")):
            print(c.get("hello"))
'world'
```

```
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.set("foo", "bar")
True
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.get("foo")
'bar'
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.get("foobar")
False
```

## Commands
SET: Inserts a key-value pair into the Clossyne store. Returns True if the insert was successful, False otherwise.
GET: Retrieves the value associated with a given key. Returns False if the key does not exist.
DEL: Deletes a key-value pair from the Clossyne store. Returns True if the deletion was successful, False otherwise.
RNG: Retrieves a range of key-value pairs from the Clossyne store.
EXT: Closes the connection to the Clossyne server. Sent automatically when the with context ends.
