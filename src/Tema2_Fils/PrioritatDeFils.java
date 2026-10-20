package Tema2_Fils;

import java.util.ArrayList;

public class PrioritatDeFils {
    public static void main(String[] args) {
        ArrayList<Integer> prioritats = new ArrayList<Integer>();
        for (int i = Thread.MIN_PRIORITY; i <= Thread.MAX_PRIORITY; i++){
            prioritats.add(i);
        }

        try{
            ArrayList<FilAmbPrioritat> fils = new ArrayList<FilAmbPrioritat>();
            for (Integer prior : prioritats){
                FilAmbPrioritat fil = new FilAmbPrioritat();
                fil.setPriority(prior);
                fils.add(fil);
            }

            for (Thread fil : fils){
                fil.start();
            }

            int duradaSegons = 10;
            System.out.printf("Es deixa còrrer els fils durant %d segons.\n", duradaSegons);
            Thread.sleep(1000 * duradaSegons);

            for (Thread fil : fils){
                fil.interrupt();
            }

            for (FilAmbPrioritat fil : fils){
                System.out.printf("Comptador pel procés amb prioritat %d: %d\n", fil.getPriority(), fil.getComptador());
            }
        }catch (InterruptedException ex){
            System.out.println("Fil principal interromput.");
        }
    }
}

class FilAmbPrioritat extends Thread{
    long comptador = 0;

    public long getComptador(){
        return comptador;
    }

    private void incComptador(){
        comptador++;
    }

    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            this.incComptador();
        }
    }
}