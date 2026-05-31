import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main class for the FastRoute Kayseri COMP206 semester project.
 *
 * This version uses an interactive menu so each group member can demonstrate
 * one data structure and its methods step by step during the presentation.
 */
public class Main {

    private static Graph roadNetwork;
    private static String[] pathEndpoints;
    private static SinglyLinkedList masterRegistry;
    private static DoublyLinkedList intakeBuffer;
    private static PackageQueue deliveryQueue;
    private static PackageStack truckStack;
    private static AVLTree addressDirectory;

    private static boolean graphLoaded;
    private static boolean packagesLoaded;

    private static Scanner scanner;

    /**
     * Program entry point.
     *
     * @param args command-line arguments, not used in this project
     */
    public static void main(String[] args) {
        printBranding();
        scanner = new Scanner(System.in);
        initializeSystem();
        runMenu();
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
        pathEndpoints = loadGraphFromFile("mapData.txt", roadNetwork);
        graphLoaded = pathEndpoints != null;

        masterRegistry = new SinglyLinkedList();
        intakeBuffer = new DoublyLinkedList();
        deliveryQueue = new PackageQueue();
        truckStack = new PackageStack();
        addressDirectory = new AVLTree();

        packagesLoaded = loadPackagesFromFile(
                "packageData.txt",
                masterRegistry,
                intakeBuffer,
                deliveryQueue,
                truckStack);

        if (packagesLoaded) {
            populateAddressDirectory();
        }

        if (graphLoaded) {
            System.out.println("mapData.txt loaded successfully.");
        }

        if (packagesLoaded) {
            System.out.println("packageData.txt loaded successfully.");
        }

        System.out.println();
        System.out.println("System ready. Use the menu to demonstrate each part.");
        System.out.println();
    }

    /**
     * Displays the presentation menu and processes user selections.
     */
    private static void runMenu() {
        while (true) {
            printMenu();
            int choice = readMenuChoice();

            switch (choice) {
                case 1:
                    demoMasterRegistry();
                    break;
                case 2:
                    demoIntakeBuffer();
                    break;
                case 3:
                    demoDeliveryQueue();
                    break;
                case 4:
                    demoTruckStack();
                    break;
                case 5:
                    demoAddressDirectory();
                    break;
                case 6:
                    demoCityRouting();
                    break;
                case 7:
                    demoFullSystem();
                    break;
                case 0:
                    System.out.println("Thank you for using FastRoute Kayseri. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option. Please enter a number between 0 and 7.");
            }

            System.out.println();
            System.out.println("Press Enter to return to the menu...");
            scanner.nextLine();
        }
    }

    /**
     * Prints the interactive demo menu.
     */
    private static void printMenu() {
        System.out.println("========================================");
        System.out.println("        FastRoute Kayseri");
        System.out.println(" Urban Logistics & Distribution System");
        System.out.println("========================================");
        System.out.println("1. Master Registry (Singly Linked List)");
        System.out.println("2. Intake Buffer (Doubly Linked List)");
        System.out.println("3. Standard Delivery (Queue)");
        System.out.println("4. Truck Loading (Stack)");
        System.out.println("5. Address Directory (AVL Tree)");
        System.out.println("6. City Routing (Graph)");
        System.out.println("7. Run Full Demo (All Systems)");
        System.out.println("0. Exit");
        System.out.println("----------------------------------------");
        System.out.print("Select option: ");
    }

    /**
     * Reads and validates a menu choice from the user.
     *
     * @return selected menu option
     */
    private static int readMenuChoice() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Please enter a valid number: ");
        }

        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

    /**
     * Demonstrates Singly Linked List methods step by step.
     */
    private static void demoMasterRegistry() {
        printSectionHeader("MASTER REGISTRY - SINGLY LINKED LIST");

        SinglyLinkedList registry = new SinglyLinkedList();
        Package[] packages = readPackagesFromFile("packageData.txt", 3);

        if (packages.length == 0) {
            System.out.println("No package data available for this demo.");
            return;
        }

        System.out.println("[Step 1] Method: addRecord(Package pkg)");
        System.out.println("Purpose : Append each package to the end of the daily log.");
        System.out.println("Complexity: O(n) because the list is traversed to find the tail.");
        System.out.println();

        for (Package pkg : packages) {
            System.out.println("Calling addRecord(" + pkg.packageID + ", " + pkg.destination + ")");
            registry.addRecord(pkg);
        }

        System.out.println();
        System.out.println("[Step 2] Method: displayLog()");
        System.out.println("Purpose : Traverse the list from head to tail and print all records.");
        System.out.println("Complexity: O(n) because every node is visited once.");
        System.out.println();
        registry.displayLog();
    }

    /**
     * Demonstrates Doubly Linked List methods step by step.
     */
    private static void demoIntakeBuffer() {
        printSectionHeader("INTAKE BUFFER - DOUBLY LINKED LIST");

        DoublyLinkedList buffer = new DoublyLinkedList();
        Package[] packages = readPackagesFromFile("packageData.txt", 3);

        if (packages.length == 0) {
            System.out.println("No package data available for this demo.");
            return;
        }

        System.out.println("[Step 1] Method: insertAtTail(Package pkg)");
        System.out.println("Purpose : Add newly arrived packages to the end of the buffer.");
        System.out.println("Complexity: O(1) because a tail pointer is used.");
        System.out.println();

        for (Package pkg : packages) {
            System.out.println("Calling insertAtTail(" + pkg.packageID + ", " + pkg.destination + ")");
            buffer.insertAtTail(pkg);
        }

        System.out.println();
        System.out.println("Current buffer state:");
        buffer.displayBuffer();

        System.out.println("[Step 2] Method: removeFromHead()");
        System.out.println("Purpose : Move the oldest package out of the buffer for processing.");
        System.out.println("Complexity: O(1) because only the head pointer is updated.");
        System.out.println();

        Package removed = buffer.removeFromHead();

        if (removed != null) {
            System.out.println("Removed package from head:");
            removed.displayPackage();
        } else {
            System.out.println("Buffer was empty.");
        }

        System.out.println();
        System.out.println("Buffer after removeFromHead():");
        buffer.displayBuffer();
    }

    /**
     * Demonstrates Queue methods step by step.
     */
    private static void demoDeliveryQueue() {
        printSectionHeader("STANDARD DELIVERY - QUEUE (FIFO)");

        PackageQueue queue = new PackageQueue();
        Package[] packages = readPackagesFromFile("packageData.txt", 3);

        if (packages.length == 0) {
            System.out.println("No package data available for this demo.");
            return;
        }

        System.out.println("[Step 1] Method: enqueue(Package pkg)");
        System.out.println("Purpose : Add packages to the rear of the delivery line.");
        System.out.println("Complexity: O(1) because a rear pointer is used.");
        System.out.println();

        for (Package pkg : packages) {
            System.out.println("Calling enqueue(" + pkg.packageID + ", " + pkg.destination + ")");
            queue.enqueue(pkg);
        }

        System.out.println();
        System.out.println("Current queue order (front to rear):");
        queue.displayQueue();

        System.out.println("[Step 2] Method: dequeue()");
        System.out.println("Purpose : Remove the front package first (First In, First Out).");
        System.out.println("Complexity: O(1) because only the front pointer is updated.");
        System.out.println();

        Package removed = queue.dequeue();

        if (removed != null) {
            System.out.println("Dequeued package:");
            removed.displayPackage();
        } else {
            System.out.println("Queue was empty.");
        }

        System.out.println();
        System.out.println("Queue after dequeue():");
        queue.displayQueue();
    }

    /**
     * Demonstrates Stack methods step by step.
     */
    private static void demoTruckStack() {
        printSectionHeader("TRUCK LOADING - STACK (LIFO)");

        PackageStack stack = new PackageStack();
        Package[] packages = readPackagesFromFile("packageData.txt", 3);

        if (packages.length == 0) {
            System.out.println("No package data available for this demo.");
            return;
        }

        System.out.println("[Step 1] Method: push(Package pkg)");
        System.out.println("Purpose : Load packages onto the truck cargo area.");
        System.out.println("Complexity: O(1) because insertion happens directly at the top.");
        System.out.println();

        for (Package pkg : packages) {
            System.out.println("Calling push(" + pkg.packageID + ", " + pkg.destination + ")");
            stack.push(pkg);
        }

        System.out.println();
        System.out.println("Current stack order (top to bottom):");
        stack.displayStack();

        System.out.println("[Step 2] Method: pop()");
        System.out.println("Purpose : Unload the most recently loaded package first (Last In, First Out).");
        System.out.println("Complexity: O(1) because removal happens directly from the top.");
        System.out.println();

        Package removed = stack.pop();

        if (removed != null) {
            System.out.println("Popped package:");
            removed.displayPackage();
        } else {
            System.out.println("Stack was empty.");
        }

        System.out.println();
        System.out.println("Stack after pop():");
        stack.displayStack();
    }

    /**
     * Demonstrates AVL Tree methods step by step.
     */
    private static void demoAddressDirectory() {
        printSectionHeader("ADDRESS DIRECTORY - AVL TREE");

        AVLTree directory = new AVLTree();

        System.out.println("[Step 1] Method: insert(String neighborhood, String customerID)");
        System.out.println("Purpose : Store neighborhood records in a balanced search tree.");
        System.out.println("Complexity: O(log n) because rotations keep the tree balanced.");
        System.out.println();

        String[][] sampleRecords = {
                { "Talas", "CUST001" },
                { "Belsin", "CUST002" },
                { "Melikgazi", "CUST003" },
                { "Erkilet", "CUST004" },
                { "Incesu", "CUST005" },
                { "Hacilar", "CUST006" }
        };

        for (String[] record : sampleRecords) {
            System.out.println("Calling insert(" + record[0] + ", " + record[1] + ")");
            directory.insert(record[0], record[1]);
        }

        System.out.println();
        System.out.println("[Step 2] Method: search(String neighborhood)");
        System.out.println("Purpose : Find a neighborhood record efficiently.");
        System.out.println("Complexity: O(log n) because the tree height stays balanced.");
        System.out.println();

        System.out.println("Searching for Talas:");
        directory.search("Talas");
        System.out.println();

        System.out.println("Searching for UnknownDistrict:");
        directory.search("UnknownDistrict");
        System.out.println();

        System.out.println("[Step 3] Method: displayInOrder()");
        System.out.println("Purpose : Print all records in sorted neighborhood order.");
        System.out.println("Complexity: O(n) because every node is visited once.");
        System.out.println();
        directory.displayInOrder();
    }

    /**
     * Demonstrates Graph methods step by step.
     */
    private static void demoCityRouting() {
        printSectionHeader("CITY ROUTING - WEIGHTED GRAPH");

        if (!graphLoaded) {
            System.out.println("Graph data is not available. Please check mapData.txt.");
            return;
        }

        Graph demoGraph = new Graph(100);
        String[] endpoints = loadGraphFromFile("mapData.txt", demoGraph);

        System.out.println("[Step 1] Method: addEdge(String source, String destination, int weight)");
        System.out.println("Purpose : Build the city road network.");
        System.out.println("Complexity: O(V) because vertex names may be searched before adding.");
        System.out.println();
        System.out.println("Roads loaded from mapData.txt using addEdge():");
        demoGraph.displayGraph();

        if (endpoints == null) {
            System.out.println("No valid graph endpoints found.");
            return;
        }

        System.out.println();
        System.out.println("[Step 2] Method: calculateShortestPath(String start, String end)");
        System.out.println("Purpose : Find the fastest delivery route using Dijkstra's Algorithm.");
        System.out.println("Complexity: O(V^2) with the current adjacency matrix implementation.");
        System.out.println();
        demoGraph.calculateShortestPath(endpoints[0], endpoints[1]);

        System.out.println();
        System.out.println("[Step 3] Method: calculateMST()");
        System.out.println("Purpose : Find the most efficient city network using Prim's Algorithm.");
        System.out.println("Complexity: O(V^2) with the current adjacency matrix implementation.");
        System.out.println();
        demoGraph.calculateMST();
    }

    /**
     * Runs the complete system demo using all loaded data structures.
     */
    private static void demoFullSystem() {
        printSectionHeader("FULL SYSTEM DEMO");

        if (graphLoaded) {
            System.out.println("CITY ROAD NETWORK");
            System.out.println("----------------------------------------");
            roadNetwork.displayGraph();
            System.out.println();

            System.out.println("SHORTEST PATH");
            System.out.println("----------------------------------------");
            roadNetwork.calculateShortestPath(pathEndpoints[0], pathEndpoints[1]);
            System.out.println();

            System.out.println("MINIMUM SPANNING TREE");
            System.out.println("----------------------------------------");
            roadNetwork.calculateMST();
            System.out.println();
        }

        if (packagesLoaded) {
            System.out.println("MASTER REGISTRY");
            System.out.println("----------------------------------------");
            masterRegistry.displayLog();
            System.out.println();

            System.out.println("INTAKE BUFFER");
            System.out.println("----------------------------------------");
            intakeBuffer.displayBuffer();
            System.out.println();

            System.out.println("DELIVERY QUEUE");
            System.out.println("----------------------------------------");
            deliveryQueue.displayQueue();
            System.out.println();

            System.out.println("TRUCK LOADING STACK");
            System.out.println("----------------------------------------");
            truckStack.displayStack();
            System.out.println();
        }

        System.out.println("ADDRESS DIRECTORY");
        System.out.println("----------------------------------------");
        addressDirectory.displayInOrder();
    }

    /**
     * Inserts neighborhood records into the AVL Tree using package destinations.
     */
    private static void populateAddressDirectory() {
        Package[] packages = readAllPackagesFromFile("packageData.txt");

        for (int i = 0; i < packages.length; i++) {
            String customerID = "CUST" + String.format("%03d", i + 1);
            addressDirectory.insert(packages[i].destination, customerID);
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
     * Prints a formatted section header for demo output.
     *
     * @param title section title
     */
    private static void printSectionHeader(String title) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("  " + title);
        System.out.println("========================================");
        System.out.println();
    }

    /**
     * Reads up to a maximum number of packages from a text file.
     *
     * @param fileName   name of the package data file
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
     * @return two neighborhood names for shortest path testing, or null if no
     *         valid road data could be loaded
     */
    private static String[] loadGraphFromFile(String fileName, Graph graph) {
        String firstSource = null;
        String lastDestination = null;
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

                            if (firstSource == null) {
                                firstSource = source;
                            }

                            lastDestination = destination;
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
            return null;
        } catch (IOException exception) {
            System.out.println("ERROR: Unable to read file.");
            System.out.println("Details: " + exception.getMessage());
            return null;
        }

        if (loadedRoadCount == 0) {
            System.out.println("No valid graph data was found in " + fileName + ".");
            return null;
        }

        return new String[] { firstSource, lastDestination };
    }

    /**
     * Reads package records from a text file and inserts every package into
     * all package-based data structures used in this project.
     *
     * @param fileName       name of the package data file
     * @param masterRegistry singly linked list for the package registry
     * @param intakeBuffer   doubly linked list for the intake buffer
     * @param deliveryQueue  queue for standard delivery order
     * @param truckStack     stack for truck loading order
     * @return true if at least one package was loaded, otherwise false
     */
    private static boolean loadPackagesFromFile(
            String fileName,
            SinglyLinkedList masterRegistry,
            DoublyLinkedList intakeBuffer,
            PackageQueue deliveryQueue,
            PackageStack truckStack) {

        int loadedPackageCount = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();

            while (line != null) {
                String trimmedLine = line.trim();

                if (!trimmedLine.equals("") && !trimmedLine.startsWith("#")) {
                    String[] parts = trimmedLine.split("\\s+");

                    if (parts.length == 2) {
                        String packageId = parts[0];
                        String destination = parts[1];

                        masterRegistry.addRecord(new Package(packageId, destination));
                        intakeBuffer.insertAtTail(new Package(packageId, destination));
                        deliveryQueue.enqueue(new Package(packageId, destination));
                        truckStack.push(new Package(packageId, destination));

                        loadedPackageCount++;
                    } else {
                        System.out.println("Skipped invalid package line: " + trimmedLine);
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

        if (loadedPackageCount == 0) {
            System.out.println("No valid package data was found in " + fileName + ".");
            return false;
        }

        return true;
    }
}
