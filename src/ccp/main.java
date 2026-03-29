/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;

import java.util.Random;

/**
 *
 * @author User
 */
public class main {
    public static void main(String[] args){
        Runway runway = new Runway();
        RefuelTruck rTruck = new RefuelTruck();
        Airport airport = new Airport(runway, rTruck);
        ATC atc = new ATC(airport);
        
        Random rand = new Random();
        Thread[] planes = new Thread[6];
        
        for(int i =0 ; i <6; i++){
            boolean isEmergency = (i == 4); //set plane 5 as emergency
            planes[i] = new Thread(new Airplane(i + 1, isEmergency, atc, airport));
            planes[i].start();
            
            try{
                Thread.sleep(rand.nextInt(2000));
            } catch (InterruptedException ex) {
                
            }
        }
        
        for (Thread plane : planes) {
            try {
                plane.join();
            } catch (InterruptedException ex) {}
        }
        
        
        System.out.println("\n========== STATISTICS ==========");
        System.out.println("All planes have left the airport.");
        System.out.println("Gate-1 empty: " + !airport.getGates()[0].isOccupied());
        System.out.println("Gate-2 empty: " + !airport.getGates()[1].isOccupied());
        System.out.println("Gate-3 empty: " + !airport.getGates()[2].isOccupied());
        System.out.println("Total planes served: 6");
        airport.printStatistics();
        System.out.println("================================");
        
        
    }
}
