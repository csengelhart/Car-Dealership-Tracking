package javaFiles;

/**
 * Sedan is a child class of Vehicle, it calls Vehicle class's constructor
 * with the string "Sedan" , which then sets a Sedan object's vehicle_type to "Sedan"
 *
 * @author Christopher Engelhart
 */

public class Sedan extends Vehicle
{
    /**
     * Sedan's constructor calls upon its parent class Vehicle's constructor.
     * Sedan's constructor passes the String "Sedan" to Vehicle's constructor.
     */
    Sedan()
    {
        super("Sedan");
    }
}
