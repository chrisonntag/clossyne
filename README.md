<h1>
<img align="left" height="90" style="margin: 0 21px 12px 0;" src="https://github.com/chrisonntag/clossyne/raw/main/clossyne/docs/clossyne_small.png"> WIP: Clossyne
</h1>

Clossyne – short for Cloud Storage System and named after Mnemosyne, the greek goddess 
of memory – is a distributed key-value storage based on binary search-tress. 
It can be easily accessed by a dictionary-based data access API and is written in Scala with the Akka framework 
incorporating the Actor model.

It allows for fast and efficient data storage and retrieval by utilizing a binary search tree structure and 
assigning each node of the tree to an individual actor.

## Installation and Usage
### TLDR;
Run the following command in terminal

```
docker run -v /tmp/clossyne:/tmp/clossyne -e CLOSSYNE_HOST="0.0.0.0" -p 4297:4297 christophsonntag/clossyne:1.0
```

This will pull a prepared image from the Docker Hub and run it. The log files will be stored in ```/tmp/closssyne```.

After that, navigate to ```clossyne-py/```, create a virtual environment and install the package: 

```
python3 -m venv env
source env/bin/activate

pip install setup.py
```

Import clossyne and use the ```Clossyne``` class in a with-context:

```
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.set("foo", "bar")
True
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.get("foo")
'bar'
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.get("foobar")
None
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.delete("foo")
True
>>> with clossyne.Clossyne('localhost', 4297) as c:
        c.get("foo")
None
```

Alternatively, download the [FraudTrain](https://www.kaggle.com/datasets/kartik2112/fraud-detection?resource=download&select=fraudTrain.csv) dataset and 
put it in ```test/data/```. You can run

```
python3 test.py
```

in order to perform the commands required for the Test Class Report. 
Please follow the README files in the respective directories for further information. 

## Disclaimer
This is a toy project, done for educational purposes – nothing more.




