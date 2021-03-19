import math
import numpy as np


def normalize(store):
    for key in store:
        x_sum = sum(store[key][0])
        y_sum = sum(store[key][1])
        n = len(store[key][0])

        x_avg = float(x_sum / n)
        y_avg = float(y_sum / n)

        x_list = list()
        y_list = list()

        store[key] = (store[key][0] - x_avg, store[key][1] - y_avg)

        mx = max(abs(store[key][0]))
        my = max(abs(store[key][1]))

        m = max(mx, my)

        store[key] = (store[key][0] / m, store[key][1] / m)


def getDs(Ds, store):
    for key in store:
        D = 0
        n = len(store[key][0])

        for i in range(1, n):
            x1 = store[key][0][i]
            x0 = store[key][0][i - 1]
            y1 = store[key][1][i]
            y0 = store[key][1][i - 1]
            D += math.sqrt((y1 - y0) ** 2 + (x1 - x0) ** 2)

        Ds[key] = D


def getRepr(representatives, store, Ds, M):
    for key in store:
        prva_tocka = np.array([store[key][0][0], store[key][1][0]])
        n = len(store[key][0])
        tocke = list()

        for k in range(0, M):
            mini = np.inf
            index = np.array([])
            for i in range(0, n):
                tocka = np.array([store[key][0][i], store[key][1][i]])
                f = k * Ds[key] / (M - 1)
                distance = np.sqrt((prva_tocka[0] - tocka[0]) ** 2 + (prva_tocka[1] - tocka[1]) ** 2)
                if distance == f:
                    index = tocka
                    break
                elif abs(distance - f) < mini:
                    mini = abs(distance - f)
                    index = tocka
            tocke.append(index)

        representatives[key] = tocke
