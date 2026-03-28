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
    private Airport airport;
    
    public ATC(Airport airport){
        this.airport = airport;
    }
    
    public synchronized Gate requestLanding(int planeNumber){
        Gate gate = airport.getAvailableGate();
        
        if(gate == null){
            System.out.println("ATC: Landing Permission Denied for Plane-" + planeNumber + ", Airport Full.");
            return null;
        }
        
        System.out.println("ATC: Landing permission granted for Plane" + planeNumber + ".");
        return gate;
    }
    
    public synchronized void requestTakeoff(int planeNumber){
        System.out.println("ATC: Taking-off is granted for Plane-" + planeNumber + ". Runway is free.");
        airport.exitAirport();        
    }
    
    
}
