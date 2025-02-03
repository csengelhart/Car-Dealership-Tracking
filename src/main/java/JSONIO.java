/**
 * A class that reads and writes to JSON files
 *
 * @author Dylan Browne
 */

public class JSONIO
{
   // File file;
    char mode;
    /**
     *
     *
     * @param fileName
     * @param mode
     */
    public JSONIO(String fileName, char mode) throws ReadWriteAppendException
    {
        if (getMode(mode) == 'x') {
            String message = "Mode (" + mode +
                    ") is not in {'r', 'R', 'w', 'W', 'a', 'A'}";
            throw new ReadWriteAppendException(message);
        }

    }

    /**
     * Takes a char and converts it to
     *
     * @param mode
     * @return
     */
    private char getMode(char mode) {
        mode = Character.toLowerCase(mode);
        char[] valid_modes = {'r', 'w', 'a'};
        for (char mode_lower : valid_modes) {
            if (mode == mode_lower) {
                return mode;
            }
        }
        return 'x';
    }

    // for testing purposes
    public static void main(String[] args) {
        System.out.println("Hello World");
    }

    public int write(String[][] data) throws ReadWriteAppendException {
        int added = 0;
        if (mode != 'w') {
            throw new ReadWriteAppendException();
        }


        return added;
    }
}