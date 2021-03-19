import sys
from KanonicElimination import *

# U program upisati redom datoteku iz koje se čita, vjerojatnost mutacije
# veličinu populacije, broj iteracija i mortalitet M

if len(sys.argv) != 6:
    raise Exception("Expected 5 arguments, found " + str(len(sys.argv)))

file = sys.argv[1]
mutation_probability = float(sys.argv[2])
vel_pop = int(sys.argv[3])
max_iter = int(sys.argv[4])
M = int(sys.argv[5])

dataset = read_from_file(file)

ke = KanonicElimination(mutation_probability, vel_pop, max_iter, dataset, M)
ke.genetic_algorithm()
