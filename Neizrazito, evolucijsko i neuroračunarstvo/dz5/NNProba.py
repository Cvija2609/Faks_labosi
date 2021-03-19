import sys
from NeuralNetwork import *
import numpy as np


# Parametri programa je arhitektura NN oblika axbxcx...xz gdje su a,b,c,z elementi N

def f(x):
    return x ** 2


data = list()
for i in np.arange(-1, 1.2, 0.2):
    data.append((np.array([round(i, 2)]), np.array([round(f(i), 2)])))

arch = sys.argv[1]

nn = NeuralNetwork(arch, data)

nn.miniBatch(1, 60000)

nn.to_txt("weights.txt")

for d in data:
    print(d)
    print(nn.evaluate(d[0]))

print(np.array([0.5]))
print(nn.evaluate(np.array([0.5])))
