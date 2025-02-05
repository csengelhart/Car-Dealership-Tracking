package javaFiles;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


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
                if (key.equals("acquisition_date")) { //acquisition date is in Epoch format convert to dd-mm-yyyy
                    output[i][j] = convertMilisec_to_date((Long) dataPoint);

                } else if (key.equals("price")) {
                    output[i][j] = String.valueOf(dataPoint);
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


    /**
     * Opens a file chooser dialog to allow the user to select a JSON file.
     * The file chooser will start in the current user's working directory
     * and will filter files to only show those with a ".json" extension.
     *
     * @return The selected JSON file if the user selects a file and confirms the dialog,
     *         or null if the user cancels or closes the dialog without selecting a file.
     *
     * @author Christopher Engelhart
     */
    public static File selectJsonFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files", new String[]{"json"}));
        int result = fileChooser.showOpenDialog((Component)null);
        return result == 0 ? fileChooser.getSelectedFile() : null;
    }


    /**
     * Takes a long value that represents the number of milliseconds since 01Jan1970
     * and converts to a date in the format "dd-MM-yyyy"
     * @param acquisition_date long value that is the milliseconds since 01Jan1970
     * @return formatted String representation of acquisition date
     *
     * @author Christopher Engelhart
     */
    public static String convertMilisec_to_date(long acquisition_date) {
        Date date = new Date(acquisition_date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(date);
    }


    // for testing purposes
    public static void main(String[] args) {
        // TODO: Check inventory.json is supposed to be case sensitive
        // (download from d2l and compare to this inventory.json if I forget what this means)

        try {
            System.out.println("Select an inventory file to process");
            String input_filePath = Objects.requireNonNull(selectJsonFile()).toString();
            System.out.println("Create a name and choose location to save output file");
            String output_filePath = Objects.requireNonNull(selectJsonFile()).toString();

            // input file
            File file = new File(input_filePath);
            if (!file.exists() || file.length() == 0) {
                throw new ReadWriteException("The file does not exist or is empty.");
            }



            JSONIO jReadExample = new JSONIO(input_filePath, 'r');

            JSONIO jWriteExample = new JSONIO(output_filePath, 'w');
            String[][] read = jReadExample.read();
            if (jWriteExample.write(read) == read.length) {
                System.out.println("Added all files.");

            }
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }

    }
}