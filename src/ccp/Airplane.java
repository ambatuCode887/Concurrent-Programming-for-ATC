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
        System.out.println("Plane-" + planeNumber + ": Requesting Landing.");
        
        Gate gate = atc.requestLanding(planeNumber);
        if(gate == null){
            return;
        }
        
        System.out.println("Plane-" + planeNumber + ": Landing.");
        Runway runway = airport.getRunway();
        try {
            runway.land();
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Plane-" + planeNumber + ": Landed.");
         
        System.out.println("Plane-" + planeNumber + ": CoastingtoGate-" + gate.getGateNumber() + ".");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            gate.dock();
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Plane-" + planeNumber + ": DockedatGate-" + gate.getGateNumber());
        
        Thread disembark = new Thread(new Passenger(planeNumber, passengerOnBoard, false));
        
        Thread refuel = new Thread(() -> {
           RefuelTruck rTruck = airport.getRefuelTruck();
           try{
               System.out.println("Plane-" + planeNumber + ": Refueling started.");
               rTruck.fuel();
               Thread.sleep(3000);
               rTruck.refuelComplete();
               System.out.println("Plane-" + planeNumber + ": Refueling complete.");
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
        
        System.out.println("Plane-" + planeNumber + ": undocking");
        gate.undock();
        
        try {
            System.out.println("Plane-" + planeNumber + ": Coasting to Runway.");
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Airplane.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println("Plane-" + planeNumber + ": Requesting Taking off.");
        atc.requestTakeoff(planeNumber);
      
        runway.takeoff();
        System.out.println("Plane-" + planeNumber + ": Taking off");
    }
}
