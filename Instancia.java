/*
 
 */
package tp2grasp;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Comparator;

/**
 *
 * @author Hailton
 */
public class Instancia {

    private int numItens, binCapacity; //n itens, capacidade do bin
    private ArrayList<String> linhas = new ArrayList<>(); // guarda linhas do arquivo txt
    public ArrayList<Item>[] solucao; //guarda sequencia de vertices da solucao
    public int[][] matrizCompativel; // matriz de adjacencia
    public ArrayList<Item>[] lrc;
    ArrayList<Item> bin = new ArrayList<>();
    ArrayList<Item> instancia = new ArrayList<>(); 
    Item[] a;
    
    
    
    public ArrayList<Item> Instancia(String nomeArquivo) throws FileNotFoundException {
        
        try (Scanner leitor = new Scanner(new File(nomeArquivo))) {
            while (leitor.hasNextLine()) {
                this.linhas.add(leitor.nextLine());
            }
            String[] auxiliar = this.linhas.get(0).split(" ");
            this.setNumItens(Integer.parseInt(auxiliar[0])) ;
            this.setBinCapacity(Integer.parseInt(auxiliar[1]));           
            leitor.close();
        }
        //Criação e instanciação do vetor de objetos Item.
        a = new Item[this.numItens+1];
        for(int i = 0;i<this.numItens+1;i++){
            a[i] = new Item();
        }
        System.out.println(" a.length "+a.length);
        
        //Criação e instanciação de vetor de Lista, na solução.
        //Cada posição no vetor corresponde a 1 bin
        
        this.matrizCompativel = new int[this.numItens + 1][this.numItens + 1];        
        String y;
        String[] x;
        int item, vizinho;
        double custo;
        
        //Instancia cada item e atribui seu respectivo ID e peso.
        for(int i = 1; i < this.linhas.size(); i++){
            y = this.linhas.get(i);
            x = y.split(" ");
            a[i].setId(Integer.parseInt(x[0]));
            System.out.print(a[i].getId()+" ");
            a[i].setPeso(Integer.parseInt(x[1])); 
            System.out.println(a[i].getPeso());
        }
        
        for (int i = 1; i < this.linhas.size(); i++) {
            y = this.linhas.get(i);
            x = y.split(" ");
            System.out.println();
            for(int k = 2;k<x.length;k++){
                a[i].conflitaCom.add(a[Integer.parseInt(x[k])]);
            }
          
            for(int m = 0;m<a[i].conflitaCom.size();m++){
                System.out.print(a[i].conflitaCom.get(m).getId()+" ");
            }
            this.instancia.add(a[i]);
            }
        //Inicializa, e preenche matriz de itens compatíveis
        
        for(int i = 0;i<this.numItens+1;i++){
            for (int j = 0;j<this.numItens+1;j++){
                this.matrizCompativel[i][j] = 0;
                }
        }
        
        for(int i = 1;i<this.numItens+1;i++){
            
            for (int j = 1;j<this.numItens+1;j++){
                if(this.verConflito(a[i], a[j])){
                this.matrizCompativel[i][j] = 1;
                //System.out.print(this.matrizCompativel[i][j]+" ");
                }else{
                this.matrizCompativel[i][j] = 0;
                //System.out.print(this.matrizCompativel[i][j]+" ");
                }
            }
            
        }
     
    this.mostraMatrizCompativel(this.matrizCompativel);       
    
    //System.out.println("Instancia.size"+this.instancia.size()); //qtd itens
        System.out.println();

     
     for(int i = 0;i<instancia.size();i++){
         System.out.println(instancia.get(i).getPeso());
     }
//    Collections.sort(instancia);
//        System.out.println("depois");
//    for(int i = 0;i<instancia.size();i++){
//         System.out.println(instancia.get(i).getPeso());
//     }
        System.out.println("Sorteado: "+this.sorteiaItem(instancia, 0.5).getId());
    return instancia;
    }

    public int getItens() {
        return this.numItens;
    }
    public void setNumItens(int a){
    this.numItens = a;
    }
    
    public void  setBinCapacity(int a){
    this.binCapacity = a;
    }
    
    public int getBinCapacity(){
    return this.binCapacity;
    }
    
    /**
     * @param void
     * @return Lista de elementos e seus respectivos conflitos.
     */
    public void mostraArquivo() {
        for (int i = 0; i < this.linhas.size(); i++) {
            System.out.println(this.linhas.get(i));
        }
    }
    
    /**
     * 
     * @param a
     * @param b
     * @return Verdadeiro, caso os dois itens possam ser alocados juntos. 
     */
    public boolean verConflito(Item a, Item b) {
        return !(b.conflitaCom.contains(a)||a.conflitaCom.contains(b));
    }
   
    public void mostraMatrizCompativel(int[][] a){
    System.out.println("Matriz de Compatibilidade: ");
    a = this.matrizCompativel;
    for(int i = 1;i<this.numItens+1;i++){
        for(int j = 1;j<this.numItens+1;j++){
            System.out.print(this.matrizCompativel[i][j]+" ");
        }
        System.out.println();
        }
    }
    //criar método da fase de construção usar matrizes
    /**
     *
     * @param alfa
     * @return Uma solução factível para o problema
     */
    public ArrayList<Item>[] binPackingGrasp (double alfa, ArrayList<Item> a ){
        this.solucao = new ArrayList[numItens+1];
        for(int i = 1;i<this.numItens+1;i++){
            this.solucao[i] = new ArrayList<>();
        }
        
        a = this.instancia;
              
        //Cria Lista Restrita de Candidatos para cada item, e a preenche   
        this.lrc = new ArrayList[numItens+1];
        for(int i = 1;i<this.numItens+1;i++){
            lrc[i] = new ArrayList<>();
        }
        for(int i = 1;i<numItens+1;i++){
            for(int j = 1;j<numItens+1;j++){
                if(this.matrizCompativel[i][j] == 1){
                    lrc[i].add(this.instancia.get(j));               
                }
            }
        }
        
        Item itemAuxiliar;
        Item itemAtual = this.instancia.get(0);
        int somaAtual = 0;
        bin = new ArrayList<>();
        //Enquanto houver item fora de algum contêiner, atribuí-lo a um.
        int i = 0;
        Random r = new Random();
        
        while(!this.instancia.isEmpty()){
            itemAtual = instancia.get(i);
            if (bin.isEmpty()){
                bin.add(itemAtual);
                somaAtual = this.somaPesos(bin);
            }else{
            somaAtual = this.somaPesos(bin);
            }
            if (this.verCapacidade(itemAtual,somaAtual)){
                
            }
            for(int j = 1; i<solucao.length; i++){
                if(solucao[j].isEmpty()){
                solucao[j].add(itemAtual);
                somaAtual = this.somaPesos(solucao[j]);
                }else{
                  for(int k = 0;k < solucao[j].size();k++){
                  
                  }
                }
            }
            instancia.remove(i);
            i++;
        }
    return this.solucao;
    }
    
    //testar método
    public boolean verCapacidade(Item a, int somaAtual){
        return somaAtual + a.getPeso() <= this.binCapacity;
    }
    
    //testar método
    public int somaPesos(ArrayList<Item> a){
        int soma = 0;
        for(int i = 0; i<a.size(); i++){
            soma += a.get(i).getPeso();
        }
        return soma;
    }
    //testar método
    public ArrayList<Item> removePorId(ArrayList<Item> entrada, int id){
        for(int i = 0;i<entrada.size();i++){
            if(entrada.get(i).getId() == id){
                entrada.remove(i);
            }
        }
    return entrada;
    }
    //testar método
    public boolean binConflito(ArrayList<Item> a, Item b){
        boolean bool = true;
        for(int i = 0; i<a.size(); i++){
            if(a.get(i).conflitaCom.contains(b)){
            bool = false;
            }else{
            bool = true;
            }
        }
    return bool;
    }
    
    //testar método
    public Item sorteiaItem(ArrayList<Item> list, double alfa){
        Item sorteado;
        int j;
        Random r = new Random();       
        j = (int)Math.ceil((r.nextInt(list.size()))*alfa);
        sorteado = list.get(j);
        
     return sorteado;
    }
}