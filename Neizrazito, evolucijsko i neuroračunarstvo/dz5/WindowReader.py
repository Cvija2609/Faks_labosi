import pyglet
from pyglet import window
from pyglet.window import mouse
from pyglet.gl import *
from MakeRepresentatives import *
from NeuralNetwork import *


def readFile(filename):
    retVal = list()
    with open(filename) as f:
        content = f.readlines()
    content = [x.strip() for x in content]

    for line in content:
        retVal.append(toList(line))

    return retVal


M = 30
letters = 0
greek = ["Alpha", "Beta", "Gamma", "Delta", "Epsilon"]
num = 0
magic = 20

window = pyglet.window.Window(resizable=True, caption="Draw " + greek[letters] + " " + str(magic - num) + " more times")

xi, yi = list(), list()
store = dict()
ukupno = 0
keys = list()

for letter in greek:
    for i in range(0, magic):
        keys.append(letter + str(i))

for k in keys:
    store[k] = tuple()


@window.event
def on_mouse_drag(x, y, dx, dy, buttons, modifiers):
    global xi, yi
    if buttons and mouse.LEFT:
        xi.append(x)
        yi.append(y)


@window.event
def on_mouse_release(x, y, buttons, modifiers):
    global xi, yi, num, ukupno, greek, letters, magic
    store[greek[letters] + str(num)] = (np.array(xi), np.array(yi))
    xi = list()
    yi = list()
    num += 1
    if num == magic:
        ukupno += magic
        num = 0
        if letters == (len(greek) - 1):
            pass
        else:
            letters += 1

    window.set_caption("Draw " + greek[letters] + " " + str(magic - num) + " more times")

    if ukupno == len(greek) * magic:
        window.close()


@window.event
def on_mouse_press(x, y, buttons, modifiers):
    if buttons & mouse.RIGHT:
        window.close()


@window.event
def on_draw():
    window.clear()
    glBegin(GL_POINTS)
    for i in range(0, len(xi)):
        glVertex2f(xi[i], yi[i])
    glEnd()


pyglet.app.run()

Ds = dict()
representatives = dict()

normalize(store)
getDs(Ds, store)
getRepr(representatives, store, Ds, M)


file1 = open("gestures.txt", "w")

for key in representatives:
    L = str()
    for arr in representatives[key]:
        L += str(list(arr)) + ", "
    if greek[0] in key:
        L += "[1., 0., 0., 0., 0.]"
    elif greek[1] in key:
        L += "[0., 1., 0., 0., 0.]"
    elif greek[2] in key:
        L += "[0., 0., 1., 0., 0.]"
    elif greek[3] in key:
        L += "[0., 0., 0., 1., 0.]"
    else:
        L += "[0., 0., 0., 0., 1.]"
    L += "\n"
    file1.writelines(L)

file1.close()

dataset = readFile("gestures.txt")

ndata = list()


for data in dataset:
    xevi = list()
    yoni = list()
    for d in data[0:-1]:
        xevi.append(d[0])
        yoni.append(d[1])
    oba = list()
    for i in xevi:
        oba.append(i)
    for i in yoni:
        oba.append(i)
    ndata.append((np.array(oba), np.array(data[-1])))

arch = "60x5x6x5"

nn = NeuralNetwork(arch, ndata)

nn.miniBatch(1, 1000)

nn.to_txt("greek_weights.txt")

#for d in ndata:
#    print(d)
#    print(nn.evaluate(d[0]))