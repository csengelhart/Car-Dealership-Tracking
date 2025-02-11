package javaFiles;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        Map<Vehicle, String> carInventory = new HashMap<>(); // Vehicle, dealershipID
        Company company = new Company("c_ID", "c_Name");
        Map<Vehicle, Dealership> vehicleToDealershipMap = new HashMap<>();

        readData(scanner, carInventory, company);
        if (carInventory.isEmpty()) {
            System.out.println("No json file read. No data in company yet.");
        } else {
            System.out.println("Data loaded to queue, but not written to company");
        }

        while (true) {
            // Prompt user for the following:
            System.out.println(
                    "\nSelect one of the following actions: \n" +
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
                    // if carInventory is empty, print message and return to menu
                    if (carInventory.isEmpty()) {
                        System.out.println("No vehicles in queue, nothing added.");
                        continue;
                    }

                    writeCompanyData(company, carInventory);
                    System.out.println("Sending vehicles to dealership...");
                    continue;
                case "2":
                    // TODO: Implement checking pending vehicle deliveries
                    printPending(carInventory, company);
                    System.out.println("Checking pending vehicle deliveries...");
                    continue;
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
                            System.out.println("Would you like to try again or return to the main menu? (Enter any key to retry or 'exit' to go back): ");
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
                    int itemsWritten =  writeData(getCompanyData(company), scanner);
                    System.out.println("Wrote " + itemsWritten + " items to file");
                    continue;
                case "5":
                    // TODO: Implement reading another JSON file
                    readData(scanner, carInventory, company);
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


    /**
     * Opens a JSON file based on the given mode and user input.
     *
     * This method allows the user to choose a JSON file and opens it in the specified mode (read or write).
     * It will validate the file path and mode, and repeatedly prompt the user for a new path if an invalid path is entered.
     * If a valid file path is found, a {@link JSONIO} instance is created with the path and mode passed to it and returned. If the user cancels the file chooser or
     * enters an invalid path multiple times, the method returns null.
     *
     * @param mode A character representing the mode in which the file should be opened ('r' for read, 'w' for write).
     * @param sc   A {@link Scanner} object used to read user input from the console for path selection and retry prompts.
     * @return     A {@link JSONIO} object representing the opened file, or null if no valid file path is selected.
     */
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


    /**
     * Populates an inventory map with vehicle data from a list of maps.
     *
     * This method iterates through a list of maps, each representing a vehicle's data.
     * For each vehicle data map, it retrieves dealership information, creates a new
     * Vehicle object, populates the vehicle's attributes, and adds the vehicle
     * to the inventory map, associating it with the dealership ID.
     *
     * @param inventory A {@link Map} where the key is a {@link Vehicle} object and the value is the dealership ID (as a String).
     *                  This map represents the inventory to be populated.
     * @param data      A {@link List} of {@link Map} objects, where each map represents
     *                  the data for a single vehicle.
     * @param company   The {@link Company} object to which the dealerships belong.  This is used
     *                  to find existing dealerships or create new {@link Dealership} objects.
     */
    private static void dataToInventory(Map<Vehicle, String> inventory, List<Map<String, Object>> data, Company company) {
        for (Map<String, Object> map: data) {
            Dealership dealership = company.find_dealership(JSONIO.getDealIDVal(map));
            if (dealership == null) {
                dealership = new Dealership(JSONIO.getDealIDVal(map));
                company.add_dealership(dealership);
            }
            Vehicle vehicle = createNewVehicle(
                    JSONIO.getTypeVal(map),
                    JSONIO.getVehicleIDVal(map)
            );
            if (vehicle == null) {
                continue;
            }
            vehicle.setVehicleId(JSONIO.getVehicleIDVal(map));
            vehicle.setVehicleManufacturer(JSONIO.getManufacturerVal(map));
            vehicle.setVehicleModel(JSONIO.getModelVal(map));
            vehicle.setVehicleId(JSONIO.getVehicleIDVal(map));
            vehicle.setVehiclePrice(JSONIO.getPriceVal(map));
            vehicle.setAcquisitionDate(JSONIO.getDateVal(map));

            inventory.put(vehicle, JSONIO.getDealIDVal(map));
        }
    }


    /**
     * Reads data from a JSON file and populates an inventory.
     *
     * This method opens a JSON file in "read" mode ('r') using the provided {@link Scanner}
     * and a {@link JSONIO} object. If the file cannot be opened (e.g., file not found, invalid
     * permissions), the method returns without doing anything. Otherwise, it reads the JSON
     * data, which is expected to be a list of maps, where each map represents a vehicle.
     * This data is then used to populate the provided inventory map and associate
     * vehicles with their respective dealerships within the given company.
     *
     * @param sc  A {@link Scanner} object used by {@link #openFile(char, Scanner)} to read user input from the console for path 
     *            selection and retry prompts.
     * @param inventory A {@link Map} where the key is a {@link Vehicle} object and the value
     *                  is the dealership ID (a String). This map represents the inventory
     *                  to be populated.  The dealership ID is used to associate the vehicle
     *                  with a specific dealership.
     * @param company The {@link Company} object that manages the dealerships. This object is used
     *                to find existing dealerships or create new {@link Dealership} objects if
     *                they don't already exist.
     */
    private static void readData(Scanner sc, Map<Vehicle, String> inventory, Company company) {
        List<Map<String, Object>> data = new ArrayList<>();
        JSONIO jsonio = openFile('r', sc);
        if (jsonio == null) {return;}
        try {
            data.addAll(jsonio.read());
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }

        dataToInventory(inventory, data, company);
    }


    /**
     * Writes data to the JSON file contained by {@link JSONIO} object.
     *
     * This method opens a JSON file contained in {@link JSONIO} object
     * in write ("w") mode and writes data in the form of a {@link List}
     * of {@link Map} which consists of String keys with Object values to the file.
     * If the file cannot be opened or an error occurs during the write operation, an error
     * message is printed to the console, and the method returns 0. If data is successfully
     * written, the method returns the number of entries written in the file.
     *
     * @param data A {@link List} of {@link Map} objects representing the data to be written
     *  *             to the JSON file. Each Map represents data from a vehicle.
     * @param sc A {@link Scanner} object used by {@link #openFile(char, Scanner)} to read user input from the console for path
     *      *            selection and retry prompts.
     * @return The number of items successfully written to the file. Returns 0 if no data
     *  *         is provided, the file cannot be opened, or an error occurs during writing.
     */
    private static int writeData(List<Map<String, Object>> data, Scanner sc) {
        if (data == null) {
            System.out.println("No data to write.");
            return 0;
        }
        JSONIO jsonio = openFile('w', sc);
        if (jsonio == null) {return 0;}
        try {
            return jsonio.write(data);
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }


    /**
     * Retrieves vehicle data for a given dealership.
     *
     * This method generates a list of maps, where each map represents a vehicle
     * in the specified dealership's inventory.  Each map contains key-value pairs
     * representing the vehicle's attributes.
     *
     * @param dealership A {@link Dealership} object whose vehicle data is to be retrieved.
     *                  Dealership objects hold a collection {@link Vehicle} objects.
      *@return {@link List} of {@link Map} objects where each Map object holds a specific vehicle
     *         and its data.(dealership ID, vehicle type, manufacturer, model,
     *         vehicle ID, price, and acquisition date) as key-value pairs. Method returns
     *         null if the Dealership object is empty.
     */
    private static List<Map<String, Object>> getDealershipData(Dealership dealership) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Vehicle vehicle: dealership.getInventory_Vehicles()) {
            Map<String, Object> map = new HashMap<>();
            map.put(JSONIO.getDealIDKey(), dealership.getDealerId());
            map.put(JSONIO.getTypeKey(), vehicle.getVehicleType());
            map.put(JSONIO.getManufacturerKey(), vehicle.getVehicleManufacturer());
            map.put(JSONIO.getModelKey(), vehicle.getVehicleModel());
            map.put(JSONIO.getVehicleIDKey(), vehicle.getVehicleId());
            map.put(JSONIO.getPriceKey(), vehicle.getVehiclePrice());
            map.put(JSONIO.getDateKey(), vehicle.getAcquisitionDate());
            list.add(map);
        }
        return list;
    }

    /**
     *
     *
     * @param company
     * @return
     */
    private static List<Map<String, Object>> getCompanyData(Company company) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Dealership dealership : company.get_list_dealerships()) {
            list.addAll(getDealershipData(dealership));
        }
        return list;
    }

    /**
     * This method processes the vehicle inventory and updates the dealership's incoming vehicles.
     * It checks each vehicle in the provided inventory, finds the corresponding dealership, and if
     * the dealership is acquiring vehicles, it adds the vehicle to the dealership's incoming
     * vehicle list. After processing, it removes the accepted vehicles from the inventory
     *
     * @param company The {@link Company} object that contains the dealerships
     * @param inventory A map containing vehicles as keys and the corresponding dealership ID (as Strings)
     *                  as values. The vehicles in the inventory will be processed and moved based on
     *                  the status of their respective dealerships.
     */
    private static void writeCompanyData(Company company, Map<Vehicle, String> inventory) {
        List<Vehicle> accepted = new ArrayList<>();
        for (Vehicle vehicle : inventory.keySet()) {
            Dealership dealership = company.find_dealership(inventory.get(vehicle));
            if (dealership.getStatus_AcquiringVehicles()) {
                accepted.add(vehicle);
            }
            dealership.add_incoming_vehicle(vehicle);
        }
        for (Vehicle vehicle : accepted) {
            inventory.remove(vehicle);
        }

    }

    private static void printPending(Map<Vehicle, String> inventory, Company company) {
        Dealership d;
        for (Vehicle vehicle : inventory.keySet()) {
            d = company.find_dealership(inventory.get(vehicle));
            String open;
            if (d == null) {
                open = " has not been initiated (will be initiated as accepting Vehicles).";
            } else if (d.getStatus_AcquiringVehicles()) {
                open = " is accepting Vehicles.";
            } else {
                open = " is not accepting Vehicles.";
            }

            System.out.println(vehicle);
            System.out.println("Dealership ID: " + inventory.get(vehicle) + open + "\n");
        }
    }

    // Method: Checks for new dealerships. Adds new dealerships to company.
    private static void populateDealerships(List<Map<String, Object>> inventory, Company company) {
        for (Map<String, Object> dealership : inventory) {
            if (company.find_dealership( JSONIO.getDealIDVal(dealership)) == null) {
                Dealership d = new Dealership(JSONIO.getDealIDVal(dealership));
                company.add_dealership(d);
            }
        }

    }

    private static Vehicle createNewVehicle(String vehicleType, String ID) {
        return switch (vehicleType) {
            case "suv" -> new SUV();
            case "sedan" -> new Sedan();
            case "pickup" -> new Pickup();
            case "sports car" -> new Sports_Car();
            default -> {
                System.out.println("\"" + vehicleType +
                        "\" is not a supported vehicle type. " +
                        "Vehicle ID: " + ID + "was not added");
                yield null;
            }
        };
    }

    // Method: Populates vehicle attributes. Returns HashMap with vehicle(key) to dealership(value) associations.
    private static Map<Vehicle, Dealership> PopulateVehicleInformation(List<Map<String, Object>> inventory, Company company) {
        Map<Vehicle, Dealership> vehicleToDealershipMap = new HashMap<>();
        for (Map<String, Object> vehicle : inventory) {
            Vehicle currVehicle = null;
            boolean vehicleAdded = true;
            currVehicle = createNewVehicle(
                    JSONIO.getTypeVal(vehicle),
                    JSONIO.getVehicleIDVal(vehicle));
            if (currVehicle == null) {vehicleAdded = false;}

            // Checks if the vehicle was created and populates remaining attributes.
            if (vehicleAdded && currVehicle != null) {
                currVehicle.setVehicleManufacturer( JSONIO.getManufacturerVal(vehicle) );
                currVehicle.setVehicleModel( JSONIO.getModelVal(vehicle) );
                currVehicle.setVehicleId( JSONIO.getVehicleIDVal(vehicle) );
                currVehicle.setVehiclePrice( JSONIO.getPriceVal(vehicle) );
                currVehicle.setAcquisitionDate( JSONIO.getDateVal(vehicle) );

                Dealership dealership = company.find_dealership( JSONIO.getDealIDVal(vehicle) );
                if (dealership != null) {
                    vehicleToDealershipMap.put(currVehicle, dealership);
                } else {
                    System.out.println("Unable to map Vehicle ID: " + vehicle.get(JSONIO.getVehicleIDKey())
                            + " to " + JSONIO.getDealIDVal(vehicle) + ". " +
                            "Dealership " + JSONIO.getDealIDVal(vehicle) + " does not exist.");
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