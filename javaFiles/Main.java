package javaFiles;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        String filePath;
        List<Map<String, Object>> carInventory = null;
        Company company = new Company("c_ID", "c_Name");
        Map<Vehicle, Dealership> vehicleToDealershipMap = new HashMap<>();
        boolean filepath_empty = true; // flag variable to track whether a file has already been read

        while (true) {

            /** Should not be needed, when a user first opens the program,
             *  they can just choose to load a file in the action loop.
             *  They also need to be able to open multiple files.
             * */
            /*
            if (filepath_empty){
                System.out.print("Enter the JSON file name: ");
                filePath = scanner.nextLine();

                // Verifies and corrects file extension for .json
                filePath = verifyFileExtension(filePath);

                // Checks if the .json file exists. Prompts user for new file name if not found.
                if (!fileExists(filePath)) {
                    System.out.println("File \"" + filePath + "\" does not exist.");
                    System.out.println("Try another file(Y/N)? ");
                    do {
                        userInput = scanner.nextLine();
                    } while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n"));

                    if (userInput.equalsIgnoreCase("y")) {
                        continue;
                    } else {
                        System.out.println("Goodbye.");
                        break;
                    }

                }

                System.out.println("Opening file... " + filePath);

                // Retrieves data from the JSON file
                carInventory = getInventory(filePath);

                // Creates Company object
                //Company company = new Company("c_ID", "c_Name");

                // Checks for new dealerships. Adds new dealerships to company.
                populateDealerships(carInventory, company);

                // Output dealership list // TEST
                System.out.println("Current dealerships...\n" + company.get_list_dealerships());

                // Populates vehicle attributes. Returns Map with vehicle(key) to dealership(value) associations.
                vehicleToDealershipMap = PopulateVehicleInformation(carInventory, company);

                // Output key and value associations // TEST
                System.out.println("Vehicle destinations...");
                for (Map.Entry<Vehicle, Dealership> entry : vehicleToDealershipMap.entrySet()) {
                    Vehicle vehicle = entry.getKey();
                    Dealership dealership = entry.getValue();
                    System.out.println("Vehicle ID: " + vehicle.getVehicleId() + " -> Dealership ID: " + dealership.getDealerId());
                }

                // while(true) {
                // TODO: Prompt user for the following:

                filepath_empty = false;
            } */
            // while(true) {
            // TODO: Prompt user for the following:

            System.out.println(
                    "Select one of the following actions: \n" +
                            "1) Send vehicles to dealership.\n" +
                            "2) Check pending vehicle deliveries. \n" +
                            "3) Change dealership vehicle receiving status. \n" +
                            "4) Write dealership inventory to file.\n" +
                            "5) Read a json file.\n" +
                            "6) Exit program."
            );
            userInput = scanner.nextLine();

            switch (userInput) {
                case "1":
                    // TODO: Implement sending vehicles to dealership
                    System.out.println("Sending vehicles to dealership...");
                    break;
                case "2":
                    // TODO: Implement checking pending vehicle deliveries
                    System.out.println("Checking pending vehicle deliveries...");
                    break;
                case "3":
                    System.out.println("Changing dealership vehicle receiving status...");


                    Dealership dealer = null; // will hold Dealership object
                    boolean validDealership = false; // controls loop to find dealership

                    while (!validDealership) {
                        System.out.println("Enter the ID of the dealership or back to return to menu:");
                        userInput = scanner.nextLine();

                        // If user enters "exit", go back to the main menu
                        if (userInput.equalsIgnoreCase("back")) {
                            System.out.println("Returning to the main menu...");

                            break; // Exit the current while loop and return to the main menu
                        }

                        // Try to find the dealership
                        dealer = company.find_dealership(userInput);

                        if (dealer != null) {
                            validDealership = true; // Dealership found
                        } else {
                            // Dealership not found, prompt user to retry or exit
                            System.out.println("Dealership ID not found.");
                            System.out.println("Would you like to try again or return to the main menu? (Enter 'try' to retry, 'exit' to go back): ");
                            String retryInput = scanner.nextLine();

                            if (retryInput.equalsIgnoreCase("exit")) {
                                System.out.println("Returning to the main menu...");

                                break; // Exit the current while loop and return to the main menu
                            }
                            // Otherwise, the loop will continue to prompt for a valid dealership ID
                        }
                    }

                    // Proceed with enabling or disabling the vehicle receiving status once a valid dealership is found
                    if (dealer != null) {
                        System.out.println("Enable or disable vehicle receiving status for dealership " + userInput + "? (Enter 'enable' or 'disable')");
                        userInput = scanner.nextLine();

                        if (userInput.equalsIgnoreCase("enable")) {
                            // Check if the dealership's vehicle receiving status is already enabled
                            if (dealer.getStatus_AcquiringVehicles()) {
                                System.out.println("Dealership " + userInput + " is already set to receive vehicles.");
                            } else {
                                // Enable vehicle receiving for the dealership
                                dealer.enable_receiving_vehicle();
                                System.out.println("Vehicle receiving status for dealership " + userInput + " has been enabled.");
                            }
                        } else if (userInput.equalsIgnoreCase("disable")) {
                            // Disable the vehicle receiving status
                            if (!dealer.getStatus_AcquiringVehicles()) {
                                System.out.println("Dealership " + userInput + " is already set to not receive vehicles.");
                            } else {
                                dealer.disable_receiving_vehicle();
                                System.out.println("Vehicle receiving status for dealership " + userInput + " has been disabled.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter 'enable' or 'disable'.");
                        }
                    }

                    // After completing the dealership status change process, return to the main menu
                    continue; // Exit Case 3 and go back to the main menu


                case "4":
                    // TODO: Implement writing dealership inventory to file
                    writeData(carInventory, scanner);
                    System.out.println("Writing dealership inventory to file...");
                    continue;
                case "5":
                    // TODO: Implement reading another JSON file
                    carInventory = readData(scanner);
                    System.out.println("Reading JSON file...");
                    continue;
                case "6":
                    System.out.println("Exiting program...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid input. Please select a valid option.");
                    continue;
            }
            //      Send to dealership, enable/disable receiving, save to file, read another file, exit program.

            // TODO: Send vehicles to dealerships
            ArrayList<Vehicle> vehiclesAddedList = new ArrayList<>();
            for (Map.Entry<Vehicle, Dealership> vehicleToDealership : vehicleToDealershipMap.entrySet()) {
                if (vehicleToDealership.getValue().getStatus_AcquiringVehicles()) {
                    vehicleToDealership.getValue().add_incoming_vehicle(vehicleToDealership.getKey());
                    vehiclesAddedList.add(vehicleToDealership.getKey());
                }
            }
            // Removes vehicles that were successfully added to a dealership.
            // vehicleToDealershipMap provides a list of vehicles not added.
            // This can be used to retry adding vehicles when the dealership enables receiving vehicles.
            for (Vehicle vehicle : vehiclesAddedList) {
                vehicleToDealershipMap.remove(vehicle);
            }

            // TODO: Enable/disable dealership accepting vehicles
            //      company.find_dealership("12513").disable_receiving_vehicle();
            //      company.find_dealership("12513").enable_receiving_vehicle();

            // TODO: Write vehicle inventory from dealership to JSON file.
            //      Implement writeInventory method
            // }
            break;
        }
        scanner.close();
    }

    // Method: Verifies that the filename provided exists
    private static boolean fileExists (String filePath) {
        filePath = verifyFileExtension(filePath);
        return Files.exists(Paths.get(filePath));
    }

    // Method: Verifies and corrects file extension to include .json
    private static String verifyFileExtension (String filePath) {
        if (filePath.endsWith(".json")) {
            return filePath;
        }

        int lastDotIndex = filePath.lastIndexOf('.');
        if (lastDotIndex != -1) {
            filePath = filePath.substring(0, lastDotIndex) + ".json";
        } else {
            filePath += ".json";
        }

        return filePath;
    }

    private static JSONIO openFile(char mode, Scanner sc) {
        String path;
        JSONIO jsonio = null;
        char userInput;

        try {
            mode = JSONIO.getMode(mode);
        } catch (ReadWriteException e) {
            System.out.println("Mode '" + mode + "' is not valid, returning null.");
            return null;
        }

        do {
            System.out.print("Choose Path: ");
            path = JSONIO.selectJsonFilePath();
            try {
                if (path != null) {
                    System.out.println(path);
                    jsonio = new JSONIO(path, mode);
                } else {
                    System.out.println("File chooser closed. No file opened.");
                    return null;
                }
            } catch (ReadWriteException e) {
                System.out.print("Path \"" + path + "\" is not a valid path.\n" +
                        "Enter new path ('y' / 'n'): ");
                userInput = Character.toLowerCase(sc.next().charAt(0));
                while (userInput != 'y' && userInput != 'n') {
                    System.out.println("Invalid input, try again (only 'y' or 'n' valid): ");
                    userInput = Character.toLowerCase(sc.next().charAt(0));
                }
                if (userInput == 'n') {return null;}
            }
        } while (jsonio == null);
        return jsonio;
    }

    private static List<Map<String, Object>> readData(Scanner sc) {
        JSONIO jsonio = openFile('r', sc);
        if (jsonio == null) {return null;}
        try {
            return jsonio.read();
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void writeData(List<Map<String, Object>> data, Scanner sc) {
        if (data == null) {
            System.out.println("No data to write.");
            return;
        }
        JSONIO jsonio = openFile('w', sc);
        if (jsonio == null) {return;}
        try {
            jsonio.write(data);
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }
    }

    // Method: Checks for new dealerships. Adds new dealerships to company.
    private static void populateDealerships(List<Map<String, Object>> inventory, Company company) {
        for (Map<String, Object> dealership : inventory) {
            if (company.find_dealership((String) dealership.get(JSONIO.getDealIDKey())) == null) {
                Dealership d = new Dealership((String) dealership.get(JSONIO.getDealIDKey()));
                company.add_dealership(d);
            }
        }

    }

    // Method: Populates vehicle attributes. Returns HashMap with vehicle(key) to dealership(value) associations.
    private static Map<Vehicle, Dealership> PopulateVehicleInformation(List<Map<String, Object>> inventory, Company company) {
        Map<Vehicle, Dealership> vehicleToDealershipMap = new HashMap<>();
        for (Map<String, Object> vehicle : inventory) {
            Vehicle currVehicle = null;
            boolean vehicleAdded = true;
            switch ( (String)vehicle.get(JSONIO.getTypeKey()) ) {
                case "suv":
                    currVehicle = new SUV();
                    break;
                case "sedan":
                    currVehicle = new Sedan();
                    break;
                case "pickup":
                    currVehicle = new Pickup();
                    break;
                case "sports car":
                    currVehicle = new Sports_Car();
                    break;
                default:
                    System.out.println("\"" + vehicle.get(JSONIO.getTypeKey()) +
                            "\" is not a supported vehicle type. " +
                            "Vehicle ID: " + vehicle.get(JSONIO.getVehicleIDKey()) + "was not added");
                    vehicleAdded = false;
                    break;
            }
            // Checks if the vehicle was created and populates remaining attributes.
            if (vehicleAdded && currVehicle != null) {
                currVehicle.setVehicleManufacturer( (String) vehicle.get(JSONIO.getManufacturerKey()) );
                currVehicle.setVehicleModel( (String) vehicle.get(JSONIO.getModelKey()) );
                currVehicle.setVehicleId( (String) vehicle.get(JSONIO.getVehicleIDKey()) );
                currVehicle.setVehiclePrice( (long) vehicle.get(JSONIO.getPriceKey()) );
                currVehicle.setAcquisitionDate( (long) vehicle.get(JSONIO.getDateKey()) );

                Dealership dealership = company.find_dealership( (String) vehicle.get(JSONIO.getDealIDKey()) );
                if (dealership != null) {
                    vehicleToDealershipMap.put(currVehicle, dealership);
                } else {
                    System.out.println("Unable to map Vehicle ID: " + vehicle.get(JSONIO.getVehicleIDKey())
                            + " to " + vehicle.get(JSONIO.getDealIDKey()) + ". " +
                            "Dealership " + vehicle.get(JSONIO.getDealIDKey()) + " does not exist.");
                }
            }
        }
        return vehicleToDealershipMap;
    }

    // Method: Reads data from a JSON file.
    private static List<Map<String, Object>> getInventory(String filePath) {
        try {
            JSONIO jsonData = new JSONIO(filePath, 'r');
            return jsonData.read();
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Method: Writes data to a JSON file.
    private static void writeInventory(Dealership dealership) {
        // TODO: Implement writing .JSON file for dealership inventory. using JSONIO class.
    }

}