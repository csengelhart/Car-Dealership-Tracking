package javaFiles;

import java.util.ArrayList;

/*  Creates Dealership object with the following attributes:
        Dealership ID (String)      (Required during instantiation)
        Vehicle acquisition status  (default: ENABLED)
        Vehicle inventory           (default: empty)

    Author: Patrick McLucas */

public class Dealership {
    private final String dealer_id;
    private final ArrayList<Vehicle> vehicle_inventory;
    private boolean receiving_vehicle; 

    // Instantiation requires dealer_ID
    public Dealership(String dealer_id) {
        this.dealer_id = dealer_id;
        this.receiving_vehicle = true;
        vehicle_inventory = new ArrayList<>();
    }

    // Returns Dealer ID
    public String getDealerId () {
        return dealer_id;
    }

    // Provides vehicle acquisition status
    public Boolean getStatus_AcquiringVehicles() {
        return receiving_vehicle;
    }

    // Provides list of vehicles at the dealership
    public ArrayList<Vehicle> getInventory_Vehicles() {
        return vehicle_inventory;
    }

    // ENABLES vehicle acquisition.
    public void enable_receiving_vehicle() {
        this.receiving_vehicle = true;
    }

    // DISABLES vehicle acquisition.
    public void disable_receiving_vehicle() {
        this.receiving_vehicle = false;
    }

    // Method for adding new vehicles to the dealership.
    public void add_incoming_vehicle(Vehicle newVehicle) {
        // Checks if the dealership is accepting new vehicles.
        if (!receiving_vehicle) {
            System.out.println("Dealership " + this.dealer_id + " is not accepting new vehicles at this time.");
            System.out.println("Vehicle ID: " + newVehicle.getVehicleId() + " was not added to Dealership: " + this.dealer_id +".");
            return; // Exits method if the dealership is not accepting new vehicles.
        } 
        
        // Checks if the new vehicle is already located at the dealership. 
        for (Vehicle vehicle : vehicle_inventory) {
            if (vehicle.getVehicleId().equals(newVehicle.getVehicleId())) {
                System.out.println("This vehicle is already located at the dealership.");
                System.out.println("Vehicle ID: " + newVehicle.getVehicleId() + " was not added to Dealership: " + this.dealer_id + ".");
                return; // Exits method if the vehicle already exists at the dealership
            }
        }
        this.vehicle_inventory.add(newVehicle);
    }
}


