package Tema2_Fils;

import java.util.Random;

public class Exemple2LlensaFilEsperaAcabi {
    public static void main(String[] args) {
        Thread f1 = new Thread(new FilAmbPausa("H1"));
        Thread f2 = new Thread(new FilAmbPausa("H2"));
        Thread f3 = new Thread(new FilAmbPausa("H3"));
        f1.start();
        f2.start();
        f3.start();

        try {
            f1.join();
            f2.join();
            f3.join();
        } catch (InterruptedException e) {
            System.out.println("Fil principal interromput");
        }

        System.out.println("Tots els fils han acabat");
    }
}

class FilAmbPausa implements Runnable{
    private final String nom;

    FilAmbPausa(String nom) {
        this.nom = nom;
    }

    @Override
    public void run() {
        System.out.printf("Hola, soc el fil: %s.\n", this.nom);
        Random r = new Random();
        for (int i = 0 ; i < 5; i++){
            int pausa = 10 + r.nextInt(500-10);
            System.out.printf("Fil %s fa una pausa de %d ms.\n", this.nom, pausa);
            try {
                Thread.sleep(pausa);
            } catch (InterruptedException e) {
                System.out.printf("Fil %s interromput.\n", this.nom);
            }
        }
        System.out.printf("Fil %s acabat.\n", this.nom);
    }
}