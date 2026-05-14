/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ccp;
/**
 *
 * @author User
 */
import java.util.Random;

public class main {
    public static void main(String[] args) {
        ATC atc = new ATC();
        Thread atcThread = new Thread(atc);
        atcThread.setName("ATC-Thread");
        atcThread.start();

        Random rand = new Random();
        Thread[] planes = new Thread[6];

        for(int i = 0; i < 6; i++) {
            boolean isEmergency = (i == 4);
            planes[i] = new Thread(new Airplane(i + 1, isEmergency, atc, atc.getAirport()));
            planes[i].setName("Plane-" + (i + 1) + "-Thread");
            planes[i].start();

            try {
                Thread.sleep(rand.nextInt(2000));
            } catch(InterruptedException ex) {}
        }

        for(Thread plane : planes) {
            try {
                plane.join();
            } catch(InterruptedException ex) {}
        }

        atc.allPlanesDone();
    }
}

