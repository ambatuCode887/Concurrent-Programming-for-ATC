/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

/**
 *
 * @author User
 */
public class EmbarkingPassenger implements Runnable {
    
    private int planeNumber;
    private int passengerCount;
    
    public EmbarkingPassenger(int planeNumber, int passengerCount) {
        this.planeNumber = planeNumber;
        this.passengerCount = passengerCount;
    }
    
    @Override
    public void run (){
        System.out.println("Plane-" + planeNumber 
                + passengerCount + " Passenger embarking into Plane-" + planeNumber + " .");
        try{
            Thread.sleep(2000);
        } catch (InterruptedException ex){
            
        }
        
    }
    
}
