/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ccp;
/**
 *
 * @author User
 */
public class main {
    public static void main(String[] args) {
        Runway runway = new Runway();
        RefuelTruck rTruck = new RefuelTruck();
        Kitchen kitchen = new Kitchen();
        Airport airport = new Airport(runway, rTruck, kitchen);
        
        ATC atc = new ATC(airport);
        Thread atcThread = new Thread(atc);
        atcThread.start();
    }
}

