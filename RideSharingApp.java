import java.util.*;

public class RideSharingApp {

    private static GraphRepresentation graphRepresentation;
    private static CostCalculation costCalculator;
    private static DriverAllocation driverAllocation;
    private static Customer customer;
    private static Rider rider;
    private static String currentUser;
    private static boolean isRiderRequestAccepted;

    public static void main(String[] args) {

        initializeComponents();
        displayMenu();
    }

    private static void initializeComponents() {
        graphRepresentation = new GraphRepresentation();
        costCalculator = new CostCalculation();
        driverAllocation = new DriverAllocation(graphRepresentation);
        customer = new Customer();
        rider = new Rider();
        isRiderRequestAccepted = false;
    }

    private static void displayMenu() {
        int choice;
        do {
            System.out.println("Ride-Sharing App Menu:");
            System.out.println();
            System.out.println("1. User");
            System.out.println("2. Rider");
            System.out.println("3. Admin");
            System.out.println("0. Exit");
            System.out.println();

            choice = getUserChoice();

            switch (choice) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    riderMenu();
                    break;
                case 3:
                    handleAdminLogin();
                    break;
                case 0:
                    System.out.println("Exiting Ride-Sharing App. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 0);
    }

    private static void handleAdminLogin() {
        if (adminLogin()) {
            System.out.println();
            System.out.println("\u001B[32mAdmin login successful!\u001B[0m");
            System.out.println();
            graphRepresentation.handleGraphRepresentation();
        } else {
            System.out.println("\nIncorrect username or password. Please try again.\n");
        }
    }

    private static boolean adminLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        return username.equals("rifat") && password.equals("r107754n");
    }

    private static void customerMenu() {
        int choice;
        do {
            System.out.println("User Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("0. Back to Main Menu");
            System.out.println();

            choice = getUserChoice();

            switch (choice) {
                case 1:
                    customer.register();
                    break;
                case 2:
                    handleCustomerLogin();
                    break;
                case 0:
                    System.out.println("Returning to the main menu.");
                    System.out.println("\n");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println();
            }
        } while (choice != 0);
    }

    private static void handleCustomerLogin() {
        if (customer.login()) {
            System.out.println();
            System.out.println("\u001B[32mUser login successful!\u001B[0m");
            System.out.println();
            System.out.println();
            handleUserOptions();
        } else {
            System.out.println("\nIncorrect username or password. Please try again.\n");
            System.out.println();
        }
    }

    private static void handleUserOptions() {
        int userChoice;
        do {
            System.out.println("User Options:");
            System.out.println("1. Request Rider");
            System.out.println("2. Request Status");
            System.out.println("0. Logout");
            System.out.println();

            userChoice = getUserChoice();

            switch (userChoice) {
                case 1:
                    handleRiderRequest();
                    break;
                case 2:
                    handleUserRequestStatus();
                    break;
                case 0:
                    displayMenu();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println();
            }
        } while (userChoice != 0);
    }

    private static void handleRiderRequest() {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Your name: ");
        String name = scanner.next();
        System.out.print("Your phone: ");
        String phone = scanner.next();

        System.out.print("Enter source location name: ");
        String sourceName = scanner.next();
        System.out.print("Enter destination location name: ");
        String destinationName = scanner.next();

        GraphRepresentation.LocationNode sourceNode = graphRepresentation.getLocationNode(sourceName);
        GraphRepresentation.LocationNode destinationNode = graphRepresentation.getLocationNode(destinationName);

        new UserRequest(name, phone, sourceName, destinationName);

        if (sourceNode != null && destinationNode != null) {

            System.out.println();
            System.out.println("\u001B[32mRequest submitted successfully!\u001B[0m");
            System.out.println();
            isRiderRequestAccepted = false;
            // displayShortestPath(sourceNode, destinationNode);
            System.out.println("Waiting for a rider to accept your request...");
            System.out.println();
            displayMenu();

            System.out.println("Your request has been accepted!");
            System.out.println("Rider is on the way.");
            System.out.println();
        } else {
            System.out.println("Invalid location names. Please check and try again.");
            System.out.println();
        }
        // display the main menu
        // displayMenu();
    }

    // show user request status
    private static void handleUserRequestStatus() {
        // Access the global list from another class
        ArrayList<UserRequest> allUserRequests = UserRequest.getUserRequestsList();
        int length = allUserRequests.size();
        if (length == 0) {
            System.out.println("Your have no requests to be accepted");
            System.out.println();
        } else {

            for (UserRequest request : allUserRequests) {

                if (request.getAccepted()) {
                    System.out.println("Your ride sharing request has been accepted!");
                    if (request.getSource() != null && request.getDestination() != null) {
                        GraphRepresentation.LocationNode sourceNode = graphRepresentation
                                .getLocationNode(request.getSource());
                        GraphRepresentation.LocationNode destinationNode = graphRepresentation
                                .getLocationNode(request.getDestination());

                        isRiderRequestAccepted = false;

                        displayShortestPath(sourceNode, destinationNode);
                        displayMenu();
                        System.out.println("Rider is on the way.");
                        System.out.println();
                    } else {
                        System.out.println("Invalid location names. Please check and try again.");
                        System.out.println();
                    }
                } else {
                    System.out.println("Waiting for accepting request...");
                    System.out.println();
                }
            }
        }

    }

    private static void displayShortestPath(GraphRepresentation.LocationNode sourceNode,
            GraphRepresentation.LocationNode destinationNode) {
        List<GraphRepresentation.LocationNode> shortestPath = graphRepresentation.findShortestPath(sourceNode,
                destinationNode);

        if (shortestPath != null) {
            System.out.println("\nShortest Path from " + sourceNode.name + " to " + destinationNode.name + ":");
            for (GraphRepresentation.LocationNode node : shortestPath) {
                System.out.print(node.name + "->");
            }
            System.out.println("\n");
            double totalDistance = graphRepresentation.calculateTotalDistance(shortestPath);

            CostCalculation.calculateCost(totalDistance);
        } else {
            System.out.println("No path found between " + sourceNode.name + " and " + destinationNode.name + ".");
        }
    }

    private static void riderMenu() {
        int choice;
        do {
            System.out.println("Rider Menu:");
            System.out.println("1. Register");
            System.out.println("2. Login");

            System.out.println("0. Back to Main Menu");
            System.out.println();

            choice = getUserChoice();

            switch (choice) {
                case 1:
                    rider.register();
                    break;
                case 2:
                    handleRiderLogin();
                    break;

                case 0:
                    System.out.println("Returning to the main menu.");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println();
            }
        } while (choice != 0);
    }

    private static void handleRiderLogin() {
        if (rider.login()) {
            System.out.println();
            System.out.println("\u001B[32mRider login successful!\u001B[0m");
            System.out.println();
            viewUserRequests();
        } else {
            System.out.println("\nIncorrect username or password. Please try again.\n");
            System.out.println();
        }
    }

    private static void viewUserRequests() {
        System.out.println("User Requests:");
        System.out.println("1. See User Request");
        System.out.println("2. Back to Rider Menu");
        System.out.println("0. Logout");
        System.out.println();

        int choice = getUserChoice();

        switch (choice) {
            case 1:
                acceptUserRequest();
                break;
            case 2:
                System.out.println("Returning to Rider Menu.");
                System.out.println();
                break;
            case 0:
                displayMenu();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                System.out.println();
        }
    }

    private static void acceptUserRequest() {
        ArrayList<UserRequest> allUserRequests = UserRequest.getUserRequestsList();

        for (UserRequest request : allUserRequests) {
            System.out.println("Name: " + request.getName() + ", Phone: " +
                    request.getPhone());
        }

        System.out.println("1. Accept request.");
        System.out.println("2. Logout");

        Scanner scan = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        int choice = scan.nextInt();
        if (choice == 1) {
            for (UserRequest request : allUserRequests) {
                request.markAsAccepted();
            }
            System.out.println("User request accepted!");
            System.out.println();
            isRiderRequestAccepted = true;
        } else if (choice == 2) {
            displayMenu();
        } else {
            System.out.println("Please choose correct option");
            System.out.println();
        }
        // scan.close();
    }

    private static void handleDriverAllocation() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter source location name for driver allocation: ");
        String sourceName = scanner.next();
        DriverAllocation.DriverInfo allocatedDriver = driverAllocation.allocateDriver(sourceName);
        if (allocatedDriver != null) {
            System.out.println("Rider allocated successfully. Rider's current location: " +
                    allocatedDriver.locationNode.name + ", Rider's name: " + allocatedDriver.driverName);
        } else {
            System.out.println("No available drivers. Please try again later.");
        }
    }

    private static int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }
}

class Customer {
    private Map<String, String> customerDatabase;

    Customer() {
        this.customerDatabase = new HashMap<>();
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        customerDatabase.put(username, password);
        System.out.println();
        System.out.println("\u001B[32mUser registered successfully.\u001B[0m");
        System.out.println();
    }

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        return customerDatabase.containsKey(username) && customerDatabase.get(username).equals(password);
    }
}

class Rider {
    private Map<String, String> riderDatabase;

    Rider() {
        this.riderDatabase = new HashMap<>();
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        riderDatabase.put(username, password);
        System.out.println();
        System.out.println("\u001B[32mRider registered successfully.\u001B[0m");
        System.out.println();
    }

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        return riderDatabase.containsKey(username) && riderDatabase.get(username).equals(password);
    }
}

class GraphRepresentation {

    private Map<String, LocationNode> locationGraph;
    private Map<String, LocationNode> minimumSpanningTree;

    GraphRepresentation() {
        this.locationGraph = new HashMap<>();
        this.minimumSpanningTree = new HashMap<>();
    }

    static class LocationNode {
        String name;
        Map<LocationNode, Double> neighbors;

        LocationNode(String name) {
            this.name = name;
            this.neighbors = new HashMap<>();
        }

        void addNeighbor(LocationNode neighbor, double distance) {
            neighbors.put(neighbor, distance);
            neighbor.neighbors.put(this, distance);
        }
    }

    public void handleGraphRepresentation() {
        int choice;
        do {
            System.out.println("Admin Panel:");
            System.out.println("1. Add Location");
            System.out.println("2. Add Route");
            System.out.println("3. Display Path");
            System.out.println("0. Logout");
            System.out.println();

            choice = getUserChoice();

            switch (choice) {
                case 1:
                    addLocationNode();
                    break;
                case 2:
                    addRoute();
                    break;
                case 3:
                    displayGraph();
                    break;
                case 0:
                    System.out.println("Returning to the main menu.");
                    System.out.println();
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println();
            }
        } while (choice != 0);
    }

    private void addLocationNode() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter location name: ");
        String locationName = scanner.next();
        locationGraph.put(locationName, new LocationNode(locationName));
        System.out.println("\n");
        System.out.println("\u001B[32mLocation added successfully.\u001B[0m");
        System.out.println();
    }

    private void addRoute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter source location name: ");
        String sourceName = scanner.next();
        System.out.print("Enter destination location name: ");
        String destinationName = scanner.next();
        System.out.print("Enter distance between locations: ");
        double distance = scanner.nextDouble();

        LocationNode sourceNode = locationGraph.get(sourceName);
        LocationNode destinationNode = locationGraph.get(destinationName);

        if (sourceNode != null && destinationNode != null) {
            sourceNode.addNeighbor(destinationNode, distance);
            System.out.println();
            System.out.println("\u001B[32mRoute added successfully.\u001B[0m");
            System.out.println();
        } else {
            System.out.println();
            System.out.println("\u001B[31mInvalid location names. Please check and try again.\u001B[0m");
            System.out.println();
        }
    }

    private void displayGraph() {
        System.out.println("\n");
        System.out.println("Source and destination locations: ");
        for (LocationNode node : locationGraph.values()) {
            System.out.print(node.name + ": ");
            for (Map.Entry<LocationNode, Double> entry : node.neighbors.entrySet()) {
                System.out.print(entry.getKey().name + "(" + entry.getValue() + "km.) ");
            }
            System.out.println("\n");
        }
    }

    public void displayMinimumSpanningTree() {
        System.out.println("Minimum Spanning Tree:");
        minimumSpanningTree = findMinimumSpanningTree();
        for (LocationNode node : minimumSpanningTree.values()) {
            System.out.print(node.name + ": ");
            for (Map.Entry<LocationNode, Double> entry : node.neighbors.entrySet()) {
                System.out.print(entry.getKey().name + "(" + entry.getValue() + "Km.) ");
            }
            System.out.println();
        }
    }

    public LocationNode getLocationNode(String locationName) {
        return locationGraph.get(locationName);
    }

    private Map<String, LocationNode> findMinimumSpanningTree() {
        Map<String, LocationNode> result = new HashMap<>();
        Set<LocationNode> visited = new HashSet<>();
        LocationNode startNode = locationGraph.values().iterator().next();
        minimumSpanningTreeDFS(startNode, visited, result);
        return result;
    }

    private void minimumSpanningTreeDFS(LocationNode node, Set<LocationNode> visited,
            Map<String, LocationNode> result) {
        visited.add(node);
        result.put(node.name, node);
        for (LocationNode neighbor : node.neighbors.keySet()) {
            if (!visited.contains(neighbor)) {
                minimumSpanningTreeDFS(neighbor, visited, result);
            }
        }
    }

    public List<LocationNode> findShortestPath(LocationNode source, LocationNode destination) {
        Map<LocationNode, LocationNode> predecessorMap = new HashMap<>();
        Queue<LocationNode> queue = new LinkedList<>();
        Set<LocationNode> visited = new HashSet<>();

        queue.add(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            LocationNode currentNode = queue.poll();

            for (LocationNode neighbor : currentNode.neighbors.keySet()) {
                if (!visited.contains(neighbor)) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    predecessorMap.put(neighbor, currentNode);

                    if (neighbor.equals(destination)) {
                        return reconstructPath(source, destination, predecessorMap);
                    }
                }
            }
        }

        return null;
    }

    private List<LocationNode> reconstructPath(LocationNode source, LocationNode destination,
            Map<LocationNode, LocationNode> predecessorMap) {
        List<LocationNode> path = new ArrayList<>();
        LocationNode current = destination;

        while (current != null) {
            path.add(current);
            current = predecessorMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }

    public double calculateTotalDistance(List<LocationNode> path) {
        double totalDistance = 0.0;

        for (int i = 0; i < path.size() - 1; i++) {
            LocationNode current = path.get(i);
            LocationNode next = path.get(i + 1);
            totalDistance += current.neighbors.get(next);
        }

        return totalDistance;
    }

    private int getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your choice: ");
        return scanner.nextInt();
    }
}

class CostCalculation {

    private static final double COST_PER_METER = 5.50;

    public static void calculateCost(double distance) {
        double dynamicCost = distance * COST_PER_METER;

        System.out.println("Cost: " + dynamicCost + " TK");
        System.out.println();
    }
}

class DriverAllocation {

    private GraphRepresentation graphRepresentation;

    DriverAllocation(GraphRepresentation graphRepresentation) {
        this.graphRepresentation = graphRepresentation;
    }

    public static class DriverInfo {
        public GraphRepresentation.LocationNode locationNode;
        public String driverName;

        public DriverInfo(GraphRepresentation.LocationNode locationNode, String driverName) {
            this.locationNode = locationNode;
            this.driverName = driverName;
        }
    }

    public DriverInfo allocateDriver(String sourceLocation) {
        GraphRepresentation.LocationNode sourceNode = graphRepresentation.getLocationNode(sourceLocation);
        if (sourceNode != null) {
            return new DriverInfo(sourceNode, "Ashfak");
        } else {
            return null;
        }
    }
}