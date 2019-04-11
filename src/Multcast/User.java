package Multcast;

public class User  {
    
    public String name;
    
    /**
     * Atributo necessario para comunicacao do usuario com a rede.
     */
    public Channel channel;
    
    /**
     * Atributo necessario para visualizacao da dinamica do canal de
     * comunicacao, semelhante a ter uma TV e ligar num determinado Canal.
     */
    public Screen screen;

    public User(String name) {
        this.name = name;
        
        // iniciamos os servicos para um Usuario RSA
        screen = new Screen(this);
        channel = new Channel(this);
    }
    
}
