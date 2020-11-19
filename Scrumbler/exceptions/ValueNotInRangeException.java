package exceptions;

/**
 * Simple exception used when a variable is not in a specific range
 * 
 * @author Kawa-git
 */
public class ValueNotInRangeException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param numberOfIterations is the variable which may not be in range
     * @param leftmostRange is the lower limit of the range
     * @param rightmostRange is the upper limit of the range
     * 
     */
    public ValueNotInRangeException(int numberOfIterations, int leftmostRange, int rightmostRange) {
        super(numberOfIterations + " is not in range [" + leftmostRange + ", " + rightmostRange + ")");
    }

    /**
     * @param numberOfIterationsis the variable which may not be in range
     */
    public ValueNotInRangeException(int numberOfIterations) {
        super(numberOfIterations + " is not in range");
    }

}
