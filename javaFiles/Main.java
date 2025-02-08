package javaFiles;

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
            if (!filePath.endsWith(".json")) {
                int lastDotIndex = filePath.lastIndexOf('.');
                if (lastDotIndex != -1) {
                    filePath = filePath.substring(0, lastDotIndex) + ".json";
                } else {
                    filePath += ".json";
                }
            }

            // Retrieves data from the JSON file
            carInventory = getInventory(filePath);

            // Prompts user for another filename if unable to locate or read file.
            if (getInventory(filePath) == null) {
                userInput = "";
                while (!userInput.equalsIgnoreCase("y") && !userInput.equalsIgnoreCase("n")) {
                    System.out.println("Try another file(Y/N)? ");
                    userInput = scanner.nextLine();
                }
                
                if (userInput.equalsIgnoreCase("y")) {
                    continue;
                } else {
                    System.out.println("Goodbye.");
                    break;
                }
            }

            // Creates Company object
            Company company = new Company("c_ID", "c_Name");
            
            // Creates dealership if it does not already exist.
            // Then, adds new dealerships to the company.
            for (String[] dealership : carInventory) {
                if (company.find_dealership(dealership[0]) == null) {
                    Dealership d = new Dealership(dealership[0]);
                    company.add_dealership(d);
                }
            }

            // System.out.println(company.get_list_dealerships()); // TEST

            // Populates vehicle attributes.
            // Creates a map associating each vehicle to a dealership.
            HashMap<Vehicle, Dealership> vehicleToDealershipMap = new HashMap<>();
            for (String[] vehicle : carInventory) {
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
            
            // while(true) {
                // TODO: Prompt user for the following:
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
    }
    
    // Reads data from a JSON file.
    private static String[][] getInventory(String filePath) {
        try {
            JSONIO jsonData = new JSONIO(filePath, 'r');
            return jsonData.read();
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // Writes data to a JSON file.
    private static void writeInventory(Dealership dealership) {
        // TODO: Implement writing .JSON file for dealership inventory. using JSONIO class.
    }

}