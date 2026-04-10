/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
public class ATC {
    private final Airport airport;
    
    public ATC(Airport airport){
        this.airport = airport;
    }
    
    public Gate requestLanding(int planeNumber, boolean isEmergency) throws InterruptedException {
        Gate gate;
        if(isEmergency) {
            System.out.println("ATC: EMERGENCY landing granted for Plane-" + planeNumber + "!");
            gate = airport.enterAirportEmergency();
        } else {
            if(airport.isFull()) {
                System.out.println("ATC: Landing Permission Denied for Plane-" + planeNumber + ", Airport Full.");
            }
            gate = airport.enterAirport(); // ← returns reserved gate
        }
        airport.getRunway().land();
        airport.waitForDock();
        System.out.println("ATC: Landing permission granted for Plane-" + planeNumber + ".");
        return gate;
    }
    
    public void requestTakeoff(int planeNumber){
        System.out.println("ATC: Taking-off is granted for Plane-" + planeNumber + ". Runway is free.");
        airport.exitAirport();        
    }
}
