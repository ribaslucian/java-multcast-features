package Multcast;

import Multcast.Channel;

public class User  {
    
    public String name;
    
    // atributo necessario para comunicacao do usuario com a rede
    public Channel channel;
    
    // atributo necessario para visualizacao da dinamica do canal de comunicacao
    public Screen screen;

    public User(String name) {
        this.name = name;
        
        // iniciamos os servicos para um Usuario RSA
        screen = new Screen(this);
        channel = new Channel(this);
    }
    
}
