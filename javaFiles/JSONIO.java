package javaFiles;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
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
        if (filePath.endsWith(".json")) {
            this.file = new File(filePath);
        } else {
            throw new ReadWriteException("filePath \"" + filePath +"\" is not a .json file. "
                                        + "Make sure to include .json at the end for filePath.");
        }
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
     * Takes a JSONObject and an output String[]. Fills the String array with the data
     * from the JSONObject in the same key order as keyOrder. If any keys are absent,
     * data is filled with null.
     *
     * @param jObj The JSONObject that data is being extracted from.
     * @param output The String[] that data is being added to.
     */
    private void readJSONObject(JSONObject jObj, String[] output) {
        for (int i = 0; i < keyOrder.length; i++) {
            String key = keyOrder[i];
            Object dataPoint = jObj.get(key);

            if (dataPoint == null) {
                System.out.println("Key \"" + key + "\" not in JSONObj, " +
                        "saving all JSONObject data from it as null in data String[][].");
                // clearing stored data for the object.
                for (int j = 0; j < keyOrder.length; j++) {
                    output[j] = null;
                }
                break;
            }

            if (key.equals("acquisition_date") || key.equals("price")) {
                output[i] = String.valueOf(dataPoint);
            } else {
                output[i] = (String) dataPoint;
            }
        }
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
        String[][] output =  new String[jArray.size()][keyOrder.length];

        for (int i = 0; i < jArray.size(); i++) {
            JSONObject jObj = (JSONObject)jArray.get(i);
            readJSONObject(jObj, output[i]);
        }

        return output;
    }

    /**
     * Takes a String[] of data in the order keyOrder and converts it to a JSONObject and returns it
     *
     * @param data The data of items to be ordered in a JSONObject wear the keys for the data
     *             correspond to the index of the key in keyOrder.
     * @return The newly created JSONObject
     */
    private JSONObject makeJSONObject(String[] data) {
        JSONObject jObj = new JSONObject();
        for (int i = 0; i < keyOrder.length; i++) {
            Object dataPoint = data[i];
            if (dataPoint == null) {return null;}
            if (keyOrder[i].equals("price") || keyOrder[i].equals("acquisition_date")) {
                dataPoint = Long.parseLong((String) dataPoint);
            }
            jObj.put(keyOrder[i], dataPoint);
        }
        return jObj;
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

        JSONArray jArray = new JSONArray();
        for (String[] carData : data) {
            if (carData.length == keyOrder.length) {
                JSONObject jObj = makeJSONObject(carData);
                if (jObj != null) {
                    jArray.add(jObj);
                    added++;
                }
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
        int result = fileChooser.showOpenDialog(null);
        return result == 0 ? fileChooser.getSelectedFile() : null;
    }

/*
    /**
     * Takes a long value that represents the number of milliseconds since 01Jan1970
     * and converts to a date in the format "dd-MM-yyyy"
     * @param acquisition_date long value that is the milliseconds since 01Jan1970
     * @return formatted String representation of acquisition date
     *
     * @author Christopher Engelhart
     *//*
    public static String convertMillisecondToDate(long acquisition_date) {
        Date date = new Date(acquisition_date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        return sdf.format(date);
    }
*/

    // for testing purposes
    public static void main(String[] args) {
        try {
            System.out.println("Select an inventory file to process");
            String input_filePath = Objects.requireNonNull(selectJsonFile()).toString();

            // input file
            File file = new File(input_filePath);
            if (!file.exists() || file.length() == 0) {
                throw new ReadWriteException("The file does not exist or is empty.");
            }

            JSONIO jReadExample = new JSONIO(input_filePath, 'r');
            String[][] read = jReadExample.read();

            System.out.println("Create a name and choose location to save output file");
            String output_filePath = Objects.requireNonNull(selectJsonFile()).toString();

            JSONIO jWriteExample = new JSONIO(output_filePath, 'w');

            int jObjsRead = jWriteExample.write(read);
            if (jObjsRead == read.length) {
                System.out.println("Added all files.");
            } else {
                System.out.println("Added " + jObjsRead + "/" + read.length + " items.");
            }
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }

    }
}