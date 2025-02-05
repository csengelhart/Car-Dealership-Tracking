package javaFiles;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * A class that reads and writes to JSON files
 *
 * @author Dylan Browne
 */
public class JSONIO
{
    private final File file;
    private final char mode;
    private final static String[] keyOrder = {
            "dealership_id", "vehicle_type",
            "vehicle_manufacturer", "vehicle_model",
            "vehicle_id", "price", "acquisition_date" };

    /**
     * Creates or opens a JSON file with name fileName in read ('r') or write ('w') mode.
     * Read mode allows the reading, but not writing of files, write mode allows for the
     * writing, but not reading of files.
     *
     * @param filePath The path of the file to be opened or created
     * @param mode A char representation of the type of file this is (read 'r' or write 'w')
     * @throws ReadWriteException Thrown if the mode is an invalid char
     */
    public JSONIO(String filePath, char mode) throws ReadWriteException {
        this.mode = getMode(mode);
        this.file = new File(filePath);
    }

    /**
     * Takes a char and converts it to an appropriate lowercase version of the mode
     *
     * @param mode A char representation of the type of file this is to be converted to correct form
     * @return The correct form of the char representation (read 'r', write 'w')
     * @throws ReadWriteException Thrown if the mode is an invalid char
     */
    private char getMode(char mode)  throws ReadWriteException{
        mode = Character.toLowerCase(mode);
        char[] valid_modes = {'r', 'w'};
        for (char mode_lower : valid_modes) {
            if (mode == mode_lower) {
                return mode;
            }
        }

        String message = "Mode '" + mode + //original mode, not converted
                "' is not in {'r', 'R', 'w', 'W'}, file not opened.";
        throw new ReadWriteException(message);
    }

    /**
     * Returns keyOrder, which is a list of Strings that correspond
     * to the index of the corresponding variable index in input and
     * output String[] for the read and write class.
     *
     * @return The
     */
    public static String[] getKeyOrder() {
        return keyOrder;
    }

    /**
     * Reads and returns the data stored in the file of this object.
     *
     * @return An array of String[] that correspond to the array of data stored
     *         in the JSON file for this object. The String[] has data in the
     *         indexes represented by keyOrder.
     * @throws ReadWriteException Thrown if not in read ('r') mode.
     */
    public String[][] read() throws ReadWriteException {
        if (mode != 'r') {
            throw new ReadWriteException("Must be mode 'r', not mode '" + mode + "'.");
        }
        JSONParser parser = new JSONParser();
        Reader fileReader;
        JSONObject jFile = null;
        JSONArray jArray;
        try {
            fileReader = new FileReader(file);
            jFile = (JSONObject) parser.parse(fileReader);
            fileReader.close();
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
        }

        assert jFile != null;

        jArray = (JSONArray)jFile.get("car_inventory");
        String[] keyOrder = getKeyOrder();
        String[][] output =  new String[jArray.size()][keyOrder.length];

        for (int i = 0; i < jArray.size(); i++) {
            JSONObject jObj = (JSONObject)jArray.get(i);
            for (int j = 0; j < keyOrder.length; j++) {
                String key = keyOrder[j];
                Object dataPoint = jObj.get(key);
                if (dataPoint instanceof Long) {
                    output[i][j] = ( (Long) dataPoint).toString();
                } else {
                    output[i][j] = (String) dataPoint;
                }
            }
        }

        return output;
    }

    /**
     * Takes an array of String[] to write to the file stored in this object.
     *
     * @param data An array of an array of Strings to write to a file.
     *             The array of String should be in the order represented by keyOrder.
     * @return The number of entries written to the file
     * @throws ReadWriteException Thrown if not in write ('w') mode.
     */
    public int write(String[][] data) throws ReadWriteException {
        int added = 0;
        if (mode != 'w') {
            throw new ReadWriteException("Must be mode 'w', not mode '" + mode + "'.");
        }

        String[] keyOrder = getKeyOrder();

        JSONArray jArray = new JSONArray();
        for (String[] carData : data) {
            if (carData.length == keyOrder.length) {
                JSONObject jObj = new JSONObject();
                for (int i = 0; i < keyOrder.length; i++) {
                    Object dataPoint = carData[i];
                    if (i >= keyOrder.length - 2) {
                        dataPoint = Long.parseLong((String) dataPoint);
                    }
                    jObj.put(keyOrder[i], dataPoint);
                }
                jArray.add(jObj);
                added++;
            }
        }

        Writer fileWriter;
        JSONObject jFile = new JSONObject();
        jFile.put("car_inventory", jArray);
        try {
            fileWriter = new FileWriter(file);
            jFile.writeJSONString(fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return added;
    }

    // for testing purposes
    public static void main(String[] args) {
        // TODO: Check inventory.json is supposed to be case sensitive
        // (download from d2l and compare to this inventory.json if I forget what this means)

        try {
            JSONIO jReadExample = new JSONIO("Car-Dealership-Tracking/inventory.json", 'r');
            JSONIO jWriteExample = new JSONIO("Car-Dealership-Tracking/output.json", 'w');
            String[][] read = jReadExample.read();
            if (jWriteExample.write(read) == read.length) {
                System.out.println("Added all files.");
            }
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }

    }
}