import java.util.ArrayList;
import java.util.Scanner;

class UserRequest {

    private static ArrayList<UserRequest> userRequestsList = new ArrayList<>();

    private String name;
    private String phone;
    private String sourceLocation;
    private String destinationLocation;
    private boolean accepted;

    public UserRequest(String name, String phone, String sourceLocation, String destinationLocation) {
        this.name = name;
        this.phone = phone;
        this.sourceLocation = sourceLocation;
        this.destinationLocation = destinationLocation;
        this.accepted = false;

        userRequestsList.add(this);
    }

    public static ArrayList<UserRequest> getUserRequestsList() {
        return userRequestsList;
    }

    public void markAsAccepted() {
        this.accepted = true;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSource() {
        return sourceLocation;
    }

    public String getDestination() {
        return destinationLocation;
    }

    public Boolean getAccepted() {
        return accepted;
    }
}
