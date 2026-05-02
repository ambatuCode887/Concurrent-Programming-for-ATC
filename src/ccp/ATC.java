/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */

public class ATC implements Runnable {
    private Airport airport;
    private boolean done = false;

    public ATC() {
        Runway runway = new Runway();
        RefuelTruck refuelTruck = new RefuelTruck();
        Kitchen kitchen = new Kitchen();
        this.airport = new Airport(runway, refuelTruck, kitchen);
    }

    public Airport getAirport() {
        return airport;
    }

    @Override
    public void run() {
        System.out.println("ATC: Airport is now open!");

        // Start refuel truck and kitchen
        Thread truckThread = new Thread(airport.getRefuelTruck());
        Thread kitchenThread = new Thread(airport.getKitchen());
        truckThread.setName("RefuelTruck-Thread");
        kitchenThread.setName("Kitchen-Thread");
        truckThread.start();
        kitchenThread.start();

        // Wait until all planes are done
        synchronized(this) {
            while(!done) {
                try {
                    wait();
                } catch(InterruptedException ex) {}
            }
        }

        // Shut down
        truckThread.interrupt();
        kitchenThread.interrupt();

        // Print statistics
        printFinalStatistics();
    }

    /**
     * Called by Main when all planes finished
     */
    public synchronized void allPlanesDone() {
        done = true;
        notifyAll();
    }

    public Gate requestLanding(int planeNumber, boolean isEmergency) throws InterruptedException {
        Gate gate;
        if(isEmergency) {
            System.out.println("ATC: EMERGENCY landing granted for Plane-" + 
                planeNumber + "! Fuel shortage!");
            gate = airport.enterAirportEmergency();
        } else {
            if(airport.isFull()) {
                System.out.println("ATC: Landing Permission Denied for Plane-" + 
                    planeNumber + ", Airport Full.");
            }
            gate = airport.enterAirport();
        }
        airport.requestRunwayLand();
        airport.waitForDock();
        System.out.println("ATC: Landing permission granted for Plane-" + planeNumber + ".");
        return gate;
    }

    public void requestTakeoff(int planeNumber) {
        System.out.println("ATC: Taking-off is granted for Plane-" + 
            planeNumber + ". Runway is free.");
    }

    private void printFinalStatistics() {
        System.out.println("\n========== STATISTICS ==========");
        System.out.println("ATC: All planes have left the airport.");
        System.out.println("ATC: Sanity check -");
        System.out.println("Gate-1 empty: " + !airport.getGates()[0].isOccupied());
        System.out.println("Gate-2 empty: " + !airport.getGates()[1].isOccupied());
        System.out.println("Gate-3 empty: " + !airport.getGates()[2].isOccupied());
        System.out.println("Total planes served: 6");
        airport.printStatistics();
        System.out.println("================================");
    }
}

