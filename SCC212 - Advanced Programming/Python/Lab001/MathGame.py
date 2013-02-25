#imports
import random


#global variables
levelChoice = 0
levelValid = 0

styleChoice = 0
styleValid = 0

ops = ['*','/','-','+']
op = 0

number1 = 0
number2 = 0

score = 0
questionsAttempted = 0


#function defs
def printWelcome():
	print("*****************************************")
	print("********Welcome To The Math Game********")
	print("*****************************************\n")
	return

def displayLevels():
        print("Please chose you initial level")
        print("Beginner - 1")
        print("Inter - 2")
        print("Advanced - 3\n")
        return

def getLevelChoice():
        global levelValid
        while levelValid == 0:
                levelChoice = input("Please choose a level: ")
                levelChoice = int(levelChoice)
                if levelChoice > 0 and levelChoice < 4:
                        levelValid = 1
                else:
                        print("That is not a valid level selection\n")
        return

def getGameStyle():
        global styleValid
        global styleChoice
        while styleValid == 0:
                styleChoice = raw_input("\nPlease select single or random mode: ")
                if styleChoice == "single" or styleChoice == "Single":
                        styleValid = 1
                        op = raw_input("Please choose an operand from +, -, *, /:")
                elif styleChoice == "random" or styleChoice == "Random":
                        print("Random Mode Selected \n")
                        styleValid = 1
                else:
                        print("That is not a valid style selection\n")
        return


def generateQuestions():
        global questionsAttempted
        global score
        global levelChoice
        global op
        global number1
        global number2

        if levelChoice == 1:
                number1 = random.randrange(1,5)
                number2 = random.randrange(1,5)
                if level_mode == "random" or level_mode == "Random":
                        randomOperand = random.choice(ops)
                else:
                        randomOperand = op
       

        question = "%d %s %d" % (number1, randomOperand, number2)
        answer = eval(question)
        print(question)
        
        userAnswer = input("Your Answer: ")
        userAnswer = int(userAnswer)
        
        print(answer)
        
        questionsAttempted += 1

#program
printWelcome()
displayLevels()
getLevelChoice()
getGameStyle()
generateQuestions()
