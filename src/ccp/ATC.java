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
    
    public Gate requestLanding(int planeNumber, boolean isEmergency) throws InterruptedException{
       if(isEmergency) {
        System.out.println("ATC: EMERGENCY landing granted for Plane-" + planeNumber + "! Fuel shortage!" + "Waiting on Gate to be free");
        airport.enterAirportEmergency();
    } else {
        if(airport.isFull()) {
            System.out.println("ATC: Landing Permission Denied for Plane-" + planeNumber + ", Airport Full.");
        }
        airport.enterAirport();
    }
       
       airport.getRunway().land();

       Gate gate = airport.getAvailableGate();
       gate.reserve();
       System.out.println("ATC: Landing permission granted for Plane-" + planeNumber + ".");
       return gate;
    }
    
    public void requestTakeoff(int planeNumber){
        System.out.println("ATC: Taking-off is granted for Plane-" + planeNumber + ". Runway is free.");
        airport.exitAirport();        
    }
}
