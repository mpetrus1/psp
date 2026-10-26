package Tema2_Fils.Exemple4Enquesta;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class Enquesta {
    private static final int NUM_ZONES = 20;


    public static void main(String[] args) {

        ResultatsEnquesta resultats = new ResultatsEnquesta();


        Thread[] enquestadors = new Thread [NUM_ZONES];
        for (int i = 0 ; i < NUM_ZONES ; i++){
            Thread enquestador = new Thread(new EnquestadorZona("zona" + (i+1), resultats));
            enquestadors[i] = enquestador;
        }


        for(Thread enquestador : enquestadors){
            enquestador.start();
        }
        for (Thread enquestador: enquestadors){
            try {
                enquestador.join();
            } catch (InterruptedException ex) {
            }
        }
        System.out.println("Enquestats per zones");
        Set<String> zones = resultats.obtenirZones();
        int granTotalPerZones = 0;
        for (String zona : zones){
            int totalPerZona = resultats.obtenirNumRespostesZona(zona);
            System.out.printf("%s: %d\n", zona, totalPerZona);
            granTotalPerZones+=totalPerZona;
        }
        System.out.printf("TOTAL: %d\n", granTotalPerZones);
        System.out.println("Resultats per resposta:");
        Set<String> respostes = resultats.obtenirRespostes();
        int granTotalPerRespostes = 0;
        for (String resposta : respostes){
            int totalPerResposta = resultats.obtenirNumRespostes(resposta);
            System.out.printf("%s: %d\n", resposta!=null? resposta : "NS/NC", totalPerResposta);
            granTotalPerRespostes += totalPerResposta;
        }
        System.out.printf("TOTAL: %d\n", granTotalPerRespostes);
    }
}


class EnquestadorZona implements Runnable{
    private final String idZona;
    private final ResultatsEnquesta resultats;


    EnquestadorZona(String idZona, ResultatsEnquesta resultats){
        this.idZona=idZona;
        this.resultats = resultats;
    }
    @Override
    public void run() {
        System.out.printf(">>Enquestador per la zona %s comença.\n", this.idZona);
        Random r = new Random();
        int numRespostes = 100 + r.nextInt(200-100)+1;
        for ( int i = 0 ; i < numRespostes ; i++){
            int numResposta = r.nextInt(10);
            String resposta = null;
            if (numResposta>0){
                resposta = "resposta_" + numResposta;
            }
            this.resultats.anotaResposta(this.idZona, resposta);
        }
        System.out.printf("##Enquestador de la zona %s acaba.\n", this.idZona);
    }
}


class ResultatsEnquesta{
    private final HashMap<String, Integer> totalPerResposta = new HashMap<>();
    private final HashMap<String, Integer> totalPerZona = new HashMap<>();


    synchronized public void anotaResposta(String idZona, String resposta){
        Integer numRespValor = this.totalPerResposta.get(resposta);
        this.totalPerResposta.put(resposta, numRespValor == null? 1 : numRespValor+1);
        Integer numRespZona = this.totalPerZona.get(idZona);
        this.totalPerZona.put(idZona, numRespZona == null? 1 : numRespZona+1);
    }
    synchronized public Set<String> obtenirZones(){
        return this.totalPerZona.keySet();
    }
    synchronized public Set<String> obtenirRespostes(){
        return this.totalPerResposta.keySet();
    }
    synchronized public int obtenirNumRespostesZona(String zona){
        return this.totalPerZona.get(zona);
    }
    synchronized public int obtenirNumRespostes(String resposta){
        return this.totalPerResposta.get(resposta);
    }
}

