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
    
    
    public synchronized void dock() throws InterruptedException{
        while(isOccupied){
            wait();
        }
        isOccupied = true;
        System.out.println("Dock at Gate-1");
    }
    
    public synchronized void undock(){
        isOccupied = false;
        System.out.println("Undocked from gate 1");
        notify();
    }
}
