package orlov641p.khai.edu.com.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Flight implements Serializable {
    private String flightId;
    private String origin;
    private String destination;
    private LocalDateTime flightDate;
    private Boolean[] seats;

    public Flight(String origin, String destination, LocalDateTime flightDate, Integer numberOfSeats) {
        this.flightId = UUID.randomUUID().toString();
        this.origin = origin;
        this.destination = destination;
        this.flightDate = flightDate;
        seats = new Boolean[numberOfSeats];
        for(int i = 0; i < numberOfSeats; i++){
            seats[i] = false;
        }
    }

    public Flight(String flightId, String origin, String destination, LocalDateTime flightDate, Integer numberOfSeats) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.flightDate = flightDate;
        seats = new Boolean[numberOfSeats];
        for(int i = 0; i < numberOfSeats; i++){
            seats[i] = false;
        }
    }

    public Flight(String flightId, String origin, String destination, LocalDateTime flightDate, Boolean[] seats) {
        this.flightId = flightId;
        this.origin = origin;
        this.destination = destination;
        this.flightDate = flightDate;
        this.seats = seats;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDateTime flightDate) {
        this.flightDate = flightDate;
    }

    public Boolean[] getSeats() {
        return seats;
    }

    public void setSeats(Boolean[] seats) {
        this.seats = seats;
    }

    public String getFormattedSeats(){
        StringBuilder sb = new StringBuilder("{");

        for(int i = 0; i < seats.length; i++){
            sb.append(i);
            if(!seats[i]){
                sb.append("=free");
            } else {
                sb.append("=taken");
            }

            if(i < seats.length-1){
                sb.append(", ");
            }
            if(i % 10 == 0 && i != 0){
                sb.append("\n");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightId='" + flightId + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", flightDate=" + flightDate +
                ", seats=" + getFormattedSeats() +
                '}';
    }
}