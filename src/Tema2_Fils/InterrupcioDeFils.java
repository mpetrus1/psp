package Tema2_Fils;

public class InterrupcioDeFils {
    public static void main(String[] args) {
        try{
            Thread f1 = new Thread(new FilInterrSleep("F1"));
            Thread f2 = new Thread(new FilInterrNoSleep("F2"));
            f1.start();
            f2.start();
            Thread.sleep(5000);
            f1.interrupt();
            f2.interrupt();
        }catch (InterruptedException ex){
            System.out.println("Fil principal interromput.");
        }
    }
}

class FilInterrSleep implements Runnable {
    String nom;

    public String getNom() {
        return nom;
    }

    FilInterrSleep(String nom) {
        this.nom = nom;
    }

    @Override
    public void run() {
        try{
            while (true){
                System.out.println(this.nom);
                Thread.sleep(1000);
            }
        } catch (InterruptedException ex){
            System.out.println("***INTERROMPUT*** " + this.getNom());
        }
    }
}

class FilInterrNoSleep implements Runnable{
    String nom;

    public String getNom(){
        return nom;
    }

    public FilInterrNoSleep(String nom) {
        this.nom = nom;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            for (long i = 0 ; i < 1000000000L; i++){

            }
            System.out.println(this.nom);
        }
        System.out.println("***INTERROMPUT+++ " +this.getNom());

    }
}

