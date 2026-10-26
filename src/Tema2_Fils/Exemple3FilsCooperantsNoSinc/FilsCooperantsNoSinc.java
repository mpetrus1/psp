package Tema2_Fils.Exemple3FilsCooperantsNoSinc;

public class FilsCooperantsNoSinc {
    private static final int NUM_FILS = 10;
    private static final int COMPTA_TOTAL = 100000;


    public static void main(String[] args) {
        Comptador c = new Comptador();
        Thread[] fils = new Thread[NUM_FILS];
        for (int i = 0; i < NUM_FILS ; i++){
            Thread th = new Thread(new Fil(i, COMPTA_TOTAL/NUM_FILS, c));
            th.start();
            fils[i] = th;
        }
        for (Thread h : fils){
            try{
                h.join();
            }catch (InterruptedException e){
            }
        }
        System.out.printf("Compta global: %s\n", c.getCompta());
    }
}


class Comptador{
    private int compta = 0;

    public int getCompta() {
        return compta;
    }

    public int incrementa(){
        this.compta++;
        return compta;
    }
}


class Fil implements Runnable{
    int numFil, laMevaPart, laMevaCompta = 0;
    private final Comptador comptador;

    public int getLaMevaCompta(){
        return laMevaCompta;
    }

    public Fil(int numFil, int laMevaPart, Comptador comptador) {
        this.numFil = numFil;
        this.laMevaPart = laMevaPart;
        this.comptador = comptador;
    }


    @Override
    public void run() {
        for (int i = 0 ; i < laMevaPart; i++){
            this.comptador.incrementa();
            laMevaCompta++;
        }
        System.out.printf("Fil %d acabat, compta: %d\n", numFil, getLaMevaCompta());
    }
}

