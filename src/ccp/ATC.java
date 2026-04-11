/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
import java.util.Random;

public class ATC implements Runnable {
    private final Airport airport;
    
    public ATC(Airport airport) {
        this.airport = airport;
    }
    
    @Override
    public void run() {
        System.out.println("ATC: Airport is now open!");
        
        Random rand = new Random();
        Thread[] planes = new Thread[6];
        
        Thread truckThread = new Thread(airport.getRefuelTruck());
        Thread kitchenThread = new Thread(airport.getKitchen());
        truckThread.start();
        kitchenThread.start();
        
        for(int i = 0; i < 6; i++) {
            boolean isEmergency = (i == 4); 
            planes[i] = new Thread(new Airplane(i + 1, isEmergency, this, airport));
            planes[i].start();
            
            try {
                Thread.sleep(rand.nextInt(2000));
            } catch(InterruptedException ex) {}
        }
        
        for(Thread plane : planes) {
            try {
                plane.join();
            } catch(InterruptedException ex) {}
        }
        
        truckThread.interrupt();
        kitchenThread.interrupt();
        
        System.out.println("\n========== STATISTICS ==========");
        System.out.println("ATC: All planes have left the airport.");
        System.out.println("ATC: Sanity check - ");
        System.out.println("Gate-1 empty: " + !airport.getGates()[0].isOccupied());
        System.out.println("Gate-2 empty: " + !airport.getGates()[1].isOccupied());
        System.out.println("Gate-3 empty: " + !airport.getGates()[2].isOccupied());
        System.out.println("Total planes served: 6");
        airport.printStatistics();
        System.out.println("================================");
    }
    
    public Gate requestLanding(int planeNumber, boolean isEmergency) throws InterruptedException {
        Gate gate;
        if(isEmergency) {
            System.out.println("ATC: EMERGENCY landing granted for Plane-" + planeNumber + "! Fuel shortage!");
            gate = airport.enterAirportEmergency();
        } else {
            if(airport.isFull()) {
                System.out.println("ATC: Landing Permission Denied for Plane-" + planeNumber + ", Airport Full.");
            }
            gate = airport.enterAirport();
        }
        airport.getRunway().land();
        airport.waitForDock();
        System.out.println("ATC: Landing permission granted for Plane-" + planeNumber + ".");
        return gate;
    }
    
    public void requestTakeoff(int planeNumber) {
        System.out.println("ATC: Taking-off is granted for Plane-" + planeNumber + ". Runway is free.");
    }   
}

