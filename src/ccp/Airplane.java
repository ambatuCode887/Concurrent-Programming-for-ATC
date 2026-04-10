/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Airplane implements Runnable {
    private int planeNumber;
    private int passengerOnBoard;
    private int newPassengers;
    private boolean isEmergency;
    private ATC atc;
    private Airport airport;
    
    
    public Airplane(int planeNumber, boolean isEmergency, ATC atc, Airport airport) {
        this.planeNumber = planeNumber;
        this.isEmergency = isEmergency;
        this.passengerOnBoard = (int)(Math.random() * 50) + 1;
        this.newPassengers = (int)(Math.random() * 50) + 1;
        this.atc = atc;
        this.airport = airport;
    }
    
    @Override
    public void run(){
        long arrivalTime = System.currentTimeMillis();
        
        if(isEmergency) {
            System.out.println("Plane-" + planeNumber + ": EMERGENCY! Fuel shortage, requesting emergency landing!");
        }
        
        System.out.println("Plane-" + planeNumber + ": Requesting Landing.");
        Gate gate;
        try {
            gate = atc.requestLanding(planeNumber, isEmergency);
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        
        long waitingTime = System.currentTimeMillis() - arrivalTime;
        airport.addWaitingTime(waitingTime);
        
        Runway runway = airport.getRunway();
        System.out.println("Plane-" + planeNumber + ": Landing.");
        try {
            Thread.sleep(1000);
            System.out.println("Plane-" + planeNumber + ": Landed.");
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Plane-" + planeNumber + ": Coasting to Gate-" + gate.getGateNumber() + ".");
        try {
            Thread.sleep(1000);
            runway.takeoff();
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        System.out.println("Plane-" + planeNumber + ": DockedatGate-" + gate.getGateNumber());
        airport.notifyDocked();//notify atc plane has docked
        Thread disembark = new Thread(new DisembarkingPassenger(planeNumber, passengerOnBoard));
        
        Thread refuel = new Thread(() -> {
           try {
               airport.getRefuelTruck().requestFuel(planeNumber);
           } catch(InterruptedException ex) {
               
           }
        });
        
        Thread supplies = new Thread(() -> {
            try {
                System.out.println("Plane-" + planeNumber + ": Restocking supplies and cleaning.");
                Thread.sleep(2000); //cleaning
                System.out.println("Plane-" + planeNumber + ": Cleaning complete.");
                airport.getKitchen().requestFood(planeNumber);
            } catch(InterruptedException ex) {}
        });
        
        
        
        Thread embarking = new Thread(new EmbarkingPassenger(planeNumber, newPassengers));
        
        disembark.start();
        refuel.start();
        supplies.start();
        
        
        try { disembark.join(); } catch (InterruptedException ex) {
        
        }
        try { refuel.join(); } catch (InterruptedException ex) {
        
        }
        try { supplies.join(); } catch (InterruptedException ex) {
        
        }
        
        embarking.start();
        try { embarking.join(); } catch (InterruptedException ex) {
        
        }
        
        gate.undock();

        try {
            runway.land(); //wait for runway 
            System.out.println("Plane-" + planeNumber + ": Undocking.");
            System.out.println("Plane-" + planeNumber + ": Coasting to Runway.");
            Thread.sleep(1000);
            System.out.println("Plane-" + planeNumber + ": Requesting Taking off.");
            atc.requestTakeoff(planeNumber);
            runway.takeoff();
            System.out.println("Plane-" + planeNumber + ": Taking off.");
        } catch(InterruptedException ex) {}
    }
}
