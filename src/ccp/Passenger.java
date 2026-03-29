/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
public class Passenger implements Runnable {
    private int planeNumber;
    private int passengerCount;
    private boolean isEmbarking;
    
    public Passenger(int planeNumber, int passengerCount, boolean isEmbarking) {
        this.planeNumber = planeNumber;
        this.passengerCount = passengerCount;
        this.isEmbarking = isEmbarking;
    }
    
    @Override
    public void run (){
        
        if(isEmbarking == true){
            System.out.println("Plane-" + planeNumber + "'s Passenger" + passengerCount + " embarking into Plane-" + planeNumber + ".");
        } else {
            System.out.println("Plane-" + planeNumber + "'s Passenger" + passengerCount + " disembarking from Plane-" + planeNumber + ".");
        }
        
        try{
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
        
        }
    }
    
}
