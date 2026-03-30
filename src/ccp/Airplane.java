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
    private boolean isEmergency;
    private ATC atc;
    private Airport airport;
    
    
    public Airplane(int planeNumber, boolean isEmergency, ATC atc, Airport airport) {
        this.planeNumber = planeNumber;
        this.isEmergency = isEmergency;
        this.passengerOnBoard = (int)(Math.random() * 50) + 1;
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
        try {
            runway.land();
            System.out.println("Plane-" + planeNumber + ": Landing.");
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
        
        Thread disembark = new Thread(new Passenger(planeNumber, passengerOnBoard, false));
        
        Thread refuel = new Thread(() -> {
           RefuelTruck rTruck = airport.getRefuelTruck();
           try{
               rTruck.requestFuel(planeNumber);
               Thread.sleep(3000);
               rTruck.refuelComplete(planeNumber);
           } catch(InterruptedException ex) {}
        });
        
        Thread supplies = new Thread(() -> {
           System.out.println("Plane-" + planeNumber + ": Restocking supplies and cleaning.");
           try{
               Thread.sleep(2000);
               System.out.println("Plane-" + planeNumber + ": Restocking supplies and cleaning are complete.");
           }catch(InterruptedException ex){}
        });
        
        Thread embarking = new Thread(new Passenger(planeNumber, passengerOnBoard, true));
        
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
        
        System.out.println("Plane-" + planeNumber + ": Undocking.");
        gate.undock();
        
        System.out.println("Plane-" + planeNumber + ": Coasting to Runway.");
        try {
            Thread.sleep(1000);
            runway.land(); // ← acquire runway for takeoff
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Plane-" + planeNumber + ": Requesting Taking off.");
        atc.requestTakeoff(planeNumber);
        runway.takeoff();
        System.out.println("Plane-" + planeNumber + ": Taking off.");
    }
}
