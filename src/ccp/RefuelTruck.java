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
    public synchronized void incrementFuel(){
        for(int i = 0 ; i<5; i++){
            System.out.println("Refueling ");
        }
    }
    
    public synchronized void decrementRefuelComplete(){
        for(int i = 0; i<5; i++){
            System.out.println("Refuel complete ");
        }
    }
}
