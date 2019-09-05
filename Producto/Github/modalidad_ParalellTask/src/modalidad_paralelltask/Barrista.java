/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modalidad_paralelltask;

/**
 *
 * @author Elito
 */
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Barrista {

    static class HeatWater implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("Calentando agua...");
            Thread.sleep(1000);
            return "Agua caliente :V";
        }
    }

    static class GrindBeans implements Callable<String> {
        @Override
        public String call() throws Exception {
            System.out.println("Moliendo granos...");
            Thread.sleep(2000);
            return "Granos molidos";
        }
    }

    static class Brew implements Callable<String> {

        final Future<String> grindedBeans;
        final Future<String> hotWater;

        public Brew(Future<String> grindedBeans, Future<String> hotWater) {
            this.grindedBeans = grindedBeans;
            this.hotWater = hotWater;
        }

        @Override
        public String call() throws Exception
        {
            System.out.println("preparando café con " + grindedBeans.get()
                    + " y " + hotWater.get());
            Thread.sleep(1000);
            return "Café preparado";
        }
    }

    static class FrothMilk implements Callable<String> {

        @Override
        public String call() throws Exception {
            Thread.sleep(2000);
            return "Algo de leche";
        }
    }

    static class Combine implements Callable<String> {

        public Combine(Future<String> frothedMilk, Future<String> brewedCoffee) {
            super();
            this.frothedMilk = frothedMilk;
            this.brewedCoffee = brewedCoffee;
        }

        final Future<String> frothedMilk;
        final Future<String> brewedCoffee;

        @Override
        public String call() throws Exception {
            Thread.sleep(1000);
            System.out.println("Batiendo " + frothedMilk.get() + " "
                    + brewedCoffee.get());
            return "Café listo";
        }

    }

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(2);

        FutureTask<String> heatWaterFuture = new FutureTask<>(new HeatWater());
        FutureTask<String> grindBeans = new FutureTask<>(new GrindBeans());
        FutureTask<String> brewCoffee = new FutureTask<>(new Brew(grindBeans, heatWaterFuture));
        FutureTask<String> frothMilk = new FutureTask<>(new FrothMilk());
        FutureTask<String> combineCoffee = new FutureTask<>(new Combine(frothMilk, brewCoffee));

        executor.execute(heatWaterFuture);
        executor.execute(grindBeans);
        executor.execute(brewCoffee);
        executor.execute(frothMilk);
        executor.execute(combineCoffee);


        try {

            /**
             *  Warning this code is blocking !!!!!!!
             */         
            System.out.println(combineCoffee.get(20, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("20 SECONDS FOR A COFFEE !!!! I am !@#! leaving!!");
            e.printStackTrace();
        } finally{
                executor.shutdown();
            }
        }
    }
