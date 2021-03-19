import pyglet
from pyglet import window
from pyglet.window import mouse
from NeuralNetwork import *
from pyglet.gl import *
from MakeRepresentatives import *

def read(string):
    retVal = list()

    content = string.strip()

    retVal.append(toList(content))

    return retVal


window = pyglet.window.Window(resizable=True, caption="Draw Alpha, Beta, Gamma, Delta or Epsilon")

xi, yi = list(), list()
store = tuple()

@window.event
def on_mouse_drag(x, y, dx, dy, buttons, modifiers):
    global xi, yi
    if buttons and mouse.LEFT:
        xi.append(x)
        yi.append(y)


@window.event
def on_mouse_release(x, y, buttons, modifiers):
    global xi, yi, store
    store = (np.array(xi), np.array(yi))
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

st = dict()
st["0"] = store

normalize(st)
Ds = dict()
representatives = dict()

getDs(Ds, st)
getRepr(representatives, st, Ds, M=30)

L = str()
for key in representatives:
    i = 0
    for arr in representatives[key]:
        if i == len(representatives[key]) - 1:
            L += str(list(arr))
        else:
            L += str(list(arr)) + ", "
    L += "\n"

dataset = read(L)

ndata = list()

for data in dataset:
    xevi = list()
    yoni = list()
    for d in data:
        xevi.append(d[0])
        yoni.append(d[1])
    oba = list()
    for i in xevi:
        oba.append(i)
    for i in yoni:
        oba.append(i)
    ndata.append(np.array(oba))

arch = "60x5x6x5"

ndata = np.array([ndata])

nn = NeuralNetwork(arch, ndata)

nn.from_txt("greek_weights.txt")

print(nn.evaluate(ndata[0][0]))
