"""
Names: Genesis Sarmiento, Andrew Housholder, Omidullah Barikzay
"""

from runtournament import *

def main():
    wizards = []
    
    wizards.append(FiniteStateWizard('*A3F0_0W3C1_*M3R0_*#1R0_*.3F*_', "Alice"))
    wizards.append(FiniteStateWizard('0.1F*_*M9L1_1.1F0_0W4C2_2W2L0_2A1F0_2.1F0_0A2F*_*#1L0_', "Bob"))
    wizards.append(FiniteStateWizard('*A3F0_0W3C1_*M3L0_*#1U0_*.3F*_', "Carla"))
    wizards.append(FiniteStateWizard('*A3F0_0W3C1_*M3L0_*#1R0_*.3F*_', "Dave"))
    wizards.append(FiniteStateWizard('0.1F*_*M9L1_1.1F0_0W4C2_2W2L0_2A1F0_2.1F0_0A2F*_*#1L0_', "Egg"))
    wizards.append(FiniteStateWizard('*A3F0_0W3C1_*M3L0_*#1U0_*.3F*_', "Tom"))
    wizards.append(FiniteStateWizard('*A3F0_0W3C1_*M3L0_*#1R0_*.3F*_', "Jess"))
    wizards.append(FiniteStateWizard('*A3F0_0W3C1_*M3L0_*#1L0_*.3F*_', "Israel"))
    wizards.append(FiniteStateWizard('9W1R9_4M8L4_4M2R8_2M3N4_5.3C9_0#5U5_6.0N*_*A8F5_', "EVOKATTTTT"))
    wizards.append(FiniteStateWizard('0.1F*_*M9L1_1.1F0_0W4C2_2W2L0_2A1F0_2.1F0_0A2F*_*#1L0_', "GENEDRIDDDDD"))
                                      
    # Each rule is a 6-char sequence, ABCDE_ where:
#  A is the currentState (0-9 or * for ANY) which the finite state machine (FSM) must be in in order for this rule to trigger
#  B is the object code (A: Apple, M: Missile, W: Wizard, .: Empty square, # arena boundary)
#         in the Wizard's vision that could trigger this rule IF it is observed within...
#  C units, which is the distance threshold (0-9) for seeing object C, except special char * can be used to mean NOT seeing this object.
#      The trigger condition is satisfied if the object is closer than this threshold (i.e., actual distance <= D)
#  D is the action code (F: forward, L: turn left, R: turn right, U: U-turn, C: Cast missile, N: No-op)
#     to take if this rule is triggered.
#  E is the new state (0-9 or * for remain in same mode) that the FSM will enter if this rule is triggered
     
    runTournament(wizards,delayFactor=0.2, numDuels=1, numMovementRounds=300, useGraphics=True, printScoreBoardToConsole=False)

    # Note: for faster testing, you can also run it with ZERO delay and without graphics, like so:
    #~ runTournament(wizards,delayFactor=0.0, numDuels=20, numMovementRounds=100, useGraphics=False, printScoreBoardToConsole=False)
        
    
if __name__ == '__main__':
    main()
