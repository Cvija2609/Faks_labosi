import sys

from KanonicGeneration import *


# U program upisati redom datoteku iz koje se čita, zastavicu za elitizam (True, False), vjerojatnost mutacije
# veličinu populacije i broj iteracija

if len(sys.argv) != 6:
    raise Exception("Expected 5 arguments, found " + str(len(sys.argv)))

file = sys.argv[1]
if sys.argv[2] == "True":
    elitism = True
else:
    elitism = False
mutation_probability = float(sys.argv[3])
vel_pop = int(sys.argv[4])
max_iter = int(sys.argv[5])

dataset = read_from_file(file)

kg = KanonicGeneration(mutation_probability, vel_pop, max_iter, dataset, elitism)
kg.genetic_algorithm()
