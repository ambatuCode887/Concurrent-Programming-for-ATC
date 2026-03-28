/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
public class Runway {
    
    private boolean isOccupied = false;
    
    public synchronized void land() throws InterruptedException{
        while(isOccupied){
            wait();
        }
        isOccupied = true;
    }
    
    public synchronized void takeoff(){
        isOccupied = false;
        notify();
    }
}
