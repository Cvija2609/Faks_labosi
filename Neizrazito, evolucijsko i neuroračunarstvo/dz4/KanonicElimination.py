from abc import ABC

from GeneticAlgorithm import *

epsilon = 1e-10


class KanonicElimination(GeneticAlgorithm, ABC):
    def __init__(self, mutation_probability, vel_pop, max_iter, dataset, M, k=3, alpha=0.5):
        super().__init__(mutation_probability, vel_pop, max_iter, dataset)
        self.M = M
        self.k = k
        self.alpha = alpha

    def genetic_algorithm(self):
        generation = 0
        populacija = GeneticAlgorithm.generate(self.vel_pop)
        best = self.getBest(populacija)
        printBest(best, generation, populacija)
        for i in range(0, self.max_iter):
            print("Generacija: " + str(generation))
            currentBest = self.getBest(populacija)
            if currentBest[0] < best[0]:
                printBest(currentBest, generation, populacija)
                best = currentBest
            if best[0] < epsilon:
                break
            generation += 1
            self.selection(populacija)
            self.crossing(populacija)
            self.mutation(populacija)

        print()
        best = self.getBest(populacija)
        # Ukupna tocnost je kvadratna pogreska
        print("Najbolje rjesenje je: " + str(populacija[best[1]]) + " s ukupnom tocnoscu " + str(best[0]))

    def fitness(self, betas):
        suma = 0
        for data in self.dataset:
            x = data[0]
            y = data[1]
            izlaz = data[2]

            suma += (GeneticAlgorithm.f(x, y, betas) - izlaz) ** 2

        return float(suma / len(self.dataset))

    def getBest(self, populacija):
        bestfitness = self.fitness(populacija[0])
        i = 0
        index = 0
        for beta in populacija:
            curFitness = self.fitness(beta)
            if curFitness < bestfitness:
                bestfitness = curFitness
                index = i
            i += 1

        return bestfitness, index

    def selection(self, populacija):
        D = 0
        fitness = list()

        for beta in populacija:
            c = self.fitness(beta)
            D += c
            fitness.append(c)

        for i in range(0, self.M):
            ks = list()
            # Izaberi k jedinki
            while len(ks) != self.k:
                r = random.randint(0, len(populacija) - 1)
                if r in ks:
                    continue
                ks.append(r)
            worst = self.getWorst(ks, fitness)
            populacija.remove(populacija[worst])
            fitness.remove(fitness[worst])

    def getWorst(self, elementi, fitness):
        worst = elementi[0]
        for i in elementi:
            if fitness[i] > fitness[worst]:
                worst = i

        return worst

    # BLX - alpha
    def crossing(self, population):
        popLen = len(population)

        while popLen != self.vel_pop:
            prvi = random.randint(0, popLen - 1)
            drugi = random.randint(0, popLen - 1)
            while prvi == drugi:
                drugi = random.randint(0, popLen - 1)
            bete1 = population[prvi]
            bete2 = population[drugi]

            bet = list()
            for i in range(0, len(bete1)):
                cimin = min(bete1[i], bete2[i])
                cimax = max(bete1[i], bete2[i])
                Ii = cimax - cimin
                a = cimin - Ii * self.alpha
                b = cimax + Ii * self.alpha
                if a < GeneticAlgorithm.dg:
                    a = GeneticAlgorithm.dg
                if b > GeneticAlgorithm.gg:
                    b = GeneticAlgorithm.gg
                bet.append(random.uniform(a, b))

            population.append(bet)
            popLen = len(population)

    def mutation(self, population):
        for beta in population:
            for i in range(0, len(beta)):
                if random.random() < self.mutation_probability:
                    a = beta[i] - 1
                    b = beta[i] + 1
                    if a < GeneticAlgorithm.dg:
                        a = GeneticAlgorithm.dg
                    if b > GeneticAlgorithm.gg:
                        b = GeneticAlgorithm.gg

                    beta[i] = random.uniform(a, b)
