

def checkSeq(l):
    """
    In => List of integers of length n
    Out => Checks if all integers from 1 to n are included in the list
    Gives the numbers that are missing and the numbers that are extra
    (outside of the range)
    """

    missing = [x for x in range(1,len(l)+1) if x not in l]
    extra = [x for x in l if x not in range(1, len(l)+1)]

    print("The numbers missing are: [%s]" % ', '.join(str(x) for x in missing))
    print("The extra numbers are: [%s]" % ', '.join(str(x) for x in extra))

def genMissing(L):
    """
    Generator for numbers missing from a list.
    In => List of integers of length n
    Out => All integers missing from the range [1, n]
    """

    for i in range(1, len(L) + 1):
        if i not in L:
            yield i

def readInts(filename):
    """
    In => A file containing lines of integers.
    Out => A list of lists. Every inner list containing one line of integers.
    """

    listOfInts = []
    lineList = []

    with open(filename) as f:

        line = f.readline()

        while line:

            for integer in line.split(" "):

                lineList.append(int(integer))

            listOfInts.append(lineList)
            lineList = []

            line = f.readline()

    print(listOfInts)


inpList = [1,2,3,4,5,6]
inpList2 = [1,2,4,5,6]
inpList3 = [3,2,1,6,4,8,6]

fileName = "testInt.txt"


checkSeq(inpList)
checkSeq(inpList2)
checkSeq(inpList3)

gen = genMissing(inpList3)

print(next(gen))
print(next(gen))

readInts(fileName)
