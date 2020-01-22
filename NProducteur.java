import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.messages.*;
import visidia.simulation.SimulationConstants;
import java.util.Arrays; 
import java.io.IOException;


public class NProducteur extends Algorithm {

    public static final String ANSI_RESET =  "\u001B[0m";
    public static final String ANSI_GREEN =  "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String BG_GREEN =    "\u001B[42m";
    
    private static final long serialVersionUID = 1L;

    public int num_nodes = (int) getNetSize();

    public static String[] Consomateur_t = new String[7];

    public static int out = 0;
    public static int in = 0;

    public Door recvDoor = new Door(0);
    public Door sendDoor = new Door(1);

    public Door conso_recvDoor  = new Door(1);

    static IntegerMessage  consomateur_jeton = new IntegerMessage(Consomateur_t.length);
    
    public Object clone(){
        clearScreen();
        return new NProducteur();
    }

    public void init(){
         
        while(true) {
        if (getId() != 0 ){
            putProperty("label","Producteur N°" +  getId());
            IntegerMessage jeton = sur_recpetion_de_jeton();
	        System.out.println("[ * * . ] [RECEPTION REUSSI] Producteur N°" + getId());
	        
            IntegerMessage new_jeton = produire(jeton);

            System.out.println("[ENVOI NEW TOKEN ] => "+ new_jeton.value());
            send_jeton(new_jeton);
        } else {
	        putProperty("label","Consommateur");
	        sendTo(0, consomateur_jeton);


	        IntegerMessage jeton =  (IntegerMessage) receiveFrom(conso_recvDoor.getNum());
	        System.out.println(BG_GREEN + "                             [CONSOMATEUR]: ("+ jeton.value()+")                         "  + ANSI_RESET);
            
	        consommer();
        }
      }
    }
    
    public IntegerMessage produire(IntegerMessage jeton){
        int num_of_messages = (int)(Math.random()*((jeton.value() - 0)+1))+0;
         System.out.println("[ * * * ] [MESSAGE A PRODUIRE] => "+num_of_messages);
        for (int i=0; i < num_of_messages; i++){
	       String rand_str = randomString();
           this.Consomateur_t[in] = rand_str;
           this.in = (this.in + 1) % Consomateur_t.length;
        }
        
        System.out.println("[TAMPON]: ");
        afficherTampon();
        System.out.println(ANSI_YELLOW + "------------------------------------------------------------------------" + ANSI_RESET);

	    IntegerMessage new_jeton = new IntegerMessage(jeton.value() - num_of_messages);
	    return new_jeton;
    }

    public IntegerMessage sur_recpetion_de_jeton() { 
       System.out.println(ANSI_GREEN + "[LIBRE] " + ANSI_RESET + "Producteur N° " + getId());
       IntegerMessage jeton =  (IntegerMessage) receiveFrom(recvDoor.getNum());
       System.out.println("[ * . . ] [ENVOI TOKEN] => " + jeton);
       return jeton;
    }

    public void send_jeton(IntegerMessage jeton) {
       sendTo(1, jeton);
    }

    public void consommer(){
     Consomateur_t = new String[7];
      while(out != in) {
	    this.Consomateur_t[out] = null;
	    this.out = (this.out + 1) % Consomateur_t.length;
      }
        afficherTampon();
        in = 0;
        out = 0;
    }

    public String randomString() {
        String[] generatedString = {"Paris","Madrid","Tokyo","Moscow","London","Alger","New York","Sheingai","Toronto"};
        int x = (int)(Math.random()*((8-0)+1))+0;
        return generatedString[x];
    }

    public void afficherTampon(){
         String str = Arrays.toString(Consomateur_t);
         System.out.println(str);
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    }
}