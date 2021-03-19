import numpy as np

eta = 0.2


def getArchitecture(arch):
    ar = list()
    for i in arch.split("x"):
        ar.append(int(i))
    return ar


def sigmoid(x, deriv=False):
    if deriv:
        return x * (1 - x)
    else:
        return 1 / (1 + np.exp(np.round(-x, 100)))


class Layer:
    def __init__(self, n_inputs, n_neurons):
        self.weights = np.random.randn(n_neurons, n_inputs) + 1.
        self.weights = self.weights.astype("float128")
        self.biases = np.random.randn(n_neurons, 1) + 1.
        self.biases = self.biases.astype("float128")

    def forward(self, inputs, function=None):
        if function is None:
            self.output = np.dot(self.weights, inputs) + self.biases
        else:
            self.output = function(np.dot(self.weights, inputs) + self.biases)


class NeuralNetwork:
    def __init__(self, architecture, dataset, outputs):
        self.outputs = outputs
        self.dataset = dataset
        self.architecture = getArchitecture(architecture)
        self.number_of_layers = len(self.architecture) - 1
        self.layers = list()
        self.y = list()
        n_inputs = len(self.dataset[0])

        for l in range(0, self.number_of_layers):
            if l == 0:
                self.layers.append(Layer(n_inputs, self.architecture[l + 1]))
            else:
                self.layers.append(Layer(self.architecture[l], self.architecture[l + 1]))


    def forward(self, dataset):
        l = 0
        for layer in self.layers:
            if l == 0:
                layer.forward(dataset, sigmoid)
            else:
                layer.forward(self.layers[l - 1].output, sigmoid)
            l += 1

        self.y.append(self.layers[l - 1].output)

    def mini_batch(self, n, iter):
        if n > len(self.dataset):
            raise Exception("Mini batch greater than dataset of length " + str(len(self.dataset)))

        it = 0
        while it < iter:
            print(it)
            s0 = 0
            n2 = n
            while s0 < n:
                X = np.array(self.dataset, dtype="float128")
                out = np.array(self.outputs, dtype="float128")

                output_delta = dict()
                for d in range(len(X)):
                    self.forward(np.array(X[d]))

                    for l in range(self.number_of_layers - 1, -1, -1):
                        output_delta[l] = 0
                        if l == self.number_of_layers - 1:
                            output_error = out[d] - self.y[d]
                            output_delta[l] = output_error * sigmoid(self.y[d], deriv=True)

                            self.layers[l].weights += eta * np.dot(output_delta[l], self.layers[l - 1].output.T)
                            self.layers[l].biases += eta * output_delta[l]
                        elif l == 0:
                            output_error = np.dot(self.layers[l + 1].weights.T, output_delta[l + 1])
                            output_delta[l] = output_error * sigmoid(self.layers[l].output, deriv=True)

                            self.layers[l].weights += eta * np.dot(output_delta[l], np.array(X[d]).T)
                            self.layers[l].biases += eta * output_delta[l]
                        else:
                            output_error = np.dot(self.layers[l + 1].weights.T, output_delta[l + 1])
                            output_delta[l] = output_error * sigmoid(self.layers[l].output, deriv=True)

                            self.layers[l].weights += eta * np.dot(output_delta[l], self.layers[l - 1].output.T)
                            self.layers[l].biases += eta * output_delta[l]
                s0 += n
                if n2 > len(self.dataset):
                    n2 = len(self.dataset)
                else:
                    n2 += n
                self.y = list()
            it += 1


    def backpropagation(self, iter):
        self.mini_batch(len(self.architecture), iter)


    def stohastic_backpropagation(self, iter):
        self.mini_batch(1, iter)

    def evaluate(self, X):
        self.y = list()
        self.forward(X)
        return self.y[0]
