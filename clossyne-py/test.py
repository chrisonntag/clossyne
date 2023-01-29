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

    def perform(self):
        with clossyne.Clossyne('localhost', 4297) as c:
            key, value = self.get_next_pair()
            if (c.set(key, value)):
                saved_value = c.get(key)
                print(saved_value)

    def clean(self):
        self.reader.close()
        

if __name__ == '__main__':
    test_class = TestClass('test/data/names.csv')
    test_class.perform()
    test_class.clean()
    
