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
        while(isOccupied){
            wait();
        }
        isOccupied = true;
    }
    
    public synchronized void undock(){
        isOccupied = false;
        notifyAll();
        synchronized(airport){
            airport.notifyAll();
        }
    }
    
    public synchronized boolean isOccupied() {
        return isOccupied;
    }
    
    public synchronized void reserve(){
        isOccupied = true;
    }
}
