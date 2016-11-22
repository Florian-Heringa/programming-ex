import sys

fileName = sys.argv[1]

def readInts(filename):
    """
    In => A filename of a file containing lines of integers.
    Out => A list of lists. Every inner list containing one line of integers.
    """

    listOfInts = []
    lineList = []

    with open(filename) as f:

        # Read first line
        line = f.readline()

        # Loop until next line is NONE
        while line:

            lineList = [int(integer) for integer in line.split(" ")]

            # Add line to database, reset lineList
            listOfInts.append(lineList)
            lineList = []

            # Read next line
            line = f.readline()

    return listOfInts

def printGrid(grid):

    rowString = ""

    for row in grid:
        for num in row:

            rowString += "{} ".format(num)

        print(rowString)
        rowString = ""

def transpose(grid):

    return [[row[i] for row in grid] for i in range(len(grid))]

def findEmpty(grid):

    n = len(grid)
    openSpots = [[i, j] for i in range(n) for j in range(n) if grid[i][j] == 0]

    return openSpots


def posNums(row, col):

    n = len(row) + 1
    possibilities = range(1, n)
    ans = []
    for pos in possibilities:
        if pos in row or pos in col:
            continue
        else:
            ans.append(pos)

    return ans

def findSol(grid, gridTr):

    openSpots = findEmpty(grid)
    x = 1

    while len(openSpots) > 0:

        for spot in openSpots:
            n, m = spot
            row = grid[n]
            col = gridTr[m]
            posSol = posNums(row, col)
            if len(posSol) == 1:
                grid[n][m] = posSol[0]
                openSpots.remove(spot)
                print("Spot {} filled".format(x))
                x += 1


#########################################################
grid = readInts(fileName)
gridTr = transpose(grid)
print("Welcome to sudoku solver, you entered the following grid:")
printGrid(grid)

#print("transposed")

#printGrid(gridTr)
print("\n")
openSpots = findEmpty(grid)
print("There are {} open spots:".format(len(openSpots)))
print(openSpots)

print("\nThe solution is\n")
findSol(grid, gridTr)
printGrid(grid)
