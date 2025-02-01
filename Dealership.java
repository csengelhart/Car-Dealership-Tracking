import java.util.*;

/*  Creates Dealership object with the following attributes:
        Dealership ID (String)      (Required during instantiation)
        Vehicle acquisition status  (default: ENABLED)
        Vehicle inventory           (default: empty)

    Author: Patrick McLucas */

public class Dealership {
    private String dealer_id;
    private ArrayList<Vehicle> vehicle_inventory;
    private boolean receiving_vehicle; 

    // Instantiation requires dealer_ID
    public Dealership(String dealer_id) {
        setID(dealer_id);
        enable_receiving_vehicle();
        vehicle_inventory = new ArrayList<Vehicle>();
    }

    // Returns Dealer ID
    public String getID () {
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
        receiving_vehicle = true;
    }

    // DISABLES vehicle acquisition.
    public void disable_receiving_vehicle() {
        receiving_vehicle = false;
    }

    // Adds vehicles
    public void add_incoming_vehicle(Vehicle newVehicle) {
        if (receiving_vehicle == true) {
            // TODO: Implement - vehicle_inventory.add(newVehicle);
        } else {
            System.out.println("Dealership " + dealer_id + " is not accepting new vehicles at this time.");
            System.out.println("Vehicle ID: " + "newVehicle.getVehicleId()" + "not added"); //TODO: define "newVehicle.getVehicleId()"
        }
    }

    // Sets Dealer ID
    private void setID (String dealer_id) {
        this.dealer_id = dealer_id;
    }
}

