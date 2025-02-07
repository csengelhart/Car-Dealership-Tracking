package javaFiles;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        
        // Retrieves data from the JSON file
        
        String[][] carInventory = getInventory("inventory.json");
        // **REMOVE LATER**
        // 0.dealership_id 
        // 1.vehicle_type 
        // 2.vehicle_manufacturer 
        // 3.vehicle_model 
        // 4.vehicle_id 
        // 5.price 
        // 6.acquisition_date
        
        // Creates Company object
        Company company = new Company("c_ID", "c_Name");
        
        // Creates list of unique dealership IDs from carInventory.
        ArrayList<String> dealershipList = new ArrayList<>();
        for (String[] dealership : carInventory) {
            if (!dealershipList.contains(dealership[0])) {
                dealershipList.add(dealership[0]);
            }
        }

        // Creates dealerships objects.
        // Adds dealerships to company.
        for (String dealership : dealershipList) {
            Dealership d = new Dealership(dealership);
            company.add_dealership(d);
        }

        // Creates vehicles based on vehicle type
        for (String[] vehicle : carInventory) {
            Vehicle v;
            switch (vehicle[1]) {
                case "suv":
                    v = new SUV();
                    break;
                case "sedan":
                    v = new Sedan();
                    break;
                case "pickup":
                    v = new Pickup();
                    break;
                case "sports car":
                    v = new Sports_Car();
                    break;
                default:
                    return;
            }

            v.setVehicleManufacturer(vehicle[2]);
            v.setVehicleModel(vehicle[3]);
            v.setVehicleId(vehicle[4]);
            v.setVehiclePrice(Double.parseDouble(vehicle[5]));
            v.setAcquisitionDate(Long.parseLong(vehicle[6]));
            
            // TODO: Separate this out.  
            // Compare Dealership ID with the Vehicle ID.  Add Vehicle to cooresponding Dealership.
            //  Idea - Track vehicles that have not been added due to Dealership status (not accepting = true)
            //       - Check dealership.getStatus_AcquiringVehicles() first.
            //       - Create a list of vehicles not added.
            Dealership dealership = company.find_dealership(vehicle[0]);
            if (dealership != null) {
                dealership.add_incoming_vehicle(v);
            }
        }
        // Test: Print all vehicle objects for each dealership
        for (Dealership dealership : company.get_list_dealerships()) {
            System.out.println("Dealership ID: " + dealership.getDealerId());
            System.out.println(dealership.getInventory_Vehicles());
        }
        
        // TODO: Write vehicle inventory from dealership to JSON file.




    }
    
    // Method to retrieve data from a JSON file
    private static String[][] getInventory(String filePath) {
        try {
            JSONIO jReadExample = new JSONIO(filePath, 'r');
            return jReadExample.read();
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}