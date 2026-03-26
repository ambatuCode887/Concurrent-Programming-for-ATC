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
        System.out.println("Landing");
        isOccupied = true;
        System.out.println("Landed");
    }
    
    public synchronized void takeoff(){
        isOccupied = false;
        System.out.println("Requesting Taking off.");
        System.out.println("Taking off");
        notify();
    }
}
