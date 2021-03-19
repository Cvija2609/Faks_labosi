from abc import ABC

from GeneticAlgorithm import *


def getSample(r, q):
    if q[0] >= r:
        return 0
    for i in range(1, len(q)):
        if q[i - 1] <= r <= q[i]:
            return i


class KanonicGeneration(GeneticAlgorithm, ABC):
    def __init__(self, mp, vp, max_iter, dataset, elitism=False, alpha=0.5):
        super(KanonicGeneration, self).__init__(mp, vp, max_iter, dataset)
        self.elitism = elitism
        self.alpha = alpha

    # dataset je lista u kojoj je lista podataka u obliku [[x1, y1, f1], [x2, y2, f2], ...]
    def genetic_algorithm(self):
        generation = 0
        populacija = GeneticAlgorithm.generate(self.vel_pop)
        best = self.getBest(populacija)
        printBest(best, generation, populacija)
        for i in range(0, self.max_iter):
            print("Generacija: " + str(generation))
            currentBest = self.getBest(populacija)
            if currentBest[0] > best[0]:
                printBest(currentBest, generation, populacija)
                best = currentBest
            generation += 1
            selected = self.selection(populacija)
            if selected is None:
                break
            crossed = self.crossing(selected)
            self.mutation(crossed)
            populacija = crossed.copy()
        print()
        best = self.getBest(populacija)
        # Ukupna tocnost je kvadratna pogreska
        print("Najbolje rjesenje je: " + str(populacija[best[1]]) + " s ukupnom tocnoscu " + str(
            1 / best[0]) + " Kvalitete: " + str(best[0]))

    def selection(self, population):
        retPop = list()
        elitism = self.elitism
        D = 0
        p = list()
        q = list()
        fitness = list()

        for betas in population:
            a = self.fitness(betas)
            if a == 0:
                return None
            D += a
            q.append(D)
            fitness.append(a)

        avgD = float(D / self.vel_pop)

        for a in fitness:
            p.append(a / D)  # najmanja vrijednost, najveća dobrota

        if elitism:  # Ako je tražen elitizam, najboljeg ubaci u listu odabranih
            np = 0
            index = 0
            for i in range(0, len(p)):
                if p[i] > np:
                    np = p[i]
                    index = i

            retPop.append(population[index])

        k = 0
        while len(retPop) != (len(population) / 2) + 1:
            r = randomNumber(0, D)
            beta = getSample(r, q)
            if population[beta] in retPop and k < 100:  # Ako je odabrani već u skupu odabranih, odaberi drugog
                k += 1
                continue
            k = 0
            retPop.append(population[beta])

        return retPop

    # Funkcija dobrote je prosječna vrijednost izlaza funkcije za dobivene bete
    # minus izlaz koji je poslan iz datoteke ^ 2
    def fitness(self, betas):
        suma = 0
        for data in self.dataset:
            x = data[0]
            y = data[1]
            izlaz = data[2]

            # Funkcija koja je uzeta za računanje dobrote je (dobiveni - očekivani izlaz) ^ -2
            # iz toga se za najveća odstupanja dobije najmanja vrijednost što je dobro
            # jer na taj način najmanja odstupanja će imati najveću vjerojatnost
            try:
                suma += (1 / (GeneticAlgorithm.f(x, y, betas) - izlaz) ** 2)
            except ZeroDivisionError:
                return 0

        return float(suma / len(self.dataset))

    # BLX - alpha
    def crossing(self, selected):
        retPop = list()
        selLen = len(selected)

        if self.elitism:
            retPop.append(selected[0])  # Jer ako je uključen elitizam, najbolji je uvijek prvi izabran kod selekcije

        while len(retPop) != self.vel_pop:
            prvi = random.randint(0, selLen - 1)
            drugi = random.randint(0, selLen - 1)
            while prvi == drugi:
                drugi = random.randint(0, selLen - 1)
            bete1 = selected[prvi]
            bete2 = selected[drugi]

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

            retPop.append(bet)

        return retPop

    def mutation(self, crossed):
        for beta in crossed:
            for i in range(0, len(beta)):
                if random.random() < self.mutation_probability:
                    a = beta[i] - 1
                    b = beta[i] + 1
                    if a < GeneticAlgorithm.dg:
                        a = GeneticAlgorithm.dg
                    if b > GeneticAlgorithm.gg:
                        b = GeneticAlgorithm.gg

                    beta[i] = random.uniform(a, b)

    def getBest(self, populacija):
        bestfitness = 0
        i = 0
        index = 0
        for beta in populacija:
            curFitness = self.fitness(beta)
            if curFitness > bestfitness:
                bestfitness = curFitness
                index = i
            i += 1

        return bestfitness, index
