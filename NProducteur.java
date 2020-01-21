import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.messages.*;
import visidia.simulation.SimulationConstants;


public class NProducteur extends Algorithm {
    
    private static final long serialVersionUID = 1L;

    public int num_nodes = (int) getNetSize();

    //Variable de Producteur :
    public String[] Messages = {};
    public String[] Consomateur_t = new String[20];

    public int out = 0; // indice d'extraction
    public int in = 0; // Indice d'insertion

    public int num_auth = 0;

    public int nbmessi = 0; // Nombre de message stocké
    public int nbauto = 0; // Nombre d'autorisation d'envoi

    public Door recvDoor = new Door(0);
    public Door sendDoor = new Door(1);

    // public Door conso_sendDoor  = new Door(0)
    public Door conso_recvDoor  = new Door(1);

    
    public static int counter = 0;
    IntegerMessage consomateur_jeton = new IntegerMessage(Consomateur_t.length);
    
    public Object clone(){
        return new NProducteur();
        
    }

    public void init(){
        
      while(true) {
        if (getId() != 0 ){
          putProperty("label","Producteur num " +  getId());
            IntegerMessage jeton = sur_recpetion_de_jeton();

	    System.out.println("Producteur num" + getId() + " received JETON: " + jeton.value());
            IntegerMessage new_jeton = produire(jeton);
	    System.out.println("Producteur num" + getId() + " sending new JETON: " + new_jeton.value());
            send_jeton(new_jeton);

        } else {
	     putProperty("label","Consommateur, in=" + this.in + ", out=" + this.out);
	     System.out.println("------------------Consommateur---------");
	     sendTo(0, consomateur_jeton);

	     IntegerMessage jeton =  (IntegerMessage) receiveFrom(conso_recvDoor.getNum());
	     // putProperty("label","Consommateur, in=" + in + ", out=" + out);
	     System.out.println("[Consomateur received jeton] " + jeton.value());

	     consommer();

	     IntegerMessage new_jeton = new IntegerMessage(Consomateur_t.length);
	     sendTo(0, new_jeton);
	  }

        }
    
    }
    
    public IntegerMessage produire(IntegerMessage jeton){

        int num_of_messages = (int)(Math.random()*((jeton.value() - 0)+1))+0;
        for (int i=0; i < num_of_messages; i++){
	   String rand_str = randomString();
           Consomateur_t[in] = rand_str;
           in = (in + 1) % Consomateur_t.length;

        }
	putProperty("label", "Produced: " + num_of_messages + "messages");

	System.out.println("Current in value: " + this.in + " In Produteur num: " + getId());

	IntegerMessage new_jeton = new IntegerMessage(jeton.value() - num_of_messages);
	System.out.println("NEW JETON: " + new_jeton);
	return new_jeton;
    }
    

    public IntegerMessage sur_recpetion_de_jeton() { 
       System.out.println("Producteur num " + getId() + "ready to recieve a message...");
       IntegerMessage jeton =  (IntegerMessage) receiveFrom(recvDoor.getNum());
       System.out.println("Recieved jeton" + jeton);
       return jeton;
    }

    public void send_jeton(IntegerMessage jeton) {
       sendTo(1, jeton);
    }

    public void consommer(){
      while(out != in) {
	Consomateur_t[out] = null;
	out = (out + 1) % Consomateur_t.length;
      }
      System.out.println(Consomateur_t);

    }

    public String randomString() {
    String[] generatedString = {"Samedi","Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi"};
    int x = (int)(Math.random()*((6-0)+1))+0;
    return generatedString[x];
    }

    public void information(){
        int neoud = (int) getNetSize();
        System.out.println("Nombre de producteur =  "+ (neoud-1));
        System.out.println("Nombre de consomateur =  "+1);
    }
}
