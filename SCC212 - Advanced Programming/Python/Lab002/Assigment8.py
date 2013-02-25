###Assigment 8
import random

##variables
userInput = ""

chars = ['A', 'B', 'C', 'D' ,'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
         'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
         '0', '1', '2', '3', '4', '5', '6', '7', '8', '9']

##builds the cipher table
def buildCipherTable(chars):
    table = [ [ 0 for i in range(6) ] for j in range(6) ]
    z = 0
    for x in range(6):
        for y in range(6):
            table[x][y] = chars[z]
            z += 1
    return table

##prints our the grid
def printTable(grid):
    for rownum, row in enumerate(grid):
        print(row)


##finds a letter in the grid and returns the co-ords
def findLetter(grid, letter):
    for rownum, row in enumerate(grid):
        for colnum, itemvalue in enumerate(row):
            if itemvalue == letter:
                return (rownum, colnum)

##gets the users plain text
def getPlainText():
    inputString = input("\nPlease enter the string to encrypt\n")
    inputString = inputString.strip().replace(" ", "")
    return inputString

def getCihperText():
    inputString = input("\nPlease enter the string to decrypt\n")
    return inputString

##encrypts the string pass in the plain text and the grid
def encryptString(text, grid):
    text = text.upper()
    cipherText = ""
    for letter in text:
        row, col = map(numberToLetter, findLetter(grid, letter))
        cipherText += "%s%s " % (row,col)
    print("Cipher Text is: %s" % (cipherText))

def decryptString(text, grid):
    plainText = ""
    pairs = [tuple(ord(letter) - ord('A') for letter in pair) for pair in text.split()]
    plainText = ''.join(grid[x][y] for x, y in pairs)
    print(plainText)


##converts a number to a letter    
def numberToLetter(number):
    if number >= 0 and number <= 26:
        return chr(65 + number)


##sets up the cipher grid and prints it out
##returns the cipher grid
def setup():
    random.shuffle(chars)
    grid = buildCipherTable(chars)
    printTable(grid)
    return grid

##main program
cipherTable = setup()
print()
userInput = getPlainText()
print()
encryptString(userInput, cipherTable)
print()
userInput = getCihperText()
print()
decryptString(userInput, cipherTable)
