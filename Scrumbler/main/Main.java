
import exceptions.ValueNotInRangeException;
import modules.Scrambler;

public class Main {
    public static void main(String[] args) {
        Scrambler scrambler = new Scrambler();

        try {
            scrambler.printScramble(15);
        } catch (ValueNotInRangeException e) {
            e.getMessage();
        }

    }
}