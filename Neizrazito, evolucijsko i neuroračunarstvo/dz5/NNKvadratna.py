import sys
from NeuralNetworks import *
import numpy as np


# Parametri programa je arhitektura NN oblika axbxcx...xz gdje su a,b,c,z elementi N

def f(x):
    return x ** 2


data = list()
outputs = list()
for i in np.arange(-1, 1.2, 0.2):
    data.append(np.array([[round(i, 2)]], dtype="float128"))
    outputs.append(np.array([[round(f(i), 2)]], dtype="float128"))

arch = sys.argv[1]

nn = NeuralNetwork(arch, data, outputs)

nn.mini_batch(5, 10000)

for d in data:
    print(d)
    print(nn.evaluate(np.array(d)))

print(np.array([0.5]))
print(nn.evaluate(np.array([[0.5]])))
