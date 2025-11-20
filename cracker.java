import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;



public class cracker implements Runnable{

    private List<String> chunk;     //chunk de la lista que le corresponde
    private String salt;
    private int iterations;
    private String TargetHash;
    private static AtomicBoolean pass_encontrada =  new AtomicBoolean(false);       //Flag compartida por todos los objs de la clase


    //Constructor
    public cracker(List<String> chunk, String salt, int iterations, String TargetHash){
        //Inicializamos todas las variables
        this.chunk=chunk;
        this.salt=salt;
        this.iterations=iterations;
        this.TargetHash=TargetHash;
    }


    public void run(){
        //Bucle que vaya iterando por todas las plain-text pass
        for(int i = 0; i<chunk.size(); i++){
            if(pass_encontrada.get()) break;  //Si se encuentra la password -> el proceso termina su ejecucion
            if(PBKDF2Verifier.verifyPassword(chunk.get(i), salt, iterations, TargetHash)){      //Vamos comprobando las pass
                pass_encontrada.set(true);  //Se señaliza que se ha encontrado la flag
                System.out.println("Password found: " + chunk.get(i));
            }
        }
    }


    public static void main(String[] args) {
        //Ej de hash: pbkdf2:sha256:600000$AMtzteQIG7yAbZIa$0673ad90a0b4afb19d662336f0fce3a9edd0b7b19193717be28ce4d66c887133
        //Descomposicion del hash -> pbkdf2:<algorithm>:<iterations>$<salt>$<TargetHash>
        //Argumentos que recibira el programa:  1. path/to/wordlist.txt 2. iterations 3. salt 4. targethash

        // Validar argumentos
        if (args.length < 4) {
            System.out.println("Uso: java cracker <wordlist> <iterations> <salt> <targethash>");
            System.out.println("Ejemplo: java cracker rockyou.txt 600000 AMtzteQIG7yAbZIa 0673ad90a0b4afb19d662336f0fce3a9edd0b7b19193717be28ce4d66c887133");
            System.exit(-1);
        }
        String WordlistPah = args[0];
        //debug1
        System.out.println("Leyendo wordlist......");
        List<String> wordlist = WordlistReader.readWordlist(WordlistPah);
        if(wordlist == null){System.out.println("Proporciona un archivo valido"); System.exit(-1);}      //Se comprueba que la lista wordlist tenga contenido
        WordlistReader.printFileInfo(WordlistPah);      //Se muestra por pantalla info de la wdlst
        //variables necesarios para la concurrencia
        int nTareas = Runtime.getRuntime().availableProcessors();
        int nPasswords = wordlist.size();
        int tVentana = nPasswords/nTareas;
        int ini = 0;
        int fin = tVentana;
        //Ejecutor de pool de Thead
        ThreadPoolExecutor pool = new ThreadPoolExecutor(nTareas, nTareas, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());

        System.out.println("Intentando crackear el hash con " + nTareas + " hilos.......");
        long tmp_ini = System.nanoTime();   //Para medir el tiempo que tarda
        //Creamos las tareas y lanzamos los hilos
        for(int i=0; i<nTareas; i++){
            List<String> chunk = wordlist.subList(ini, fin);
            pool.execute(new cracker(chunk, args[2], Integer.parseInt(args[1]), args[3]));  //Lanzamos las hebras para que cada una procese una parte de la lista
            //Desplazamos la ventana
            ini = i * tVentana;
            if(i == tVentana-1)fin = Math.max(ini + tVentana, nPasswords);      //El ultimo chunk cogera todas las pass que se hayan quedado por el camino al hacer nPasswords/nTareas divion entera
            else fin = Math.min(ini + tVentana, nPasswords);
        }
        pool.shutdown();
        while(!pool.isTerminated());

        long tmp_total = (System.nanoTime() - tmp_ini)/(long)Math.pow(10,9);
        System.out.println("Tiempo transcurrido: " + tmp_total + "s");

    }


}