package javaFiles;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// **REMOVE LATER**
// 0.dealership_id
// 1.vehicle_type
// 2.vehicle_manufacturer
// 3.vehicle_model
// 4.vehicle_id
// 5.price
// 6.acquisition_date

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String userInput;
        String filePath;
        String[][] carInventory;

        while (true) {
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
            Company company = new Company("c_ID", "c_Name");

            // Checks for new dealerships. Adds new dealerships to company.
            populateDealerships(carInventory, company);

            // Output dealership list // TEST
            System.out.println("Current dealerships...\n" + company.get_list_dealerships());

            // Populates vehicle attributes. Returns HashMap with vehicle(key) to dealership(value) associations.
            HashMap<Vehicle, Dealership> vehicleToDealershipMap = PopulateVehicleInformation(carInventory, company);

            // Output key and value associations // TEST
            System.out.println("Vehicle destinations...");
            for (Map.Entry<Vehicle, Dealership> entry : vehicleToDealershipMap.entrySet()) {
                Vehicle vehicle = entry.getKey();
                Dealership dealership = entry.getValue();
                System.out.println("Vehicle ID: " + vehicle.getVehicleId() + " -> Dealership ID: " + dealership.getDealerId());
            }

            // while(true) {
            // TODO: Prompt user for the following:

            System.out.println(
                    "Select one of the following actions: \n" +
                            "1) Send vehicles to dealership.\n" +
                            "2) Check pending vehicle deliveries. \n" +
                            "3) Change dealership vehicle receiving status. \n" +
                            "4) Write dealership inventory to file.\n" +
                            "5) Read another json file.\n" +
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
                    // TODO: Implement changing dealership vehicle receiving status
                    System.out.println("Changing dealership vehicle receiving status...");
                    break;
                case "4":
                    // TODO: Implement writing dealership inventory to file
                    System.out.println("Writing dealership inventory to file...");
                    break;
                case "5":
                    // TODO: Implement reading another JSON file
                    System.out.println("Reading another JSON file...");
                    break;
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
                if (vehicleToDealership.getValue().getStatus_AcquiringVehicles() == true) {
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

    // Method: Checks for new dealerships. Adds new dealerships to company.
    private static void populateDealerships(String[][] inventory, Company company) {
        for (String[] dealership : inventory) {
            if (company.find_dealership(dealership[0]) == null) {
                Dealership d = new Dealership(dealership[0]);
                company.add_dealership(d);
            }
        }

    }

    // Method: Populates vehicle attributes. Returns HashMap with vehicle(key) to dealership(value) associations.
    private static HashMap<Vehicle, Dealership> PopulateVehicleInformation(String[][] inventory, Company company) {
        HashMap<Vehicle, Dealership> vehicleToDealershipMap = new HashMap<>();
        for (String[] vehicle : inventory) {
            Vehicle currVehicle = null;
            boolean vehicleAdded = true;
            switch (vehicle[1]) {
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
                    System.out.println("\"" + vehicle[1] + "\" is not a supported vehicle type. " +
                            "Vehicle ID: " + vehicle[4] + "was not added");
                    vehicleAdded = false;
                    break;
            }
            // Checks if the vehicle was created and populates remaining attributes.
            if (vehicleAdded && currVehicle != null) {
                currVehicle.setVehicleManufacturer(vehicle[2]);
                currVehicle.setVehicleModel(vehicle[3]);
                currVehicle.setVehicleId(vehicle[4]);
                currVehicle.setVehiclePrice(Double.parseDouble(vehicle[5]));
                currVehicle.setAcquisitionDate(Long.parseLong(vehicle[6]));

                Dealership dealership = company.find_dealership(vehicle[0]);
                if (dealership != null) {
                    vehicleToDealershipMap.put(currVehicle, dealership);
                } else {
                    System.out.println("Unable to map Vehicle ID: " + vehicle[4] + " to " + vehicle[0] + ". " +
                            "Dealership " + vehicle[0] + " does not exist.");
                }
            }
        }
        return vehicleToDealershipMap;
    }

    // Method: Reads data from a JSON file.
    private static String[][] getInventory(String filePath) {
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