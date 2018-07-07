/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2grasp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Hailton
 */
public class binPackingProblem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here
        ArrayList<Item> arquivo = new ArrayList<>();
        ArrayList<Item>[] solucao;
        Instancia lida = new Instancia();
        arquivo.addAll(lida.Instancia("da-BPWC_1_9_9.txt"));
        solucao = new ArrayList[lida.getBinCapacity()];        
        solucao = lida.binPackingGreedySorted(arquivo);
        lida.EscreveArquivoSolucao(solucao,"da-BPWC_1_9_9.sol");
        lida.mostraSolucao(solucao);
        System.out.println("Lower Bound: "+lida.lowerBound(arquivo));
        System.out.println("Bin capacidade "+lida.getBinCapacity());
        System.out.println("Quantidade de Itens "+lida.getItens());
       
        if(true==true){
        System.out.println("Tamaho "+arquivo.size());
        for(Item a: arquivo){
            //System.out.println(a.getId());
        }
        }
    }    
}