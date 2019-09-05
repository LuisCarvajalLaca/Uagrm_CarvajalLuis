/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modalidad_paralelltask;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Elito
 */
public class Modalidad_ParalellTask {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       //execute();
        Barrista.main(null);
    }
    private static void execute(){
        final CountDownLatch gate = new CountDownLatch(2);
        // thread a
        new Thread() {
            public void run() {
                System.out.println("Hilo A corriendo");
                gate.countDown();
            }
        }.start();

        // thread c
        new Thread() {
            public void run() {
                System.out.println("HILO C corriendo");
                gate.countDown();
            }
        }.start();

        new Thread() {
            public void run() {
                try {
                    System.out.println("Esperando el HILO A y C");
                    gate.await();
                    System.out.println("Hilo A y C Completo, iniciando hilo B");
                    // both thread a and thread c have completed
                    // process thread b
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
