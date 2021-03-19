from abc import ABC, abstractmethod
from math import sin, cos, exp
import random


def fileCheck(file):
    try:
        open(file, "r")
        return True
    except IOError:
        return False


def read_from_file(file):
    if not fileCheck(file):
        raise FileExistsError("Error while opening " + str(file))

    with open(file) as f:
        lines = []
        for line in f:
            a = list()
            for b in line.strip().split("\t"):
                a.append(float(b))
            lines.append(a)

    return lines


def randomNumber(min, max):
    return random.random() * (max - min) + min


def printBest(best, generation, populacija):
    print("Najbolji: " + str(populacija[best[1]]) + " Generacija: " + str(generation) + " Kvaliteta: " + str(best[0]))


class GeneticAlgorithm(ABC):
    dg = -4
    gg = 4

    def __init__(self, mutation_probability, vel_pop, max_iter, dataset):
        self.mutation_probability = mutation_probability
        self.vel_pop = vel_pop
        self.max_iter = max_iter
        self.dataset = dataset

    @abstractmethod
    def genetic_algorithm(self):
        pass

    @abstractmethod
    def fitness(self, betas):
        pass

    @abstractmethod
    def selection(self, population):
        pass

    @abstractmethod
    def mutation(self, crossed):
        pass

    @abstractmethod
    def crossing(self, selected):
        pass

    @staticmethod
    def f(x, y, beta):
        if not isinstance(beta, list) or len(beta) != 5:
            raise TypeError("Beta is not list with 5, but with " + str(len(beta)) + " parameters")

        try:
            ans = sin(beta[0] + beta[1] * x) + beta[2] * cos(x * (beta[3] + y)) * float(1 / (1 + exp((x - beta[4]) ** 2)))
        except OverflowError:
            ans = float('inf')

        return ans

    @staticmethod
    def generate(vel_pop):
        population = list()
        for i in range(0, vel_pop):
            bete = list()
            for j in range(0, 5):
                bete.append(randomNumber(GeneticAlgorithm.dg, GeneticAlgorithm.gg))
            population.append(bete)

        return population

    @abstractmethod
    def getBest(self, population):
        pass
