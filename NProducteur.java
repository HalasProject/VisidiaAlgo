import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.messages.*;
import visidia.simulation.SimulationConstants;


public class NProducteur extends Algorithm{
    
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

    
    public static int counter = 0;
    IntegerMessage consomateur_jeton = new IntegerMessage(Consomateur_t.length);
    
    public Object clone(){
        return new NProducteur();
        
    }

    public void init(){
        
        if (getId() != 0 ){
            putProperty("label","Producteur num " +  getId());
            Message jeton = sur_recpetion_de_jeton();
            String msg = produire(jeton)
            send_jeton(jeton,new StringMessage(msg));
            
            
        } else {
           putProperty("label","Consommateur");
           sendTo(0, consomateur_jeton);
        }
    
    }
    
    public Object produire(Message jeton){
        //generate rand number 0 jeton.
        int num_of_messages = (int)(Math.random()*((Integer.parseInt(jeton) - 0)+1))+0;
        for (i=0;i<num_of_messages;i++){
           Consomateur_t[in] = randomString();
           in = (in + 1) % Consomateur_t.length
        }
     
        consomateur_jeton = consomateur_jeton - 1;
        return msg;
    }
    

   public Message sur_recpetion_de_jeton() { 
       System.out.println("Producteur num " + getId() + "ready to recieve a message...");
       Message jeton =  receiveFrom(recvDoor.getNum());
       System.out.println("Recieved jeton" + jeton);
       return jeton;
   }

   public void send_jeton(Message jeton) {
       counter = counter + 1;
       sendTo(sendDoor.getNum(), jeton);
   }
    public Object consommer(){
         return null;
    }

    public String randomString() {
    String[] generatedString = {"Samedi","Dimanche","Lundi","Mardi","Mercredi","Jeudi","Vendredi"};
    double x = (int)(Math.random()*((7-0)+1))+0;
    return generatedString[(int)x];
    }

    public void information(){
        int neoud = (int) getNetSize();
        System.out.println("Nombre de producteur =  "+ (neoud-1));
        System.out.println("Nombre de consomateur =  "+1);
    }
}
