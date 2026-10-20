import com.jogamp.common.os.Platform;

import java.util.Random;

public class PasPerPont {
    public static void main(String[] args) {
        final Pont pont = new Pont();

        int tMinPerArribadaPersona = 1;
        int tMaxPerArribadaPersona = 30;
        int tMinPas = 10;
        int tMaxPas = 50;
        int minPesPersona = 40;
        int maxPesPersona = 120;

        System.out.println(">>>>>>>Comença la simulació.");
        Random r = new Random();
        int idPersona = 1;

        while (true){
            int tPerArribadaPersona = tMinPerArribadaPersona + r.nextInt(tMaxPerArribadaPersona-tMinPerArribadaPersona+1);
            int pesPersona = minPesPersona + r.nextInt(maxPesPersona-minPesPersona+1);

            System.out.printf("Següent persona arriba en %d segons.\n", tPerArribadaPersona);
            try{
                Thread.sleep(1000*tPerArribadaPersona);
            }catch (InterruptedException ex){
                System.out.printf("Interromput el procés principal");
            }

            Thread filPersona = new Thread(new Persona(pont, pesPersona, tMinPas, tMaxPas, "P"+idPersona));
            filPersona.start();
            idPersona++;
        }
    }
}

class Pont { //Estat, objecte compartit entre els fils
    private static final int PES_MAXIM = 200;
    private static final int MAX_PERSONES = 3;
    private int pes = 0;
    private int numPersones = 0;

    synchronized public int getPes(){
        return pes;
    }
    synchronized public int getNumPersones(){
        return numPersones;
    }

    synchronized public boolean autoritzacioPas(Persona persona){
        boolean result;
        if (this.pes + persona.getPes() <= Pont.PES_MAXIM && this.numPersones < Pont.MAX_PERSONES){
            this.numPersones++;
            this.pes += persona.getPes();
            result = true;
        }else {
            result = false;
        }
        return result;
    }
    synchronized public void acabaPas(Persona persona){
        this.pes -= persona.getPes();
        this.numPersones--;
    }
}

class Persona implements Runnable{
    private final String idPersona;
    private final int pes;
    private final int tMinPas, tMaxPas;
    private final Pont pont;

    public int getPes(){
        return pes;
    }

    public Persona(Pont pont, int pes, int tMinPas, int tMaxPas, String idP) {
        this.pont = pont;
        this.pes = pes;
        this.tMinPas = tMinPas;
        this.tMaxPas = tMaxPas;
        this.idPersona = idP;
    }

    @Override
    public void run() {
        System.out.printf("- %s de %d kg vol creuar, en el pont %d kg, %d person%s.\n",
                this.idPersona, this.pes, pont.getPes(), pont.getNumPersones(),
                pont.getNumPersones()!=1? "es" : "a");
        // Espera per obtenir autorització
        boolean autoritzat = false;
        while (!autoritzat){
            synchronized (this.pont){
                autoritzat = this.pont.autoritzacioPas(this);
                if (!autoritzat){
                    try{
                        System.out.printf("# %s ha d'esperar. \n", this.idPersona);
                        this.pont.wait();
                    }catch (InterruptedException ex){
                        System.out.printf("Interrupció mentre %s espera.\n", this.idPersona);
                    }
                }
            }
        }
        System.out.printf("> %s amb pes %d pot creuar, pes en el pont %d amb %d person%s.\n",
                this.idPersona, this.getPes(), this.pont.getPes(),
                pont.getNumPersones(), this.pont.getNumPersones()!=1 ? "es" : "a");

        Random r = new Random(); //Passa al pont i tarda un temps en creuar
        int tempsPas = this.tMinPas + r.nextInt(this.tMaxPas-this.tMinPas+1);
        try{
            System.out.printf("%s trigarà temps %d a passar.\n",
                    this.idPersona, tempsPas);
            Thread.sleep(1000*tempsPas);
        }catch (InterruptedException ex){
            System.out.printf("Interrupció mentre %s passa.\n", this.idPersona);
        }
        //Surt del pont
        synchronized (this.pont){
            this.pont.acabaPas(this);
            System.out.printf("< %s surt del pont, pont soporta pes %d, %d person%s.\n",
                    this.idPersona, this.pont.getPes(), this.pont.getNumPersones(),
                    this.pont.getNumPersones()!=1? "es" : "a");
            pont.notifyAll();
        }
    }
}