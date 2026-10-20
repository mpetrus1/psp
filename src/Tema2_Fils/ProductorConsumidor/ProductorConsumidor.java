package Tema2_Fils.ProductorConsumidor;

public class ProductorConsumidor {
    public static void main(String[] args) {
        Contenidor<Integer> magatzem = new Contenidor<Integer>();
        Thread fprod = new Thread(new FilProductor(magatzem, "P"));
        Thread fcons = new Thread(new FilConsumidor(magatzem, "C"));
        fprod.start();
        fcons.start();
    }
}

 class Contenidor<T> {
    private T dada;
    synchronized public T get(){
        T result = this.dada;
        this.dada = null;
        return result;
    }

    synchronized public void put (T valor){
        this.dada = valor;
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
        for (int i = 0 ;;i++){
            synchronized (this.cont){
                while (this.cont.dadaDisponible()){
                    try{
                        this.cont.wait();
                    }catch (InterruptedException ex){

                    }
                }
                System.out.printf("Fil %s produeix valor %s.\n", this.nom, i);
                this.cont.put(i);
                this.cont.notify();
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
        while (true){
            synchronized (this.cont){
                while (!this.cont.dadaDisponible()){
                    try{
                        this.cont.wait();
                    }catch (InterruptedException ex){

                    }
                }
                Integer dada = this.cont.get();
                this.cont.notify();
                System.out.printf("Fil %s consumeix valor %d.\n", this.nom, dada);

            }
        }
    }
}



