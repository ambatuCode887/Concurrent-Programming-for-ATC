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
        while(isOccupied){ //here is the part where if the plane landed and haven't dock it will use the wait() function to pauses other thread
            //wanted to land but cannot
            wait();
        }
        isOccupied = true;
    }
    
    public synchronized void takeoff(){
        isOccupied = false; //once a plane takeoff meaning the runway are free and the runway are free so notify the next plane to land
        notify(); //notify works exactly waking up a thread that are waiting on this runway to let it know that the runway is free
    }
}
