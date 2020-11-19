package modules;

import exceptions.ValueNotInRangeException;
import java.util.Random;

/**
 * {@link #Scrambler()} is used to generate rubiks cube scrambles using the
 * standard notation
 * 
 * @author Kawa-git
 */
public class Scrambler {
    private static final int STDNUMBER = 21;
    private static final char[] MOVES = { 'R', 'L', 'U', 'D', 'F', 'B' };
    private static final String[] DIRECTION = { "'", "", "2" };
    private static final int NOTINRANGEVALUE = 100;
    private String[] result;
    private int randomValue;
    private int randomDirection;
    Random moveSelector;
    Random directionSelection;

    public Scrambler() {
        moveSelector = new Random();
        directionSelection = new Random();
    }

    /**
     * This method is used to calculate the scarmble itself
     * 
     * @param numberOfIterations is used to decide how many cube notations the user
     *                           needs
     * @param previousRnd        is used to keep track of the previous random number
     * @return we return an array that may be used in {@link #printScramble()}
     * @throws ValueNotInRangeException
     */
    public String[] getScramble(int numberOfIterations) throws ValueNotInRangeException {
        if (numberOfIterations > 0 && numberOfIterations <= NOTINRANGEVALUE) {
            int previousRnd = 0;
            result = new String[numberOfIterations];

            for (int i = 0; i < numberOfIterations; i++) {

                do {
                    randomValue = moveSelector.nextInt(6);
                    randomDirection = directionSelection.nextInt(3);

                    result[i] = MOVES[randomValue] + DIRECTION[randomDirection];

                } while (previousRnd == randomValue);
                previousRnd = randomValue;
            }

        } else {
            // throw exception
            throw new ValueNotInRangeException(numberOfIterations, 0, NOTINRANGEVALUE);
        }
        return result;
    }

    /**
     * This method is used to calculate the scramble itself
     * 
     * @param previousRnd is used to get the previous random number
     * @return we return an array that is used by {@link #printScramble()}
     */
    public String[] getScramble() {
        int previousRnd = 0;
        result = new String[STDNUMBER];

        for (int i = 0; i < STDNUMBER; i++) {

            do {
                randomValue = moveSelector.nextInt(6);
                randomDirection = directionSelection.nextInt(3);

                result[i] = MOVES[randomValue] + DIRECTION[randomDirection];

            } while (previousRnd == randomValue);
            previousRnd = randomValue;
        }
        return result;
    }

    /**
     * This method is used to print the scramble by reading the {@link #result}
     * returned by {@link #getScramble()}
     */

    public void printScramble() {
        for (String elements : getScramble()) {
            System.out.print(elements + "   ");
        }
        System.out.println("\n");
    }

    /**
     * This method is used to print the scramble by reading the {@link #result}
     * returned by {@link #getScramble(int)}
     * 
     * @throws ValueNotInRangeException
     */

    public void printScramble(int numberOfIterations) throws ValueNotInRangeException {
        for (String elements : getScramble(numberOfIterations)) {
            System.out.print(elements + "   ");
        }
        System.out.println("\n");
    }
}
