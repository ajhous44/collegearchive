"""
Names: Genesis Sarmiento, Andrew Housholder, Omidullah Barikzay
"""

from runtournament import *

import pylab  #only necessary if you uncomment the the fitness plotting code

import random

def generateOneRandomRule():     
    """ returns a random 6-character rule string that matches the FiniteStateWizard syntax."""     
    return random.choice('0123456789') + random.choice('MWA.#') + random.choice('0123456789') + random.choice('FCRLUN') + random.choice('0123456789*') + '_' 
    
def generateRandomGenotype(numRules):
    """ This method generates a random genotype (chromosome) with numRules rules in it...
        i.e. a valid set of FSM rules, which is a string 6*numRules characters long"""
    
    randGeno = ""
    randNumRules = random.randrange(numRules, stop=numRules+5)
    for i in range(randNumRules):
        randGeno = randGeno + generateOneRandomRule()
    #print(randGeno)
    return randGeno

def getMutatedChar(originalChar, mutationChance): #mutateChance is a %
    """ This method mutates a single character by possibly replacing it 
    (with the given mutationChance) with a new character that is interchangeable with it."""
    if random.random() < mutationChance:
        if originalChar == '_':
            return '_'
        elif originalChar in "0123456789*":
            return random.choice("0123456789*")
        elif originalChar in "MWA.#":
            return random.choice("MWA.#")
        elif originalChar in "FCRLUN":
            return random.choice("FCRLUN")
        else:
            raise Exception("Illegal FSM rules char: " + originalChar)
    else:
        return originalChar

def mutate(dnaString, mutationRate):
    """ returns a mutated version of the dna provided. 
    The mutationRate parameter controls how large and/or frequent the dna mutations are."""
    #~ This could involve going through the whole DNA strand and having a chance of changing
    #~ each character to be a random new one (call getMutatedChar() to help with this).
    #~ changing a whole rule, swapping the locations of two whole rules within the string, 
    #~ doing all of the above, etc.
    #~ It's up to you how to perform mutations, but the result must represent a *valid* finite state machine.
    
    # TODO: Fix/complete this method.
    
    newDna = ""
    for index in range(len(dnaString)):
        newChar = getMutatedChar(dnaString[index], mutationRate)
        newDna = newDna + newChar

    return newDna
    
def crossover(parentDNA1, parentDNA2):
    """ returns list of two child DNA strings [child1, child2] resulting
    from performing some crossover/recombination on the parent DNA"""
    splitPoint = random.randrange(1,min(len(parentDNA1), len(parentDNA2)))
    child1 = parentDNA1[:splitPoint] + parentDNA2[splitPoint:]
    child2 = parentDNA2[:splitPoint] + parentDNA1[splitPoint:]
    # (or change to use a different crossover mechanism, which combines the DNA in 
    # different ways!  (1-point crossover may or may not be the best...))
    return [child1, child2] 

def runTournaments(wizardPopulation, numDuels, numMovementRounds):
    """ Takes in a list of wizards to compete in tournaments.
    This method doesn't return anything, but after running this, every Wizard object should
    have stats about how many rounds they won, kills they had, apples they collected...
    
    You have a couple options:  
    
    a) You can split the wizard population up (evenly!) into groups of 5,
    and have them compete against each other (this is called "coevolution")
    
    b) You could also try each wizard in a tournament against a fixed set of a human-designed wizards
     (Alice-Dave?) in order to compare performance against a fixed benchmark
     
    I chose option (a) here, but you can try the other if you like instead.
    """
    random.shuffle(wizardPopulation)
    numGroups = int(len(wizardPopulation) / 10)
    for i in range(numGroups):
        currentGroup = wizardPopulation[i*10:i*10+10]
        runTournament(currentGroup,delayFactor=0.0, numDuels=numDuels, numMovementRounds=numMovementRounds, useGraphics=False, printScoreBoardToConsole=False) 
    
def evaluateFitness(wiz):
    """wiz is a FiniteStateWizard object... this method must be called AFTER the wizard
      has competed in duels, so that it has the appropriate win/kill/apple stats...
      This method DOES NOT run any duels/simulations... it just turns the resulting statistics
      into a single fitness measure that says 'how well did this wizard peform?' 
      Larger fitness values should represent better wizards."""
     
    return 10*wiz.getNumWins() +  5*wiz.getKillCount() + wiz.getApplePickCount()
    
def selectFitParent(wizardPopulation):
    """Let's use so-called 'tournament selection' where we choose K (in this case 3) 
      parents at random from the population, and return the one who has higher fitness.
      This way more highly fit parents are more likely to be chosen, but almost every individual 
      has a chance to reproduce."""
    candidates = random.sample(wizardPopulation, 3)
    return max(candidates, key=evaluateFitness)

def printStatsForCurrentBest(gen, bestWizard):
    """ Prints out some stats for the best wizard from each generation..."""
    bestFitness = evaluateFitness(bestWizard)
    bestWins = bestWizard.getNumWins()
    bestKills = bestWizard.getKillCount()
    bestApples = bestWizard.getApplePickCount()
    print("Gen %03d: best fitness=%3.2f wins=%3.2f kills=%d apples=%d"%(gen, bestFitness, bestWins, bestKills, bestApples))


def runGeneticAlgorithm(populationSize, numGenerations, numRulesInWizard, numDuelsForFitnessEval, numMovementRounds, crossoverRate, mutationRate):
    """ * populationSize should be a multiple of 10 (if you split the wizards into 10-wizard duels).  Bigger population is better, but also take more time!
        * numGenerations should probably be at least 50 if you want to evolve decent solutions.  Bigger is better, but also takes more time!
        * numRulesInWizard - how many FSM rules does each wizard contain? This controls how long the DNA string is, and the overall
           complexity of each wizard's "brain".  Too short may make the brains too simple, but longer/more complex isn't ALWAYS better.
        * numDuelsForFitnessEval - using more repeated duels to judge fitness gives a more accurate estimate of which wizards are better, but it also takes more time!
        * numMovementRounds - how long should each duel last?  Probably in the ballpark of 50 or 100 rounds?  
        * crossoverRate controls how many new individuals are produced by sexual recombination, rather than asexual cloning.
           should be between 0.0 and 1.0, with 0.7 being a good default.
        * mutationRate controls how frequent/large the mutations are.  Should probably be between 0.0 and 1.0, with low values (< 0.05)
          being more likely to give good results.  Too many/large mutations cause rapid population change, which can lose good individuals.
    """
    # python tip: a FOR loop running inside of list brackets is called a "list comprehension"  (google it!)
    genotypes = [generateRandomGenotype(numRulesInWizard) for i in range(populationSize)] 
    wizardPhenotypes = [ FiniteStateWizard(genotypes[i],"gen%d_wiz%d"%(0,i)) for i in range(populationSize) ]
    runTournaments(wizardPhenotypes, numDuelsForFitnessEval, numMovementRounds)
    bestWizard = max(wizardPhenotypes, key=evaluateFitness)     
    printStatsForCurrentBest(0, bestWizard)
    
    bestFitnessHistory = [evaluateFitness(bestWizard)] # keep track of the best fitness after each generation
    
    for gen in range(numGenerations):
        newGenotypes = []
        for i in range(int(crossoverRate * populationSize / 2)): # create children from crossover/recombination
            parent1Wiz = selectFitParent(wizardPhenotypes)
            parent2Wiz = selectFitParent(wizardPhenotypes)
            parent1Genotype = parent1Wiz.getRuleDNA()
            parent2Genotype = parent2Wiz.getRuleDNA()
            twoChildren = crossover(parent1Genotype, parent2Genotype)
            newGenotypes.extend(twoChildren)
            
        while len(newGenotypes) < populationSize:  # create rest of children asexually (i.e. cloning the parents)
            parentWiz = selectFitParent(wizardPhenotypes)
            newGenotypes.append(parentWiz.getRuleDNA())
            
        #after crossover & cloning, we apply mutation to each member of the population of genotypes
        newMutatedGenotypes = [ mutate(genotype,mutationRate=mutationRate) for genotype in newGenotypes ]
        # then we set the current population of genotypes to be the NEW genotypes.  
        # i.e., this is the FULL POPULATION generational replacement strategy for genetic algorithms.  
        # All parents "die", all new children take their places.
        genotypes = newMutatedGenotypes
        
        wizardPhenotypes = [ FiniteStateWizard(genotypes[i],"gen%d_wiz%d"%(gen,i)) for i in range(populationSize) ]
        runTournaments(wizardPhenotypes, numDuelsForFitnessEval, numMovementRounds)
        bestWizard = max(wizardPhenotypes, key=evaluateFitness)
        bestFitnessHistory.append(evaluateFitness(bestWizard))
        printStatsForCurrentBest(gen+1, bestWizard)
    print("\nBest wizard DNA: " + bestWizard.getRuleDNA())
    
    # create a plot to visualize the fitness over time?  
    # Even if fitness isn't going up, is it possible that the wizards are still getting better, if they're competing against each other?
    #pylab.plot(range(numGenerations+1), bestFitnessHistory)
    #pylab.xlabel("Generation #")
    #pylab.ylabel("Highest fitness in population")
    #pylab.show()
        
def main():
    print("Starting genetic algorithm...")
    """populationSize, numGenerations, numRulesInWizard, numDuelsForFitnessEval, numMovementRounds,crossoverRate, mutationRate
    """
    runGeneticAlgorithm(50, 80, 6, 5, 300, 0.7, 0.01) 
    
if __name__ == '__main__':
    main()
