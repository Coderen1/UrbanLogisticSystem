import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main class for the FastRoute Kayseri COMP206 semester project.
 *
 * Interactive operations menu (KYS-style): users can run warehouse and routing
 * operations manually while each menu item shows its data structure.
 */
public class Main {

    private static Graph roadNetwork;
    private static SinglyLinkedList masterRegistry;
    private static DoublyLinkedList intakeBuffer;
    private static PackageQueue deliveryQueue;
    private static PackageStack truckStack;
    private static AVLTree addressDirectory;

    private static Scanner scanner;
    private static int nextPackageNumber;
    private static int nextCustomerNumber;

    /**
     * Program entry point.
     *
     * @param args command-line arguments, not used in this project
     */
    public static void main(String[] args) {
        printBranding();
        scanner = new Scanner(System.in);
        initializeSystem();
        runOperationsMenu();
        scanner.close();
    }

    /**
     * Prints the FastRoute Kayseri welcome screen at program start.
     */
    private static void printBranding() {
        System.out.println("========================================");
        System.out.println("   Welcome to FastRoute Kayseri");
        System.out.println(" Urban Logistics & Distribution System");
        System.out.println("========================================");
        System.out.println("  Your trusted cargo partner in Kayseri");
        System.out.println("========================================");
        System.out.println();
    }

    /**
     * Creates all system objects and loads data from the required text files.
     */
    private static void initializeSystem() {
        System.out.println("Initializing system and loading data files...");
        System.out.println();

        roadNetwork = new Graph(100);
        masterRegistry = new SinglyLinkedList();
        intakeBuffer = new DoublyLinkedList();
        deliveryQueue = new PackageQueue();
        truckStack = new PackageStack();
        addressDirectory = new AVLTree();

        nextPackageNumber = 1;
        nextCustomerNumber = 1;

        if (loadGraphFromFile("mapData.txt", roadNetwork)) {
            System.out.println("mapData.txt loaded successfully.");
        }

        if (loadInitialPackagesFromFile("packageData.txt")) {
            System.out.println("packageData.txt loaded successfully.");
        }

        System.out.println();
        System.out.println("System ready.");
        System.out.println();
    }

    /**
     * Displays the operations menu and processes user selections.
     */
    private static void runOperationsMenu() {
        while (true) {
            printOperationsMenu();
            int choice = readIntInRange("Enter operation number (0-17): ", 1, 17);

            switch (choice) {
                case 1:
                    receiveDelivery();
                    break;
                case 2:
                    displayIntakeBuffer();
                    break;
                case 3:
                    registerDeliveryInMasterLog();
                    break;
                case 4:
                    displayMasterLog();
                    break;
                case 5:
                    processDeliveriesToQueue();
                    break;
                case 6:
                    displayDeliveryQueue();
                    break;
                case 7:
                    loadTruckFromQueue();
                    break;
                case 8:
                    displayTruckStack();
                    break;
                case 9:
                    deliverPackageFromTruck();
                    break;
                case 10:
                    createNewAddress();
                    break;
                case 11:
                    searchAddress();
                    break;
                case 12:
                    displayAllAddresses();
                    break;
                case 13:
                    addRoadConnection();
                    break;
                case 14:
                    displayCityMap();
                    break;
                case 15:
                    calculateShortestPathInteractive();
                    break;
                case 16:
                    displayMinimumSpanningTree();
                    break;
                case 17:
                    runAvlRotationTest();
                    break;
                default:
                    System.out.println("Invalid option.");
            }

            pauseForEnter();
        }
    }

    /**
     * Prints the main operations menu with data structure labels.
     */
    private static void printOperationsMenu() {
        System.out.println("========================================");
        System.out.println("   FASTROUTE KAYSERI - OPERATIONS MENU");
        System.out.println("========================================");
        System.out.println("1.  Receive a delivery                         (Doubly Linked List - insertAtTail())");
        System.out.println("2.  Display deliveries waiting               (Doubly Linked List - displayBuffer())");
        System.out.println("3.  Register delivery in master log         (Doubly Linked List removeFromHead() + Singly Linked List addRecord())");
        System.out.println("4.  Display daily master log                 (Singly Linked List - displayLog())");
        System.out.println("5.  Process deliveries to delivery queue     (Doubly Linked List removeFromHead() + Queue - enqueue())");
        System.out.println("6.  Display delivery queue                   (Queue - displayQueue())");
        System.out.println("7.  Load truck from queue                    (Queue - dequeue() + Stack - push())");
        System.out.println("8.  Display loaded deliveries on truck       (Stack - displayStack())");
        System.out.println("9.  Deliver / unload package from truck      (Stack - pop())");
        System.out.println("10. Create a new address                     (AVL Tree - insert())");
        System.out.println("11. Search address by neighborhood           (AVL Tree - search())");
        System.out.println("12. Display all addresses                    (AVL Tree - displayInOrder())");
        System.out.println("13. Add a new road connection                (Graph - addEdge())");
        System.out.println("14. Display city road map                    (Graph - displayGraph())");
        System.out.println("15. Calculate shortest path between areas    (Graph - calculateShortestPath())");
        System.out.println("16. Display minimum spanning tree            (Graph - calculateMST())");
        System.out.println("17. AVL balancing and rotation test          (AVL Tree - balance() + rotateLeft()/rotateRight())");
        System.out.println("0.  Exit");
        System.out.println("----------------------------------------");
    }

    // -------------------------------------------------------------------------
    // Package workflow operations
    // -------------------------------------------------------------------------

    /**
     * Receives a new delivery and inserts it into the intake buffer.
     * Method used: insertAtTail() on Doubly Linked List.
     */
    private static void receiveDelivery() {
        printOperationHeader("RECEIVE A DELIVERY", "Doubly Linked List - insertAtTail()");

        String destination = readNonEmptyLine("Enter destination neighborhood: ");
        String customerID = resolveCustomerID(destination);
        Package pkg = new Package(generatePackageID(), destination);

        System.out.println();
        System.out.println("Calling insertAtTail(Package pkg)...");
        intakeBuffer.insertAtTail(pkg);

        System.out.println();
        System.out.println("[SUCCESS] Package received and added to intake buffer.");
        System.out.println("Package ID  : " + pkg.packageID);
        System.out.println("Destination : " + pkg.destination);
        System.out.println("Customer ID : " + customerID);
    }

    /**
     * Displays all packages currently waiting in the intake buffer.
     * Method used: displayBuffer() on Doubly Linked List.
     */
    private static void displayIntakeBuffer() {
        printOperationHeader("INTAKE BUFFER", "Doubly Linked List - displayBuffer()");

        if (intakeBuffer.isEmpty()) {
            System.out.println("The intake buffer is currently empty.");
            return;
        }

        System.out.println("Packages waiting for processing:");
        System.out.println("----------------------------------------");
        intakeBuffer.displayBuffer();
    }

    /**
     * Registers a delivery in the master registry log.
     * Method used: addRecord() on Singly Linked List.
     */
    private static void registerDeliveryInMasterLog() {
        printOperationHeader("REGISTER DELIVERY IN MASTER LOG", "Singly Linked List - addRecord()");

        if (intakeBuffer.isEmpty()) {
            System.out.println("No packages in the intake buffer to register.");
            System.out.println("Use option 1 to receive a delivery first.");
            return;
        }

        Package pkg = intakeBuffer.removeFromHead();

        System.out.println("Registering package removed from intake buffer head:");
        pkg.displayPackage();
        System.out.println();
        System.out.println("Calling addRecord(Package pkg)...");
        masterRegistry.addRecord(pkg);

        System.out.println();
        System.out.println("[SUCCESS] Package registered in the master log.");
    }

    /**
     * Displays the full daily master registry log.
     * Method used: displayLog() on Singly Linked List.
     */
    private static void displayMasterLog() {
        printOperationHeader("DAILY MASTER LOG", "Singly Linked List - displayLog()");
        masterRegistry.displayLog();
    }

    /**
     * Moves a package from the intake buffer to the delivery queue.
     * Methods used: removeFromHead() and enqueue().
     */
    private static void processDeliveriesToQueue() {
        printOperationHeader(
                "PROCESS DELIVERIES TO QUEUE",
                "Doubly Linked List - removeFromHead() + Queue - enqueue()");

        if (intakeBuffer.isEmpty()) {
            System.out.println("No packages in the intake buffer to process.");
            return;
        }

        Package pkg = intakeBuffer.removeFromHead();

        System.out.println("Removed from intake buffer:");
        pkg.displayPackage();
        System.out.println();
        System.out.println("Calling enqueue(Package pkg)...");
        deliveryQueue.enqueue(pkg);

        System.out.println();
        System.out.println("[SUCCESS] Package moved to the standard delivery queue.");
    }

    /**
     * Displays all packages waiting in the delivery queue.
     * Method used: displayQueue() on Queue.
     */
    private static void displayDeliveryQueue() {
        printOperationHeader("DELIVERY QUEUE", "Queue - displayQueue()");
        deliveryQueue.displayQueue();
    }

    /**
     * Loads one package from the delivery queue onto the truck stack.
     * Methods used: dequeue() and push().
     */
    private static void loadTruckFromQueue() {
        printOperationHeader("LOAD TRUCK FROM QUEUE", "Queue - dequeue() + Stack - push()");

        if (deliveryQueue.isEmpty()) {
            System.out.println("The delivery queue is empty. Process deliveries first (option 5).");
            return;
        }

        Package pkg = deliveryQueue.dequeue();

        System.out.println("Dequeued from delivery queue:");
        pkg.displayPackage();
        System.out.println();
        System.out.println("Calling push(Package pkg)...");
        truckStack.push(pkg);

        System.out.println();
        System.out.println("[SUCCESS] Package loaded onto the truck.");
    }

    /**
     * Displays all packages currently loaded on the truck.
     * Method used: displayStack() on Stack.
     */
    private static void displayTruckStack() {
        printOperationHeader("TRUCK LOADING STACK", "Stack - displayStack()");
        truckStack.displayStack();
    }

    /**
     * Unloads one package from the truck for delivery.
     * Method used: pop() on Stack.
     */
    private static void deliverPackageFromTruck() {
        printOperationHeader("DELIVER PACKAGE FROM TRUCK", "Stack - pop()");

        if (truckStack.isEmpty()) {
            System.out.println("The truck is empty. Load packages first (option 7).");
            return;
        }

        Package pkg = truckStack.pop();

        System.out.println("Calling pop()...");
        System.out.println();
        System.out.println("[SUCCESS] Package delivered / unloaded from truck:");
        pkg.displayPackage();
    }

    // -------------------------------------------------------------------------
    // AVL Tree operations
    // -------------------------------------------------------------------------

    /**
     * Creates a new address record in the AVL address directory.
     * Method used: insert() on AVL Tree.
     */
    private static void createNewAddress() {
        printOperationHeader("CREATE NEW ADDRESS", "AVL Tree - insert()");

        String neighborhood = readNonEmptyLine("Enter neighborhood name: ");
        String customerID = readNonEmptyLine("Enter customer ID: ");

        System.out.println();
        System.out.println("Calling insert(\"" + neighborhood + "\", \"" + customerID + "\")...");
        addressDirectory.insert(neighborhood, customerID);

        System.out.println();
        System.out.println("[SUCCESS] Address record added to the AVL Tree.");
    }

    /**
     * Searches for a neighborhood in the AVL address directory.
     * Method used: search() on AVL Tree.
     */
    private static void searchAddress() {
        printOperationHeader("SEARCH ADDRESS", "AVL Tree - search()");

        String neighborhood = readNonEmptyLine("Enter neighborhood to search: ");

        System.out.println();
        System.out.println("Calling search(\"" + neighborhood + "\")...");
        System.out.println("----------------------------------------");
        addressDirectory.search(neighborhood);
    }

    /**
     * Displays all address records in sorted order.
     * Method used: displayInOrder() on AVL Tree.
     */
    private static void displayAllAddresses() {
        printOperationHeader("ALL ADDRESSES", "AVL Tree - displayInOrder()");
        addressDirectory.displayInOrder();
    }

    /**
     * Runs the AVL balancing and rotation proof using small test trees.
     */
    private static void runAvlRotationTest() {
        printOperationHeader("AVL BALANCING AND ROTATION TEST", "AVL Tree - balance(), rotateLeft(), rotateRight()");

        System.out.println("--- rotateLeft() test (insert A, B, C) ---");
        AVLTree leftRotationTest = new AVLTree();
        leftRotationTest.insert("A", "CUST_A");
        leftRotationTest.insert("B", "CUST_B");
        leftRotationTest.insert("C", "CUST_C");
        System.out.println("Tree after balancing:");
        leftRotationTest.displayInOrder();

        System.out.println();
        System.out.println("--- rotateRight() test (insert C, B, A) ---");
        AVLTree rightRotationTest = new AVLTree();
        rightRotationTest.insert("C", "CUST_C");
        rightRotationTest.insert("B", "CUST_B");
        rightRotationTest.insert("A", "CUST_A");
        System.out.println("Tree after balancing:");
        rightRotationTest.displayInOrder();
    }

    // -------------------------------------------------------------------------
    // Graph operations
    // -------------------------------------------------------------------------

    /**
     * Adds a new road connection to the city graph.
     * Method used: addEdge() on Graph.
     */
    private static void addRoadConnection() {
        printOperationHeader("ADD ROAD CONNECTION", "Graph - addEdge()");

        String source = readNonEmptyLine("Enter source neighborhood: ");
        String destination = readNonEmptyLine("Enter destination neighborhood: ");
        int weight = readPositiveInt("Enter distance in KM: ");

        System.out.println();
        System.out.println("Calling addEdge(\"" + source + "\", \"" + destination + "\", " + weight + ")...");
        roadNetwork.addEdge(source, destination, weight);

        System.out.println();
        System.out.println("[SUCCESS] Road connection added to the city map.");
    }

    /**
     * Displays all road connections in the city graph.
     * Method used: displayGraph() on Graph.
     */
    private static void displayCityMap() {
        printOperationHeader("CITY ROAD MAP", "Graph - displayGraph()");
        roadNetwork.displayGraph();
    }

    /**
     * Calculates the shortest path between two neighborhoods using Dijkstra.
     * Method used: calculateShortestPath() on Graph.
     */
    private static void calculateShortestPathInteractive() {
        printOperationHeader("SHORTEST PATH", "Graph - calculateShortestPath() / Dijkstra");

        String start = readNonEmptyLine("Enter start neighborhood: ");
        String end = readNonEmptyLine("Enter destination neighborhood: ");

        System.out.println();
        System.out.println("Calling calculateShortestPath(\"" + start + "\", \"" + end + "\")...");
        System.out.println("----------------------------------------");
        roadNetwork.calculateShortestPath(start, end);
    }

    /**
     * Calculates and displays the minimum spanning tree using Prim's algorithm.
     * Method used: calculateMST() on Graph.
     */
    private static void displayMinimumSpanningTree() {
        printOperationHeader("MINIMUM SPANNING TREE", "Graph - calculateMST() / Prim");
        roadNetwork.calculateMST();
    }

    // -------------------------------------------------------------------------
    // Customer / package helpers
    // -------------------------------------------------------------------------

    /**
     * Resolves or creates a customer ID for a destination neighborhood.
     *
     * @param destination destination neighborhood entered by the user
     * @return customer ID linked to the neighborhood
     */
    private static String resolveCustomerID(String destination) {
        System.out.print("Does customer already exist? (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        if (answer.equals("yes") || answer.equals("y")) {
            String customerID = readNonEmptyLine("Enter existing customer ID: ");
            addressDirectory.insert(destination, customerID);
            return customerID;
        }

        String generatedID = generateCustomerID();
        System.out.println("Generated Customer ID: " + generatedID);
        System.out.print("Do you accept this ID? (yes/no): ");
        String accept = scanner.nextLine().trim().toLowerCase();

        String customerID;

        if (accept.equals("no") || accept.equals("n")) {
            customerID = readNonEmptyLine("Enter your preferred customer ID: ");
        } else {
            customerID = generatedID;
        }

        System.out.println("Calling insert(\"" + destination + "\", \"" + customerID + "\") on AVL Tree...");
        addressDirectory.insert(destination, customerID);
        return customerID;
    }

    /**
     * Generates the next package ID in the project format.
     *
     * @return new package ID
     */
    private static String generatePackageID() {
        String packageID = "PKG" + String.format("%03d", nextPackageNumber);
        nextPackageNumber++;
        return packageID;
    }

    /**
     * Generates the next customer ID in the project format.
     *
     * @return new customer ID
     */
    private static String generateCustomerID() {
        String customerID = "CUST" + String.format("%03d", nextCustomerNumber);
        nextCustomerNumber++;
        return customerID;
    }

    // -------------------------------------------------------------------------
    // Input helpers
    // -------------------------------------------------------------------------

    /**
     * Reads a non-empty line of text from the user.
     *
     * @param prompt message shown before input
     * @return trimmed non-empty input
     */
    private static String readNonEmptyLine(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.equals("")) {
                return input;
            }

            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    /**
     * Reads a positive integer from the user.
     *
     * @param prompt message shown before input
     * @return positive integer value
     */
    private static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);

            try {
                int value = Integer.parseInt(scanner.nextLine().trim());

                if (value > 0) {
                    return value;
                }

                System.out.println("Please enter a number greater than 0.");
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    /**
     * Reads an integer within a given range, or 0 for exit.
     *
     * @param prompt    message shown before input
     * @param minChoice minimum valid choice (excluding exit)
     * @param maxChoice maximum valid choice
     * @return selected menu number, or 0 for exit
     */
    private static int readIntInRange(String prompt, int minChoice, int maxChoice) {
        while (true) {
            System.out.print(prompt);

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                if (choice == 0) {
                    System.out.println("Thank you for using FastRoute Kayseri. Goodbye!");
                    System.exit(0);
                }

                if (choice >= minChoice && choice <= maxChoice) {
                    return choice;
                }

                System.out.println("Please enter a number between 0 and " + maxChoice + ".");
            } catch (NumberFormatException exception) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Waits for the user to press Enter before showing the menu again.
     */
    private static void pauseForEnter() {
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
        System.out.println();
    }

    /**
     * Prints a formatted operation header with the related data structure.
     *
     * @param title          operation title
     * @param dataStructure  data structure and method information
     */
    private static void printOperationHeader(String title, String dataStructure) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("  " + title);
        System.out.println("  Data Structure: " + dataStructure);
        System.out.println("========================================");
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // File loading
    // -------------------------------------------------------------------------

    /**
     * Loads initial packages from file into the intake buffer and master log.
     * Also registers destinations in the AVL address directory.
     *
     * @param fileName name of the package data file
     * @return true if at least one package was loaded
     */
    private static boolean loadInitialPackagesFromFile(String fileName) {
        Package[] packages = readAllPackagesFromFile(fileName);

        if (packages.length == 0) {
            System.out.println("No package data found in " + fileName + ".");
            return false;
        }

        for (int i = 0; i < packages.length; i++) {
            Package pkg = packages[i];
            intakeBuffer.insertAtTail(new Package(pkg.packageID, pkg.destination));
            masterRegistry.addRecord(new Package(pkg.packageID, pkg.destination));

            String customerID = "CUST" + String.format("%03d", i + 1);
            addressDirectory.insert(pkg.destination, customerID);

            updatePackageCounterFromID(pkg.packageID);
        }

        nextCustomerNumber = packages.length + 1;
        return true;
    }

    /**
     * Updates the package counter based on an existing package ID.
     *
     * @param packageID package ID read from file
     */
    private static void updatePackageCounterFromID(String packageID) {
        try {
            String numberPart = packageID.replaceAll("[^0-9]", "");

            if (!numberPart.equals("")) {
                int parsed = Integer.parseInt(numberPart);

                if (parsed >= nextPackageNumber) {
                    nextPackageNumber = parsed + 1;
                }
            }
        } catch (NumberFormatException exception) {
            nextPackageNumber++;
        }
    }

    /**
     * Reads all packages from a text file.
     *
     * @param fileName name of the package data file
     * @return array of all Package objects in the file
     */
    private static Package[] readAllPackagesFromFile(String fileName) {
        int packageCount = countPackagesInFile(fileName);
        return readPackagesFromFile(fileName, packageCount);
    }

    /**
     * Counts valid package records in a text file.
     *
     * @param fileName name of the package data file
     * @return number of valid package lines in the file
     */
    private static int countPackagesInFile(String fileName) {
        int count = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                String trimmedLine = line.trim();

                if (!trimmedLine.equals("") && !trimmedLine.startsWith("#")) {
                    String[] parts = trimmedLine.split("\\s+");

                    if (parts.length == 2) {
                        count++;
                    }
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException exception) {
            System.out.println("ERROR: Unable to count packages in " + fileName + ".");
        }

        return count;
    }

    /**
     * Reads up to a maximum number of packages from a text file.
     *
     * @param fileName    name of the package data file
     * @param maxPackages maximum number of packages to read
     * @return array of Package objects read from the file
     */
    private static Package[] readPackagesFromFile(String fileName, int maxPackages) {
        if (maxPackages <= 0) {
            return new Package[0];
        }

        Package[] packages = new Package[maxPackages];
        int count = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null && count < maxPackages) {
                String trimmedLine = line.trim();

                if (!trimmedLine.equals("") && !trimmedLine.startsWith("#")) {
                    String[] parts = trimmedLine.split("\\s+");

                    if (parts.length == 2) {
                        packages[count] = new Package(parts[0], parts[1]);
                        count++;
                    }
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (FileNotFoundException exception) {
            System.out.println("ERROR: " + fileName + " not found.");
        } catch (IOException exception) {
            System.out.println("ERROR: Unable to read " + fileName + ".");
        }

        Package[] result = new Package[count];

        for (int i = 0; i < count; i++) {
            result[i] = packages[i];
        }

        return result;
    }

    /**
     * Reads road connections from a text file and inserts them into a graph.
     *
     * @param fileName name of the map data file
     * @param graph    graph object that will receive the road connections
     * @return true if at least one valid road was loaded
     */
    private static boolean loadGraphFromFile(String fileName, Graph graph) {
        int loadedRoadCount = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                String trimmedLine = line.trim();

                if (!trimmedLine.equals("") && !trimmedLine.startsWith("#")) {
                    String[] parts = trimmedLine.split("\\s+");

                    if (parts.length == 3) {
                        String source = parts[0];
                        String destination = parts[1];

                        try {
                            int weight = Integer.parseInt(parts[2]);
                            graph.addEdge(source, destination, weight);
                            loadedRoadCount++;
                        } catch (NumberFormatException exception) {
                            System.out.println("Skipped invalid road weight: " + trimmedLine);
                        }
                    } else {
                        System.out.println("Skipped invalid map line: " + trimmedLine);
                    }
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (FileNotFoundException exception) {
            System.out.println("ERROR: " + fileName + " not found.");
            System.out.println("Please place " + fileName + " in the project folder.");
            return false;
        } catch (IOException exception) {
            System.out.println("ERROR: Unable to read file.");
            System.out.println("Details: " + exception.getMessage());
            return false;
        }

        if (loadedRoadCount == 0) {
            System.out.println("No valid graph data was found in " + fileName + ".");
            return false;
        }

        return true;
    }
}
