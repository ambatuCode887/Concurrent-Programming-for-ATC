/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
public class RefuelTruck {
    
    private boolean isOccupied = false;
    
    public synchronized void fuel() throws InterruptedException{
        while(isOccupied){
            wait();
        }
        isOccupied = true;
        System.out.println("Refueling.");
    }
    
    public synchronized void refuelComplete(){
        isOccupied = false;
        System.out.println("Finished refueling.");
        notify();
    }
}
