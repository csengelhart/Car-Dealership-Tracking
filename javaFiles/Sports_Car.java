package javaFiles;

/** Sports_Car is a child class of Vehicle, it calls Vehicle class's constructor
 * with the string "Sports car" , which then sets a Sports_Car object's vehicle_type
 * to "Sports_car"
*/

public class Sports_Car extends Vehicle
{
    /**
     * Sports_car constructor calls upon its parent class Vehicle's constructor.
     * Sports_car constructor passes the String "Sports_car" to Vehicle's constructor.
     */
    Sports_Car()
    {
        super("Sports car");
    }
}
