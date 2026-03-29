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
    
    public Gate(int gateNumber){
        this.gateNumber = gateNumber;
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
        notify();
    }
    
    public synchronized boolean isOccupied() {
        return isOccupied;
    }
    
    public synchronized void reserve(){
        isOccupied = true;
    }
}
