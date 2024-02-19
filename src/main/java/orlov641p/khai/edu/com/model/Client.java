package orlov641p.khai.edu.com.model;

import java.io.Serializable;
import java.util.UUID;

public class Client implements Serializable {
    private String clientId;
    private String lastName;
    private String firstName;
    private String secondName;

    public Client(String lastName, String firstName, String secondName) {
        this.clientId = UUID.randomUUID().toString();
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Client(String clientId, String lastName, String firstName, String secondName) {
        this.clientId = clientId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                '}';
    }
}