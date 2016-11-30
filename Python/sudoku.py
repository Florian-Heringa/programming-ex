import sys
from math import sqrt

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
    """
    Prints a sudoku grid to the screen in lines
    """

    rowString = ""

    for row in grid:
        for num in row:

            rowString += "{} ".format(num)

        print(rowString)
        rowString = ""

def transpose(grid):
    """
    Returns the transpose of an nxn matrix
    """

    return [[row[i] for row in grid] for i in range(len(grid))]

def findEmpty(grid):
    """ 
    Finds empty spots (designated by '0') in an nxn grid
    """

    n = len(grid)
    return [[i, j] for i in range(n) for j in range(n) if grid[i][j] == 0]

def posNums(row, col, block):
    """
    Finds possible numbers for entering in an empty spot in a sudoku
    """

    n = len(row) + 1
    possibilities = range(1, n)
    ans = []
    for pos in possibilities:
        if pos in row or pos in col or pos in block:
            continue
        else:
            ans.append(pos)

    return ans

def toBlock(grid):
    """ 
    In => an nxn sudoku grid
    Out => the grid with the lines represented in blocks (or vice versa)
    """

    n = len(grid[0])
    blocksize = int(sqrt(n))

    gridBlock = [[grid[i + s][j + t] for i in range(blocksize) for j in range(blocksize)]
                 for s in range(0, n, blocksize) for t in range(0, n, blocksize)]

    return gridBlock

def findSol(grid, gridTr, block):
    """
    Find solution to sudoku by iteratively trying all possible combinations
    and adding the ones that are singular
    """

    openSpots = findEmpty(grid)
    trialMode = False
    trialInd = 0
    trialSpots = []

    while len(openSpots) > 0:

        for spot in openSpots:
            # current position in grid
            n, m = spot
            row = grid[n]
            col = gridTr[m]
            # list of possible solutions for the chosen position
            posSol = posNums(row, col, block)
            # If there is only one possibility, add into the grid
            if len(posSol) == 1:
                grid[n][m] = posSol[0]
                openSpots.remove(spot)
                


#########################################################
grid = readInts(fileName)
gridTr = transpose(grid)
gridBlock = toBlock(grid)
print("Welcome to sudoku solver, you entered the following grid:")
printGrid(grid)
print("\n")
openSpots = findEmpty(grid)
print("There are {} open spots:".format(len(openSpots)))
print(openSpots)

print("\nThe solution is\n")
findSol(grid, gridTr, gridBlock)
printGrid(grid)
