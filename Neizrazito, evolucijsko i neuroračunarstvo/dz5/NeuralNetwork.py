from random import random
from Neuron import *
import ast

eta = 0.5


def rnd(min, max):
    return random() * (max - min + 1) + min


def toList(string):
    return ast.literal_eval(string)






def getArchitecture(arch):
    ar = list()
    for i in arch.split("x"):
        ar.append(int(i))
    return ar


def generateBiases(arch):
    biases = list()
    n = 0
    for i in arch[1:]:
        n += i
    for i in range(0, n):
        biases.append(random())


def generateStartingWeights(n):
    weights = list()

    for i in range(0, n):
        #               rnd(-1.2, 1.2)
        weights.append(rnd(-1.2, 1.2))

    weights = np.array([weights])

    return weights


class NeuralNetwork:
    def __init__(self, architecture, dataset):
        self.a = architecture
        self.architecture = getArchitecture(architecture)
        self.dataset = dataset
        self.neurons = dict()

        self.data = dataset[0]

        # Podaci (data) moraju biti oblika ([x1, x2, ..., xi], y)
        if len(self.data[0]) != self.architecture[0]:
            raise Exception("Wrong number of data " + str(len(self.data)) + " for architecture " + self.a)

        self.initialize()

    def add_neuron(self, neuron, sloj):
        if not sloj in self.neurons.keys():
            self.neurons[sloj] = list()

        self.neurons[sloj].append(neuron)

    def initialize(self):
        for i in range(0, len(self.architecture)):
            if i == 0:
                for j in range(0, self.architecture[i]):
                    neuron = Neuron(0., np.array([[1.]]), np.array([self.data[0][j]]), adaline)
                    self.add_neuron(neuron, i)
            else:
                for j in range(0, self.architecture[i]):
                    xes = list()
                    for neuro in self.neurons[i - 1]:
                        xes.append(neuro.net)

                    xes = np.array([xes])
                    #               rnd(-1.2, 1.2)
                    neuron = Neuron(rnd(-5.2, 5.2), generateStartingWeights(self.architecture[i - 1]), xes, sigmoid)
                    self.add_neuron(neuron, i)

    def compute(self, sloj):
        y = list()

        for neuron in self.neurons[sloj]:
            y.append(neuron.net)

        return y

    def getTezine(self, k):
        w = list()

        for neuron in self.neurons[k]:
            w.append(neuron.tezine)

        return w

    def miniBatch(self, mini_batch, iter):
        slojevi = len(self.architecture)
        if mini_batch > len(self.dataset):
            raise Exception("Mini batch higher than length of dataset")

        for z in range(0, iter):
            print(z)
            s0 = 0
            mb = mini_batch
            condition = True
            while condition:
                N = 0
                N_pogreske = dict()
                N_izlazi = dict()

                for data in self.dataset[s0:mb]:
                    N_pogreske[N] = list()
                    N_izlazi[N] = list()
                    izlazi = dict()
                    izlazi[0] = list()

                    nrn = 0
                    for neuron in self.neurons[0]:
                        neuron.set_data(np.array(data[0][nrn]))
                        izlazi[0].append(neuron.net)
                        nrn += 1

                    for i in range(1, slojevi):
                        izlazi[i] = list()
                        for neuron in self.neurons[i]:
                            newData = list()
                            for neuro in self.neurons[i - 1]:
                                newData.append(neuro.net)
                            newData = np.array([newData])

                            neuron.set_data(newData)
                            izlazi[i].append(neuron.net)

                    pogreske = dict()

                    for k in range(len(izlazi.keys()) - 1, -1, -1):
                        pogreske[k] = list()

                        if k == 0:
                            pogreske[k].append(0.)
                            continue

                        if k == len(izlazi.keys()) - 1:
                            os = self.compute(k)
                            a8 = 0
                            for o in os:
                                pogreske[k].append(o * (1 - o) * (data[-1][a8] - o))
                                a8 += 1
                        else:
                            ys = self.compute(k)
                            g = 0
                            for y in ys:
                                downstream = 0
                                w = self.getTezine(k + 1)
                                d = 0
                                for delta in pogreske[k + 1]:
                                    downstream += delta * w[d][0][g]
                                    d += 1
                                g += 1
                                pogreske[k].append(y * (1 - y) * downstream)

                    N_pogreske[N].append(pogreske)
                    N_izlazi[N].append(izlazi)
                    N = N + 1

                for k in range(1, slojevi):
                    j = 0
                    for neuron in self.neurons[k]:
                        suma = 0
                        suma0 = 0
                        #for j in range(0, len(neuron.tezine[0])):
                        for i in range(0, N):
                            for p in range(0, len(self.neurons[k - 1])):
                                yi = N_izlazi[i][0][k - 1][p]
                                deltai = N_pogreske[i][0][k][j]
                                suma += yi * deltai
                                suma0 += deltai

                        for p in range(0, len(self.neurons[k - 1])):
                            neuron.set_weight(neuron.tezine[0][p] + eta * suma, p)
                            neuron.set_bias(neuron.bias + eta * suma0)
                        j += 1
                        # neuron.tezine[0][j] += eta * suma
                        # neuron.bias += eta * suma0

                condition = mb < len(self.dataset)

                mb += mini_batch
                s0 += mini_batch

                if condition and mb > len(self.dataset):
                    mb = len(self.dataset)

    def evaluate(self, data):
        slojevi = len(self.architecture)
        izlazi = list()

        k = 0
        for neuron in self.neurons[0]:
            neuron.set_data(np.array([data[k]]))
            k += 1

        for i in range(1, slojevi):
            for neuron in self.neurons[i]:
                newData = list()
                for neuro in self.neurons[i - 1]:
                    newData.append(neuro.net)
                newData = np.array([newData])

                neuron.set_data(newData)

                if i == slojevi - 1:
                    izlazi.append(neuron.net)

        return izlazi

    def to_txt(self, filename):
        slojevi = len(self.architecture)

        file1 = open(filename, "w")

        for k in range(0, slojevi):
            i = 0
            L = "["
            for neuron in self.neurons[k]:
                j = 0
                L += "["
                for w in neuron.tezine[0]:
                    if j == len(neuron.tezine[0]) - 1:
                        L += str(float(w)) + "]"
                    else:
                        j += 1
                        L += str(float(w)) + ", "

                if i == self.architecture[k] - 1:
                    continue
                else:
                    i += 1
                    L += ", "

            L += "]\n"
            file1.writelines(L)

        file1.writelines("\n")

        for k in range(0, slojevi):
            i = 0
            L = "["
            for neuron in self.neurons[k]:
                L += "["

                if i == self.architecture[k] - 1:
                    L += str(neuron.bias) + "]"
                    continue
                else:
                    i += 1
                    L += str(neuron.bias) + "], "

            L += "]\n"
            file1.writelines(L)

        file1.close()

    def from_txt(self, filename):
        k = 0

        with open(filename) as f:
            content = f.readlines()
        content = [x.strip() for x in content]

        bias_time = False

        for line in content:
            if line == "":
                bias_time = True
                k = 0
                continue
            if not bias_time:
                a = toList(line)
                for i in range(0, len(a)):
                    for j in range(0, len(a[i])):
                        self.neurons[k][i].set_weight(np.array([a[i][j]]), j)
                k += 1
            else:
                a = toList(line)
                for i in range(0, len(a)):
                    for j in range(0, len(a[i])):
                        self.neurons[k][i].set_bias(a[i][j])
                k += 1

    def stohastic(self, iterations):
        self.miniBatch(1, iterations)

    def backpropagation(self, iterations):
        self.miniBatch(len(self.dataset) - 1, iterations)