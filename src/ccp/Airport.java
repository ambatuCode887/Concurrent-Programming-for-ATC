/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
public class Airport {
    private Gate[] gates = new Gate[3];
    Runway runway;
    RefuelTruck refuelTruck;
    private int planesOnGround = 0;
    
    public Airport(Runway runway, RefuelTruck refuelTruck){
        gates[0] = new Gate(1);
        gates[1] = new Gate(2);
        gates[2] = new Gate(3);
        this.refuelTruck = refuelTruck;
        this.runway = runway;
    }
    
    public Runway getRunway(){
        return runway;
    }
    
    public RefuelTruck getRefuelTruck() {
    return refuelTruck;
    }
    
    public synchronized void enterAirport() throws InterruptedException{
        while(planesOnGround >= 3){
            wait();
        }
        planesOnGround++;
    }
    
    public synchronized void exitAirport(){
         planesOnGround--;
         notify();
    }
    
    public synchronized Gate getAvailableGate(){
        for (Gate gate : gates) {
            if (!gate.isOccupied()) {
                return gate;
            }
        }
        return null;
    }
    
    
}
