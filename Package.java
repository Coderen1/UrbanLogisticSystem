/**
 * The Package class represents one delivery package in the
 * FastRoute Kayseri Urban Logistics & Distribution System.
 *
 * Each Package object stores the basic information that will be kept
 * inside the Master Registry linked list.
 */
public class Package {

    /** Unique identification code of the package. */
    String packageID;

    /** Destination address or district of the package. */
    String destination;

    /**
     * Creates a new Package object with an ID and destination.
     *
     * @param packageID   unique package identification code
     * @param destination delivery destination of the package
     */
    public Package(String packageID, String destination) {
        this.packageID = packageID;
        this.destination = destination;
    }

    /**
     * Prints the package information in a clean and readable format.
     */
    public void displayPackage() {
        System.out.println("Package ID  : " + packageID);
        System.out.println("Destination : " + destination);
    }
}
