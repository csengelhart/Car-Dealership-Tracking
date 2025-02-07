package javaFiles;

/**
 * Pickup is a child class of Vehicle, it calls Vehicle class's constructor
 * with the string "Pickup" , which then sets a Pickup object's vehicle_type to "Pickup"
 *
 * @author Christopher Engelhart
 */

public class Pickup extends Vehicle
{
    /**
     * Pickup's constructor calls upon its parent class Vehicle's constructor.
     * Pickup's constructor passes the String "Pickup" to Vehicle's constructor.
     */
    public Pickup()
    {
        super("Pickup");
    }

}
