/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import java.awt.PopupMenu;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Lucian
 */
public class Screen {

    public static void main(String args[]) {

        JButton botaoCarregar = new JButton("Carregar XML");
        JButton botaoSair = new JButton("Sair");

        JPanel painel = new JPanel();
        painel.add(botaoCarregar);
        painel.add(botaoSair);
        
        JFrame janela = new JFrame("Argentum");
        
        janela.add(painel);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.pack();
        janela.setVisible(true);
    }

}
