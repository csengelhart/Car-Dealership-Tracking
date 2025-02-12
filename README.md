# Car-Dealership-Tracking
A tracking system for a company that owns multiple car dealerships.
## Requirements
This program requires the following modules:

-JSON.simple
https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1

Note: You need to put json-simple-1.1.1.jar in your CLASSPATH before compiling and running the program.

## Configuration
For maven projects the following dependency needs to be included in the pom.xml file:


```Markdown
<dependency>
    <groupId>com.googlecode.json-simple</groupId>
        <artifactId>json-simple</artifactId>
    <version>1.1.1</version>
</dependency>
```

## Installation
- Navigate to the directory where the project is located.
- Add the json-simple-1.1.1.jar file to the CLASSPATH.

## Usage
The program provides a menu-driven interface for managing vehicles for a company's car dealerships. The user is provided with the following options:

1. **Send vehicles to dealership**
    - If there are vehicles in the queue, they will be sent to the respective dealerships.

2. **Check pending vehicle deliveries**
    - Displays the list of vehicles pending delivery and their associated dealerships.
    - Shows whether each dealership is currently accepting vehicles.

3. **Change dealership vehicle receiving status**
    - Allows the user to enable or disable vehicle receiving for a specific dealership.
    - Prompts the user to enter the dealership ID and then choose to enable or disable vehicle receiving.

4. **Write dealership inventory to file**
    - Prompts the user to select the file path for saving the data.
    - Writes the current inventory of all dealerships to the JSON file.

5. **Read a JSON file**
    - Prompts the user to select the file path for a JSON file containing vehicle data.
    - Reads vehicle data from the JSON file and populates the inventory.

6. **Print Company Inventory**
    - Displays the dealership ID and details of each vehicle at that location.

7. **Exit program**
    - Exits the program.

## Authors
- Dylan Browne
- Chris Engelhart
- Mason Day
- Patrick McLucas