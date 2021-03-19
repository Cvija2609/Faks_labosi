from NeuralNetworks import *
import sys

arch = sys.argv[1]

data = [np.array([[0.], [0.]], dtype="float128"), np.array([[0.], [1.]], dtype="float128"), np.array([[1.], [0.]], dtype="float128"), np.array([[1.], [1.]], dtype="float128")]
outputs = [np.array([0.], dtype="float128"), np.array([1.], dtype="float128"), np.array([1.], dtype="float128"), np.array([0.], dtype="float128")]

nn = NeuralNetwork(arch, data, outputs)

nn.mini_batch(1, 10000)

for d in data:
    print(d)
    print(nn.evaluate(np.array(d)))