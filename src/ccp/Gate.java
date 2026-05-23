/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
public class Gate {
    private boolean isOccupied = false;
    private int gateNumber;
    private Airport airport;
    
    public Gate(int gateNumber, Airport airport){
        this.gateNumber = gateNumber;
        this.airport = airport;
    }
    
    public int getGateNumber(){
        return gateNumber;
    }
    
    public synchronized void dock() throws InterruptedException{
        while(isOccupied){ //if the dock was occupied then throw wait() function to suspend current thread and releases the object locks
            wait();
        }
        isOccupied = true; //once the dock is free it sets to true
    }
    
    public synchronized void undock(){
        isOccupied = false;
        notifyAll(); //using notifyAll() function to wake all thread that are currently waiting on the dock
        synchronized(airport){
            airport.notifyAll(); //safely acquire the lock on the airport object and wake up any thread that are currently waiting
        }
    }
    
    public synchronized boolean isOccupied() {
        return isOccupied;
    }
    
    public synchronized void reserve(){
        isOccupied = true;
    }
}
