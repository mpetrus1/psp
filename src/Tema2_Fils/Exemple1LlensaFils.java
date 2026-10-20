package Tema2_Fils;

public class Exemple1LlensaFils {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new FilAmbPausa("F1"));
        Thread t2 = new Thread(new FilAmbPausa("F2"));
        Thread t3 = new Thread(new FilAmbPausa("F3"));
        Thread t4 = new Thread(new FilAmbPausa("F4"));
        Thread t5 = new Thread(new FilAmbPausa("F5"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
    }
}

class Fil implements Runnable{
    private final String nom;

    Fil (String nom){
        this.nom = nom;
    }

    @Override
    public void run() {
        System.out.printf("Som el fil %s\n", this.nom);
    }
}