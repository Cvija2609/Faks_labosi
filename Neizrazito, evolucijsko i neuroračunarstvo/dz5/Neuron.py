from math import exp
import numpy as np


def sigmoid(x):
    return 1 / (1 + exp(-x))


def adaline(x):
    return x


class Neuron:
    def __init__(self, bias, tezine, podaci, prijenosna_funkcija):
        self.bias = bias
        self.tezine = tezine
        self.podaci = podaci
        self.prijenosna_funkcija = prijenosna_funkcija

        n = len(podaci)
        if n != len(tezine):
            raise Exception("Number of data and weights must be the same, not " + str(n) + " != " + str(len(tezine)))

        suma = np.dot(tezine, np.transpose(podaci)) + bias

        self.net = self.prijenosna_funkcija(suma)

    def set_data(self, data):
        self.podaci = data
        suma = np.dot(self.tezine, np.transpose(self.podaci)) + self.bias
        suma = round(float(suma), 100)
        suma = np.array([suma])
        self.net = float(self.prijenosna_funkcija(suma))

    def set_weight(self, value, index):
        self.tezine[0][index] = value

    def set_bias(self, value):
        self.bias = value
