package javaFiles;

/**
 * SUV is a child class of Vehicle, it calls Vehicle class's constructor
 * with the string "SUV" , which then sets an SUV object's vehicle_type to "SUV"
 *
 */

public class SUV extends Vehicle
{
    /**
     * SUV's constructor calls upon its parent class Vehicle's constructor.
     * SUV's constructor passes the String "SUV" to Vehicle's constructor.
     */
    public SUV()
    {
        super("SUV");
    }

}