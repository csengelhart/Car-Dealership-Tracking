package javaFiles;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        String[] keys = JSONIO.getKeys();

        String dIDKey           = keys[0];
        String vTypeKey         = keys[1];
        String manufacturerKey  = keys[2];
        String modelKey         = keys[3];
        String vIDKey           = keys[4];
        String priceKey         = keys[5];
        String acquisitionKey   = keys[6];

        // Retrieves data from the JSON file

        String filePath = JSONIO.selectJsonFilePath();

        List<Map<String, Object>> carInventory = getInventory(filePath);
        
        // Creates Company object
        Company company = new Company("c_ID", "c_Name");

        assert carInventory != null;
        
        // Creates list of unique dealership IDs from carInventory.
        List<String> dealershipList = new ArrayList<>();
        for (Map<String, Object> dealership : carInventory) {
            if (!dealershipList.contains((String) dealership.get(dIDKey))) {
                dealershipList.add( (String) dealership.get(dIDKey) );
            }
        }

        // Creates dealerships objects.
        // Adds dealerships to company.
        for (String dealership : dealershipList) {
            Dealership d = new Dealership(dealership);
            company.add_dealership(d);
        }

        // Creates vehicles based on vehicle type
        for (Map<String, Object> vehicle : carInventory) {
            Vehicle v;
            switch ( (String) vehicle.get(vTypeKey) ) {
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

            v.setVehicleManufacturer((String) vehicle.get(manufacturerKey));
            v.setVehicleModel((String)vehicle.get(modelKey));
            v.setVehicleId((String)vehicle.get(vIDKey));
            v.setVehiclePrice((double) (Long) vehicle.get(priceKey));
            v.setAcquisitionDate((Long)vehicle.get(acquisitionKey));
            
            // TODO: Separate this out.  
            // Compare Dealership ID with the Vehicle ID.  Add Vehicle to corresponding Dealership.
            //  Idea - Track vehicles that have not been added due to Dealership status (not accepting = true)
            //       - Check dealership.getStatus_AcquiringVehicles() first.
            //       - Create a list of vehicles not added.
            Dealership dealership = company.find_dealership((String) vehicle.get(dIDKey));
            if (dealership != null) {
                dealership.add_incoming_vehicle(v);
            }
        }
        // Test: Print all vehicle objects for each dealership
        for (Dealership dealership : company.get_list_dealerships()) {
            System.out.println("Dealership ID: " + dealership.getDealerId());
            for (Vehicle v : dealership.getInventory_Vehicles()) {
                System.out.print("ID: " + v.getVehicleId() + "\t");
                System.out.print("Manufacturer: " + v.getVehicleManufacturer() + "\t");
                System.out.print("Model: " + v.getVehicleModel() + "\t");
                System.out.print("Price: " + v.getVehiclePrice() + "\t");
                System.out.println("Acquisition Date: " + v.getAcquisitionDate() + "\t");
            }
            System.out.println();
        }
        
        // TODO: Write vehicle inventory from dealership to JSON file.




    }
    
    // Method to retrieve data from a JSON file
    private static List<Map<String, Object>> getInventory(String filePath) {
        try {
            JSONIO jReadExample = new JSONIO(filePath, 'r');
            return jReadExample.read();
        } catch (ReadWriteException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

}