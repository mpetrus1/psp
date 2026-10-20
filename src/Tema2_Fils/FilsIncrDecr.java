package Tema2_Fils;

public class FilsIncrDecr {

    private static final int NUM_FILS_INC = 10;
    private static final int NUM_FILS_DEC = 10;

    public static void main(String[] args) {
        Comptador c = new Comptador(0);
        Thread[] filsInc = new Thread[NUM_FILS_INC];
        for (int i = 0 ; i < NUM_FILS_INC; i++){
            Thread th = new Thread(new FilIncr("INC"+i, c));
            filsInc[i] = th;
        }

        Thread[] filsDec = new Thread[NUM_FILS_DEC];
        for (int i = 0 ; i < NUM_FILS_DEC; i++){
            Thread th = new Thread(new FilDecr("DEC"+i, c));
            filsDec[i] = th;
        }

        for (int i = 0 ; i < NUM_FILS_INC ; i++){
            filsInc[i].start();
        }

        for (int i = 0 ; i < NUM_FILS_DEC ; i++){
            filsDec[i].start();
        }
    }
}

class Comptador{
    private int compta = 0;
    Comptador(int valorInicial){
        this.compta = valorInicial;
    }
    synchronized public int getCompta(){
        return compta;
    }

    synchronized public int incrementa(){
        this.compta++;
        return compta;
    }

    synchronized public int decrementa(){
        this.compta--;
        return compta;
    }
}

class FilIncr implements Runnable{
    private final String id;
    private final Comptador compt;

    FilIncr(String id, Comptador c){
        this.id = id;
        this.compt = c;
    }

    @Override
    public void run() {
        while (true){
            synchronized (this.compt){
                this.compt.incrementa();
                this.compt.notify();
                System.out.printf("Fil %s incrementa, valor comptador: %d\n", this.id, this.compt.getCompta());
            }
        }
    }
}

class FilDecr implements Runnable {

    private final String id;
    private final Comptador compt;

    FilDecr(String id, Comptador c){
        this.id = id;
        this.compt = c;
    }

    @Override
    public void run() {
        while (true){
            synchronized (this.compt){
                while (this.compt.getCompta()<1){
                    System.out.printf("!!! El fil %s no es pot decrementar, valor comptador: %d\n", this.id, this.compt.getCompta());
                    try{
                        this.compt.wait();
                    }catch (InterruptedException ex){

                    }
                }
                this.compt.decrementa();
                System.out.printf("Fil %s decrementa, valor comptador: %d\n", this.id, this.compt.getCompta());
            }
        }
    }
}
