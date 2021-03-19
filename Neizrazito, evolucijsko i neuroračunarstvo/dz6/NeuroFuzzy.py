import sys
from math import cos
from random import uniform

import numpy as np
# import matplotlib
from mpl_toolkits import mplot3d
import matplotlib.pyplot as plt


def to_file(primjeri, filename):
    f = open(filename, "w")
    for pr in primjeri:
        f.write(str(pr[0][0]) + " " + str(pr[0][1]) + " " + str(pr[1]) + "\n")


def make_neuro(izlazi, primjeri):
    ret = list()
    n = len(primjeri)
    for i in range(0, n):
        ret.append(((primjeri[i][0][0], primjeri[i][0][1]), izlazi[i]))

    return ret


def make_mi(x, primjeri, param=0):
    ret = list()
    n = len(primjeri)
    for i in range(n):
            ret.append((primjeri[i][0][param], *x[i]))

    return ret


def to_file_mi(x, filename, param=0):
    f = open(filename, "w")
    n = len(x)

    if param == 0:
        for pr in x:
            for i in pr:
                f.write(str(i) + " ")
            f.write("\n")
    else:
        for j in range(9):
            for i in range(j, n, 9):
                for j in x[i]:
                    f.write(str(j) + " ")
                f.write("\n")


def to_file_epoha_err(x, filename):
    f = open(filename, "w")
    n = len(x)

    for i in range(n):
        f.write(str(x[i][0]) + " " + str(x[i][1]) + "\n")

def f(x, y):
    return ((x - 1) ** 2 + (y + 2) ** 2 - 5 * x * y + 3) * cos(x / 5.) ** 2
    # return x + y


def generate(rng):
    ret = list()
    for i in range(rng[0], rng[1] + 1):
        for j in range(rng[0], rng[1] + 1):
            ret.append(((i, j), f(i, j)))

    return ret


def napuni(a, m):
    for i in range(0, m):
        a.append(uniform(-1, 1))


def mi(x, a, b):
    par = b * (x - a)
    if par > 200:
        return 0
    return 1. / (1 + np.exp(par))


def fi(x, y, p, q, r):
    return p * x + q * y + r


def t_norma(param1, param2):
    return param1 * param2


def initialize(l, m):
    l = list()
    for i in range(m):
        l.append(0.)
    return l


def train(br_iter, a, b, c, d, p, q, r, primjeri, etaa=1e-9, etab=1e-4, etac=1e-9, etad=1e-4, etap=1e-2, etaq=1e-2,
          etar=1e-2, batch=False):
    kraj = False
    izl = dict()
    pogreska = dict()
    mi_x = dict()
    mi_y = dict()
    epoha_err = list()
    if batch:
        era = list()
        erb = list()
        erc = list()
        erd = list()
        erp = list()
        erq = list()
        err = list()
    napuni(a, m)
    napuni(b, m)
    napuni(c, m)
    napuni(d, m)
    napuni(p, m)
    napuni(q, m)
    napuni(r, m)
    for it in range(br_iter):
        izl[it % 2] = list()
        pogreska[it % 2] = list()
        mi_x[it % 2] = list()
        mi_y[it % 2] = list()
        if batch:
            era = initialize(era, m)
            erb = initialize(erb, m)
            erc = initialize(erc, m)
            erd = initialize(erd, m)
            erp = initialize(erp, m)
            erq = initialize(erq, m)
            err = initialize(err, m)
        grsk = list()
        for pr in primjeri:
            zk = pr[1]
            x = pr[0][0]
            y = pr[0][1]

            suma1 = 0.
            suma2 = 0.
            mix = list()
            miy = list()
            for i in range(m):
                mix.append(mi(x, a[i], b[i]))
                miy.append(mi(y, c[i], d[i]))
                alphai = t_norma(mix[i], miy[i])
                fii = fi(x, y, p[i], q[i], r[i])

                suma1 += fii * alphai
                suma2 += alphai

            if suma2 == 0.:
                kraj = True
                break
            ok = suma1 / suma2

            izl[it % 2].append(ok)
            pogreska[it % 2].append(ok - zk)
            mi_x[it % 2].append(mix)
            mi_y[it % 2].append(miy)

            greska = 0.5 * (zk - ok) ** 2
            if greska > 1e9:
                kraj = True
                break

            # print(greska)
            # print(it)
            grsk.append(greska)
            for i in range(0, m):
                suma3 = 0.
                fii = fi(x, y, p[i], q[i], r[i])
                for j in range(m):
                    if i == j:
                        continue
                    alphaj = t_norma(mix[j], miy[j])
                    fij = fi(x, y, p[j], q[j], r[j])
                    suma3 += alphaj * (fii - fij)

                param1 = (zk - ok) * (suma3 / suma2 ** 2)
                param2 = (zk - ok) * (t_norma(mix[i], miy[i]) / suma2)

                if not batch:
                    ai = a[i]
                    a[i] = a[i] + etaa * param1 * miy[i] * b[i] * (1 - mix[i]) * mix[i]
                    bi = b[i]
                    b[i] = b[i] - etab * param1 * miy[i] * (x - ai) * (1 - mix[i]) * mix[i]
                    ci = c[i]
                    di = d[i]
                    c[i] = c[i] + etac * param1 * mix[i] * di * (1 - miy[i]) * miy[i]
                    d[i] = d[i] - etad * param1 * mix[i] * (y - ci) * (1 - miy[i]) * miy[i]

                    p[i] = p[i] + etap * param2 * x
                    q[i] = q[i] + etaq * param2 * y
                    r[i] = r[i] + etap * param2
                else:
                    era[i] += param1 * miy[i] * b[i] * (1 - mix[i]) * mix[i]
                    erb[i] += param1 * miy[i] * (x - a[i]) * (1 - mix[i]) * mix[i]
                    erc[i] += param1 * mix[i] * d[i] * (1 - miy[i]) * miy[i]
                    erd[i] += param1 * mix[i] * (y - c[i]) * (1 - miy[i]) * miy[i]
                    erp[i] += param2 * x
                    erq[i] += param2 * y
                    err[i] += param2

        if kraj:
            break
        if batch:
            # N = 1. / len(primjeri)
            for i in range(m):
                a[i] += etaa * era[i]
                b[i] += -etab * erb[i]
                c[i] += etac * erc[i]
                d[i] += -etad * erd[i]
                p[i] += etap * erp[i]
                q[i] += etaq * erq[i]
                r[i] += etar * err[i]

        print(sum(grsk))
        epoha_err.append((it, sum(grsk)))

    return izl, pogreska, mi_x, mi_y, epoha_err


rng = (-4, 4)
m = int(sys.argv[1])

primjeri = generate(rng)

# to_file(primjeri, "funkcija.dat")

a = list()
etaa = 1e-9
b = list()
etab = 1e-4
c = list()
etac = 1e-9
d = list()
etad = 1e-4
p = list()
etap = 1e-2
q = list()
etaq = 1e-2
r = list()
etar = 1e-2

# , etaa=1e-9, etab=1e-9, etac=1e-9, etad=1e-9, etap=1e-9, etaq=1e-9, etar=1e-9 -> dodaj za mijenjanje
# eta
nf, pogreska, mix, miy, epoha_err = train(1000, a, b, c, d, p, q, r, primjeri, batch=True)

izlazi = list()
pogreska_crt = list()
px = list()
py = list()
if len(nf[0]) > len(nf[1]):
    izlazi = nf[0]
    pogreska_crt = pogreska[0]
    px = mix[0]
    py = mix[0]
else:
    izlazi = nf[1]
    pogreska_crt = pogreska[1]
    px = mix[1]
    py = mix[1]

neuro = make_neuro(izlazi, primjeri)
pogreska = make_neuro(pogreska_crt, primjeri)
x = make_mi(px, primjeri, 0)
y = make_mi(py, primjeri, 1)

#to_file(neuro, "neuro23_stohastic.dat")
#to_file(pogreska, "neuro23_stohastic_err.dat")
#to_file_mi(x, "mi_x1.dat")
#to_file_mi(y, "mi_y1.dat", 1)
#to_file_epoha_err(epoha_err, "epoha_err_1e-9.dat")
