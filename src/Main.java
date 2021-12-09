import Models.Item;
import Models.ItemInformation;
import com.google.gson.Gson;

import java.io.*;
import java.util.*;

public class Main {

    //location of user orders
    public static String path = "C:\\Users\\nsump\\Documents\\CSProject\\";

    /**
     *
     * @param args args
     */
    public static void main(String[] args){
        menu();
    }

    /**
     * Main menu loop
     */
    public static void menu(){
        Scanner keyboard = new Scanner(System.in);
        int input = 9999;
        //***
        //Main menu loop
        //***
        while (input != 0) {
            try {
                System.out.println("Create new order : 1");
                System.out.println("Load order       : 2");
                System.out.println("Delete order     : 3");
                System.out.println("Quit             : 0");

                System.out.print("Enter Selection: ");
                try {
                    input = keyboard.nextInt();
                }
                catch (Exception e) {
                    keyboard.nextLine();
                    input = 9999;
                }

                //CRUD methods for Creating/Reading/Deleting
                switch (input) {
                    case 1:
                        //Create
                        writeFile(setNewOrder());
                        break;
                    case 2:
                        //Read/Load
                        loadOrder();
                        break;
                    case 3:
                        //Delete
                        deleteOrder();
                        break;
                    case 0:
                        //Quit
                        break;
                    default:
                        System.out.println("\n#### Not a valid selection ####\n");
                }

            }
            catch (Exception e) {
                System.out.println("\n#### Unhandled error in main loop ####\n");
            }
        }
    }

    /**
     * Display order by ID
     */
    public static void displayFiles() {
        String[] pathnames;
        File f = new File(path);
        pathnames = f.list();
        // For each pathname in the pathnames array
        assert pathnames != null;
        for (String pathname : pathnames) {
            // Print the names of files and directories
            System.out.println("Order ID: " + pathname.substring(0, pathname.lastIndexOf(".")));
        }
    }

    /**
     * Load order by ID
     */
    public static void loadOrder(){

        String input;
        Scanner keyboard = new Scanner(System.in);
        Gson gson = new Gson();

        displayFiles();


        System.out.print("Enter order ID from list: ");
        try{
            input = keyboard.nextLine();

            String content = new Scanner(new File(path + input + ".txt")).next();

            ItemInformation i = gson.fromJson(content, ItemInformation.class);

            System.out.println("ID: " + i.iD + "\nTax Rate: " + i.taxRate +
                    "\nItems: ");
            for (int j = 0; j < i.items.size(); j++) {
                System.out.println("\n#############################################\n");
                System.out.println("\tName: " + i.items.get(j).name);
                System.out.println("\tPrice: " + i.items.get(j).price);
                System.out.println("\n#############################################\n");

            }
        }
        catch (Exception e){
            keyboard.nextLine();
            System.out.println("\nInvalid Selection\n");
        }
    }

    /**
     * Delete order by ID
     */
    public static void deleteOrder() {
        displayFiles();
        String input;
        Scanner keyboard = new Scanner(System.in);


        System.out.print("Enter order ID from list: ");
        try{
            input = keyboard.nextLine();
            File f = new File(path + input + ".txt");
            f.delete();
        }
        catch (Exception e){
            keyboard.nextLine();
            System.out.println("\nInvalid Selection\n");
        }

    }

    /**
     * Collect new order information from user and save it to the ItemInformation model
     *
     * @return ItemInformation
     */
    public static ItemInformation setNewOrder() {
        ItemInformation itemInformation = new ItemInformation();
        Scanner keyboard = new Scanner(System.in);
        String itemName;
        itemName = "";
        double price;

        //query the latest order ID from (var path) directory
        itemInformation.iD = getLatestID();

        //while var itemName does not equal "done" collect user items
        while (!(Objects.equals(itemName.toLowerCase(), "done"))) {
            System.out.print("Enter item name (enter done to finish): ");
            itemName = keyboard.nextLine();

            //if itemName does equal "done" do not collect item price
            if (!(Objects.equals(itemName.toLowerCase(), "done"))) {
                System.out.print("Enter item price: ");
                price = keyboard.nextDouble();


                itemInformation.items.add(new Item(itemName, price));
                keyboard.nextLine();
            }

        }
        //collect tax rate after the user is done with list
        System.out.print("Enter tax rate: ");
        itemInformation.taxRate = keyboard.nextDouble();

        return itemInformation;
    }

    /**
     * serialize item class and write it to a JSON file
     * @param item ItemInformation
     */
    public static void writeFile(ItemInformation item) {

        //declare Gson library and serialize Models.ItemInformation item to a JSON string object
        Gson gson = new Gson();
        String json = gson.toJson(item);


        //create file
        try {
            File myObj = new File(path + item.iD + ".txt");
            //if the file does not exist create a new file with the filename as item.iD + ".txt"
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //Write to new file
        try {
            FileWriter myWriter = new FileWriter(path + item.iD + ".txt");
            myWriter.write(json);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Get latest order ID
     * @return latestID
     */
    public static int getLatestID() {
        String[] pathnames;
        List<Integer> index = new ArrayList<>();
        File f = new File(path);

        //collect a list of filenames from the path
        pathnames = f.list();

        //convert first part of filenames to int and add them to index list
        for (int i = 0; i < Objects.requireNonNull(pathnames).length; i++) {
            index.add(Integer.parseInt(pathnames[i].split("\\.")[0]));
        }

        //if there are no files add 0 to ArrayList to avoid "Index out of range error"
        if (pathnames.length < 1) {
            index.add(0);
        }
        return Collections.max(index) + 1;
    }
}
