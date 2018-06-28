/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2grasp;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Hailton
 */
public class TP2Grasp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        ArrayList<Item> arquivo = new ArrayList<>();
        Instancia lida = new Instancia();
        arquivo = lida.Instancia("toy.txt");
        System.out.println();
        
        
    }    
}
