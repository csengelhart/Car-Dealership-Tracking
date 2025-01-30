/**
Vehicle is an abstract class that defines a set of common attributes
and behaviors for all vehicle types. This class serves as a blueprint for any
specific vehicle types that may extend it

*/

import java.util.Date;

public abstract class Vehicle {
    private String vehicleId;
    private String vehicleManufacturer;
    private String vehicleModel;
    private double vehiclePrice;
    private Date acquisitionDate;
    private String vehicleType; // Common field to all vehicle

    // Constructor
    public Vehicle(){

    }

    /** Method for each type of vehicle

        @param vehicle_type the vehicle_type of the specific vehicle class that will extend Vehicle
     */
    protected void setVehicleType(String vehicle_type)
    {
        this.vehicleType = vehicle_type;
    }

    // Getter methods for shared attributes

    public String getVehicleId() {
        return vehicleId;
    }

    public String getVehicleManufacturer() {
        return vehicleManufacturer;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public double getVehiclePrice() {
        return vehiclePrice;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }
}
