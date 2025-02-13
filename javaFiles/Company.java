package javaFiles;

import java.util.ArrayList;

// Instance variables to store company information
public class Company {
    private String company_id;
    private String company_name;
    private ArrayList<Dealership> list_dealerships;


    // Constructor to initialize the company with an ID and name
    public Company(String company_id, String company_name) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.list_dealerships = new ArrayList<>();  //Iniatilizes empty array of dealerships
    }

    // Method to add a dealership to the company's list of dealerships
    public void add_dealership(Dealership dealership) {list_dealerships.add(dealership);}

    // Method to get the list of all dealerships associated with the company
    public ArrayList<Dealership> get_list_dealerships() {return list_dealerships;}

    // Method to find a dealership by its unique dealer ID
    public Dealership find_dealership(String dealer_id) {
        for (Dealership dealership : list_dealerships) {
            if (dealership.getDealerId().equals(dealer_id)) {
                return dealership;
            }
        }
        //Return null if no dealership us found.
        return null;
    }

    // Getter method to retrieve the company ID
    public String getCompanyId() {return company_id;}

    // Getter method to retrieve the company name
    public String getCompanyName() {return company_name;}

}
