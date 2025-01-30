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

    /**
     * Sets the vehicle ID.
     *
     * @param vehicleId the unique identifier for the vehicle
     */
    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    /**
     * Sets the vehicle manufacturer.
     *
     * @param vehicleManufacturer the name of the vehicle's manufacturer
     */
    public void setVehicleManufacturer(String vehicleManufacturer) {
        this.vehicleManufacturer = vehicleManufacturer;
    }

    /**
     * Sets the vehicle model.
     *
     * @param vehicleModel the model name or number of the vehicle
     */
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    /**
     * Sets the vehicle price.
     *
     * @param vehiclePrice the price of the vehicle
     */
    public void setVehiclePrice(double vehiclePrice) {
        this.vehiclePrice = vehiclePrice;
    }

    /**
     * Sets the acquisition date of the vehicle.
     *
     * @param acquisitionDate the date the vehicle was acquired
     */
    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
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
