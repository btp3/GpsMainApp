package models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserJourneyList implements Serializable
{

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("transport")
    @Expose
    private List<Transport> transport = null;
    @SerializedName("journey")
    @Expose
    private List<Journey> journey = null;
    private final static long serialVersionUID = -5591394370914825604L;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Transport> getTransport() {
        return transport;
    }

    public void setTransport(List<Transport> transport) {
        this.transport = transport;
    }

    public List<Journey> getJourney() {
        return journey;
    }

    public void setJourney(List<Journey> journey) {
        this.journey = journey;
    }


}