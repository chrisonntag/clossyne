import os
import socket
import logging


class Clossyne:
    """
    Main class for connecting and sending data to the root server.
    The command and the arguments are separated by a colon, while arguments 
    are separated by a whitespace. Since the key cannot include whitespaces, only 
    the first detected whitespace will be used as a delimiter. 
    Commands:
        SET     Set a key, value pair.
        GET     Get a value from a key.
        DEL     Delete a key, value pair with a given key.
        RNG     Get all key, value pairs in a certain key range.
        EXT     Close the connection.
    """
    def __init__(self, host, port):
        self.logger = logging.getLogger(__name__)
        self.sock = None
        self.connect(host, port)

    def __enter__(self):
        return self

    def connect(self, host, port):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.connect((host, port))
        self.logger.debug(f"Connection established: {host}:{port}")

    def disconnect(self):
        self.send("EXT")
        self.sock.close()
        self.sock = None

    def handle_response(self, command, response):
        if (command == "GET" or command == "RNG"):
            # OperationResult event
            return None if (response == "NS" or response == "NIL") else response
        else:
            # OperationFinished event
            return response == "OK"

    def send(self, command: str, *args):
        command = command.upper()
        if args:
            arguments = " ".join(list(args))
            command += f":{arguments}"

        try:
            # sendall guarantees that all the data is sent before returning
            self.sock.sendall(str.encode(command))
            response = self.sock.recv(1024).decode()

            return self.handle_response(command[:3], response)
        except socket.error as e:
            self.logger.error("Connection error", exc_info=1)
            print("Error sending command.")

    def get(self, key, key2=None):
        if key2 == None:
            return self.send("GET", key)
        else:
            """
            Range query range(k1, k2): Support for accessing all elements in the
            range of two keys: e.g. all names in alphabetic order between “Benkner” and “Koehler”.
            """
            return self.send("RNG", key, key2)

    def set(self, key, value):
        return self.send("SET", key, value)

    def delete(self, key):
        return self.send("DEL", key)

    def __exit__(self, exc_type, exc_value, traceback):
        self.disconnect()

