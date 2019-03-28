/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Multcast;

import Multcast.Channel;

/**
 *
 * @author Lucian
 */
public class User  {
    
    public String name;
    public Channel channel;
    public Screen screen;

    public User(String name) {
        this.name = name;
        screen = new Screen(this);
        channel = new Channel(this);
        
        
//        screen.log("Mensagem atual");
//        screen.log("Mensagem atual");
//        screen.log("Mensagem atual");
//        screen.log("Mensagem atual");
//        screen.log("Mensagem atual");
    }
    
}
