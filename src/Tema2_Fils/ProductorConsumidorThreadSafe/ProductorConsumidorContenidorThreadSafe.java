package Tema2_Fils.ProductorConsumidorThreadSafe;

import java.util.Random;

public class ProductorConsumidorContenidorThreadSafe {
    public static void main(String[] args) {
        Contenidor<Integer> magatzem = new Contenidor<Integer>();
        Thread fprod1 = new Thread(new FilProductor(magatzem, "P1"));
        Thread fprod2 = new Thread(new FilProductor(magatzem, "P2"));
        Thread fcons1 = new Thread(new FilConsumidor(magatzem, "C1"));
        Thread fcons2 = new Thread(new FilConsumidor(magatzem, "C2"));
        fprod1.start();
        fprod2.start();
        fcons1.start();
        fcons2.start();
    }
}

 class Contenidor<T> {
    private T dada;
     public T get() throws InterruptedException{
        T result;
        synchronized (this){
            while (!this.dadaDisponible()){
                this.wait();
            }
            result = this.dada;
            this.dada = null;
            this.notifyAll();
        }
        return result;
    }

    public void put (T valor) throws InterruptedException{
        synchronized (this){
            while (this.dadaDisponible()){
                this.wait();
            }
            this.dada = valor;
            this.notifyAll();
        }
    }

    synchronized boolean dadaDisponible(){
        return (this.dada != null);
    }
}



class FilProductor implements Runnable {
    final Contenidor<Integer> cont;
    String nom;
    FilProductor(Contenidor<Integer> cont, String nom) {
        this.cont = cont;
        this.nom = nom;
    }

    @Override
    public void run() {
        Random r = new Random();
        for (int i = 0 ;;i++){
            try{
                Thread.sleep(10 + r.nextInt(50-10+1));
                this.cont.put(i);
                long numSec = Sequencia.getNumSec();
                System.out.printf("%d-Fil %s produeix valor %s.\n", numSec, this.nom,i);
            }catch (InterruptedException ex){

            }
        }
    }
}

class FilConsumidor implements Runnable {
    final Contenidor<Integer> cont;
    String nom;

    FilConsumidor(Contenidor<Integer> cont, String nom) {
        this.cont = cont;
        this.nom = nom;
    }

    @Override
    public void run() {
        Random r = new Random();
        while (true){
            try{
                Thread.sleep(10 + r.nextInt(50-10+1));
                Integer dada = this.cont.get();
                long numSec = Sequencia.getNumSec();
                System.out.printf("%d-Fil %s consumeix valor %d.\n", numSec, this.nom, dada);
            }catch (InterruptedException ex){

            }
        }
    }
}

class Sequencia{
    static private  long numSec = 0;
    synchronized static public long getNumSec(){
        return numSec++;
    }
}



