import os
import clossyne
import csv
from time import sleep


class DataReader:
    def __init__(self, filename):
        self.file = open(filename, 'r')
        self.csvreader = csv.reader(self.file, delimiter=',', quotechar='"')
        self.headers = next(self.csvreader)

    def next(self):
        return next(self.csvreader)

    def close(self):
        self.file.close()


class TestClass:
    def __init__(self, data_file):
        self.reader = DataReader(data_file)

    def get_next_pair(self):
        row = self.reader.next()
        key = row[0]
        value = str({k:v for (k, v) in zip(self.reader.headers, row)})
        return key, value

    def perform_delete_leaf(self):
        with clossyne.Clossyne('0.0.0.0', 4297) as c:
            key, value = self.get_next_pair()
            if (c.set(key, value)):
                print("Value has been saved")
                saved_value = c.get(key)
                print("GET ", key, ": ", saved_value)
                print("Delete ", key)
                print(c.delete(key))
                print("GET ", key, ": ", c.get(key))

    def perform_delete_child(self):
        with clossyne.Clossyne('localhost', 4297) as c:
            print(c.set("a", "val"))
            print(c.set("b", "value"))
            print(c.delete("a")) 
            print(c.get("a")) 
            print(c.get("b")) 

    def perform_delete_children(self):
        with clossyne.Clossyne('localhost', 4297) as c:
            print(c.set("d", "val"))
            print(c.set("b", "value"))
            print(c.set("a", "value"))
            print(c.set("c", "value"))
            print(c.set("f", "value"))
            print(c.set("e", "value"))
            print(c.set("g", "value"))
            print(c.delete("d")) 
            print(c.get("d")) 
            print(c.get("b")) 

        with clossyne.Clossyne('localhost', 4297) as c:
            print(c.get("d")) 
            print(c.get("b")) 

    def perform_dataset(self, num):
        with clossyne.Clossyne('0.0.0.0', 4297) as c:
            for i in range(0, num):
                key, value = self.get_next_pair()
                c.set(key, value)
            print("Delete Standing: ", c.delete("Standing"))
            print("Delete Mike: ", c.delete("Mike"))
            print("Get Ogren: ", c.get("Ogren"))
            print("Get Bergman: ", c.get("Bergman"))
            print("Get Nadia: ", c.get("Nadia"))


    def clean(self):
        self.reader.close()
        

if __name__ == '__main__':
    test_class = TestClass('test/data/names.csv')
    test_class.perform_dataset(400)
    test_class.clean()
    
