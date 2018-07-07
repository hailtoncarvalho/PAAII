/*
 
 */
package tp2grasp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
            this.setNumItens(Integer.parseInt(auxiliar[0]));
            this.setBinCapacity(Integer.parseInt(auxiliar[1]));
            leitor.close();
        }
        //Criação e instanciação do vetor de objetos Item.
        a = new Item[this.numItens + 1];
        for (int i = 0; i < this.numItens + 1; i++) {
            a[i] = new Item();
        }
        System.out.println(" a.length " + a.length);

        //Criação e instanciação de vetor de Lista, na solução.
        //Cada posição no vetor corresponde a 1 bin
        this.matrizCompativel = new int[this.numItens + 1][this.numItens + 1];
        String y;
        String[] x;
        int item, vizinho;
        double custo;

        //Instancia cada item e atribui seu respectivo ID e peso.
        for (int i = 1; i < this.linhas.size(); i++) {
            y = this.linhas.get(i);
            x = y.split(" ");
            a[i].setId(Integer.parseInt(x[0]));
            System.out.print(a[i].getId() + " ");
            a[i].setPeso(Integer.parseInt(x[1]));
            System.out.println(a[i].getPeso());
        }

        for (int i = 1; i < this.linhas.size(); i++) {
            y = this.linhas.get(i);
            x = y.split(" ");
            System.out.println();
            for (int k = 2; k < x.length; k++) {
                a[i].conflitaCom.add(a[Integer.parseInt(x[k])]);
            }

            for (int m = 0; m < a[i].conflitaCom.size(); m++) {
                System.out.print("Item "+i+ " >< "+a[i].conflitaCom.get(m).getId() + " ");
            }
            this.instancia.add(a[i]);
        }
        //Inicializa, e preenche matriz de itens compatíveis

        for (int i = 1; i < this.numItens + 1; i++) {
            for (int j = 1; j < this.numItens + 1; j++) {
                this.matrizCompativel[i][j] = 0;
            }
        }

        for (int i = 1; i < this.numItens + 1; i++) {

            for (int j = 1; j < this.numItens + 1; j++) {
                if (this.verConflito(a[i], a[j])) {
                    this.matrizCompativel[i][j] = 1;
                    //System.out.print(this.matrizCompativel[i][j]+" ");
                } else {
                    this.matrizCompativel[i][j] = 0;
                    //System.out.print(this.matrizCompativel[i][j]+" ");
                }
            }

        }

  
//     for(int i = 0;i<instancia.size();i++){
//         System.out.println(instancia.get(i).getId());
//     }
//    Collections.sort(instancia);
//        System.out.println("depois");
//    for(int i = 0;i<instancia.size();i++){
//         System.out.println(instancia.get(i).getPeso());
//     }
        
        System.out.println("Lower bound");
        this.lowerBound(instancia);
    return instancia;
    }

    public int getItens() {
        return this.numItens;
    }

    public void setNumItens(int a) {
        this.numItens = a;
    }

    public void setBinCapacity(int a) {
        this.binCapacity = a;
    }

    public int getBinCapacity() {
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
        if (a.conflitaCom.contains(b)||b.conflitaCom.contains(a)){
            return false;
        }else{
           return true;
        }
        
        
    }

    public void mostraMatrizCompativel(int[][] a) {
        System.out.println("Matriz de Compatibilidade: ");
        a = this.matrizCompativel;
        for (int i = 1; i < this.numItens + 1; i++) {
            for (int j = 1; j < this.numItens + 1; j++) {
                System.out.print(this.matrizCompativel[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Este método considera lista não ordenada, e adiciona itens enquanto for
     * possível adicionar no contêiner.
     *
     * @param a
     * @return Vetor de lista de itens.
     */

    public ArrayList<Item>[] binPackingGreedyRandom(ArrayList<Item> a) {
        //Cria vetor bins, onde lista de itens será alocada.
        this.solucao = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            this.solucao[i] = new ArrayList<>();
        }
        
        Item itemAuxiliar;
        Item itemAtual = null;
        int somaAtual = 0;
        bin = new ArrayList<>();

        Random r = new Random();
        int i = 1;
        while (!this.instancia.isEmpty()) {
            if (itemAtual == null) {                
                itemAtual = this.sorteiaItem(this.instancia);
                System.out.println(" 1. Sorteou! "+itemAtual.getId());
            }

            if (itemAtual != null &&this.solucao[i].isEmpty()) {
                this.solucao[i].add(itemAtual);
                this.removePorId(this.instancia, itemAtual.getId());
                System.out.println(" 2. Removeu! "+itemAtual.getId());
                itemAtual = null;
            } else {
                for (int k = 1; k <=i; k++) {
                    if (this.solucao[k].isEmpty()) {
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                        System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        i = k;
                        break;
                    } else if ((this.binConflito(this.solucao[k], itemAtual) == true)
                            && this.verCapacidade(this.solucao[k],itemAtual)) {
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                        System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        k = i;
                    }else{
                        i++;
                    }
                }
                
            }
        }
        return this.solucao;
    }

    /**
     * Algoritmo Guloso ordenado crescente: A instância é ordenada por ordem crescente
     * com base no item de menor peso, e o item é adicionado no contêiner é selecionado 
     * por um sorteio correspondente à metade dos itens
     * a serem alocados nos contêineres e enquanto houver espaço e não conflitar 
     * com os demais itens já alocados nele.
     * @param a
     * @return Retorna um vetor de lista que representa uma solução factível.
     */
    public ArrayList<Item>[] binPackingGreedySorted(ArrayList<Item> a) {
        //Cria vetor bins, onde lista de itens será alocada.
        this.solucao = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            this.solucao[i] = new ArrayList<>();
        }
        Item itemAuxiliar;
        Item itemAtual = null;
        int somaAtual = 0;

        Collections.sort(this.instancia);
        int i = 1;
        while (!this.instancia.isEmpty()) {
            Collections.sort(this.instancia);
            if (itemAtual == null) {                
                
                itemAtual = this.instancia.get(0);
                System.out.println(" 1. Sorteou! "+itemAtual.getId());
            }
            System.out.println(" i =  "+i);
            
//            if (this.solucao[i].isEmpty()) {
//                this.solucao[i].add(itemAtual);
//                this.removePorId(this.instancia, itemAtual.getId());
//               // System.out.println(" 2. Removeu! "+itemAtual.getId());
//                itemAtual = null;
//            } else {
                for (int k = 1; k <=i; k++) {
                    if (this.solucao[k].isEmpty()) {
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                        
               //         System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        break;
                       
                    } 
                    if (((this.binConflito(this.solucao[k], itemAtual) == true)
                            && this.verCapacidade(this.solucao[k],itemAtual))) { 
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                   //     System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        k++;
                        break;
                    }else{
                        i = solucao.length-1;
                    }
                }
            }
        
        return this.solucao;
    }
    
    public ArrayList<Item>[] binPackingGreedySortedReverse(ArrayList<Item> a) {
        //Cria vetor bins, onde lista de itens será alocada.
        this.solucao = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            this.solucao[i] = new ArrayList<>();
        }
        Item itemAuxiliar;
        Item itemAtual = null;
        int somaAtual = 0;

        Collections.sort(this.instancia);
        int i = 1;
        while (!this.instancia.isEmpty()) {
            Collections.sort(this.instancia);
            Collections.reverse(this.instancia);
            if (itemAtual == null) {                
                
                itemAtual = this.instancia.get(0);
                System.out.println(" 1. Sorteou! "+itemAtual.getId());
            }
            System.out.println(" i =  "+i);
            
//            if (this.solucao[i].isEmpty()) {
//                this.solucao[i].add(itemAtual);
//                this.removePorId(this.instancia, itemAtual.getId());
//               // System.out.println(" 2. Removeu! "+itemAtual.getId());
//                itemAtual = null;
//            } else {
                for (int k = 1; k <=i; k++) {
                    if (this.solucao[k].isEmpty()) {
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                        
               //         System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        break;
                       
                    } 
                    if (((this.binConflito(this.solucao[k], itemAtual) == true)
                            && this.verCapacidade(this.solucao[k],itemAtual))) { 
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                   //     System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        k++;
                        break;
                    }else{
                        i = solucao.length-1;
                    }
                }
            }
        
        return this.solucao;
    }
 /**
     * Algoritmo Guloso ordenado: A instância é ordenada, e o item é adicionado
     * no contêiner é selecionado por um sorteio correspondente à metade dos itens
     * a serem alocados nos contêineres e enquanto houver espaço e não conflitar 
     * com os demais itens já alocados nele.
     * @param a
     * @return Retorna um vetor de lista que representa uma solução factível.
     */
    public ArrayList<Item>[] binPackingHalfGreedy(ArrayList<Item> a) {
        //Cria vetor bins, onde lista de itens será alocada.
        this.solucao = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            this.solucao[i] = new ArrayList<>();
        }
        Item itemAuxiliar;
        Item itemAtual = null;
        int somaAtual = 0;
        Random r = new Random();
        Collections.sort(this.instancia);
        int i = 1;
        while (!this.instancia.isEmpty()) {
            Collections.sort(this.instancia);
            if (itemAtual == null) {                
                
                itemAtual = this.sorteiaItem2(this.instancia);
                System.out.println(" 1. Sorteou! "+itemAtual.getId());
            }

            if (itemAtual != null &&this.solucao[i].isEmpty()) {
                this.solucao[i].add(itemAtual);
                this.removePorId(this.instancia, itemAtual.getId());
                System.out.println(" 2. Removeu! "+itemAtual.getId());
                itemAtual = null;
            } else {
                for (int k = 1; k <=i; k++) {
                    if (this.solucao[k].isEmpty()) {
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                        System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        i = k;
                        break;
                    } else if ((this.binConflito(this.solucao[k], itemAtual) == true)
                            && this.verCapacidade(this.solucao[k],itemAtual)) {
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                        System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        k = i;
                    }else{
                        i++;
                    }
                }
            }
        }
        return this.solucao;
    }

    /**
     * Algoritmo Guloso ordenado por ordem decrescente com prioridade: A
     * instância é ordenada de forma decresceente, e os itens mais pesados são
     * adicionados nos primeiros contêineres, e o item subsequente é adicionado
     * no primeiro contêiner que couber, enquanto houver espaço e não conflitar
     * com os demais itens já alocados nele.
     *
     * @param a
     * @return Retorna um vetor de lista que representa uma solução factível.
     */
    public ArrayList<Item>[] binPackingGreedyZCW(ArrayList<Item> a) {
        //Cria vetor de lista de itens   
        this.solucao = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            this.solucao[i] = new ArrayList<>();
        }

        a = this.instancia;
        Collections.sort(this.instancia);
        Collections.reverse(this.instancia);

        //Inclui os n/2 primeiros itens mais pesados nos n/2 primeiros contêineres.
        for (int i = 1; i < this.solucao.length / 2; i++) {
            this.solucao[i].add(this.instancia.get(0));
            this.instancia.remove(0);
        }

        Item itemAuxiliar;
        Item itemAtual = null;
        int somaAtual = 0;
        bin = new ArrayList<>();

        Random r = new Random();
        int i = 1;
        while (!this.instancia.isEmpty()) {
            if (itemAtual == null) {
                itemAtual = this.sorteiaItem(this.instancia);
            }

            if (this.solucao[i].isEmpty()) {
                this.solucao[i].add(itemAtual);
                this.instancia = this.removePorId(this.instancia, itemAtual.getId());
                itemAtual = null;
            } else {
                for (int k = 1; k <= i; k++) {
                    if (this.solucao[k].isEmpty()) {
                        this.solucao[i].add(itemAtual);
                        this.instancia = this.removePorId(this.instancia, itemAtual.getId());
                        itemAtual = null;
                        k = i++;
                    } else if ((this.binConflito(this.solucao[k], itemAtual) == true)
                            && this.verCapacidade(this.solucao[k],itemAtual)) {
                        this.solucao[k].add(itemAtual);
                        this.instancia = this.removePorId(this.instancia, itemAtual.getId());
                        itemAtual = null;
                        k = i++;
                    }
                }

            }
            i++;
        }
        return this.solucao;
    }
/**
 * Bin Packing Grasp 1: Este algoritmo é baseado na Metaheurística GRASP (Greed
 * Adapted Search Procedure)- Procedimento de Busca Adaptativa Gulosa e  Aleatória proposta
 * por Feo e Resende (1995). Constrói solução de forma gulosa e aleatória. Consi-
 * derando que quanto mais próximo de 0, mais gulosa é a solução gerada, e quanto
 * mais próximo de 1, mais aleatória é a solução. Sendo assim, será considerado
 * múltiplos parâmetros alfa.
 * @param a
 * @param alpha
 * @return 
 */
        public ArrayList<Item>[] binPackingGrasp1(ArrayList<Item> a,double alpha) {
        //Cria vetor bins, onde lista de itens será alocada.
        this.solucao = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            this.solucao[i] = new ArrayList<>();
        }
        Item itemAuxiliar;
        Item itemAtual = null;
        int somaAtual = 0;
        Random r = new Random();
        
        int i = 1;
        while (!this.instancia.isEmpty()) {
            Collections.sort(this.instancia);
            if (itemAtual == null) {                
                
                itemAtual = this.sorteiaGrasp(this.instancia,alpha);
                System.out.println(" 1. Sorteou! "+itemAtual.getId());
            }

            if (itemAtual != null &&this.solucao[i].isEmpty()) {
                this.solucao[i].add(itemAtual);
                this.removePorId(this.instancia, itemAtual.getId());
                System.out.println(" 2. Removeu! "+itemAtual.getId());
                itemAtual = null;
            } else {
                for (int k = 1; k <=i; k++) {
                    if (this.solucao[k].isEmpty()) {
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                        System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        i = k;
                        break;
                    } else if ((this.binConflito(this.solucao[k], itemAtual) == true)
                            && this.verCapacidade(this.solucao[k],itemAtual)) {
                        this.solucao[k].add(itemAtual);
                        this.removePorId(this.instancia, itemAtual.getId());
                        System.out.println(" 2. Removeu! "+itemAtual.getId());
                        itemAtual = null;
                        k = i;
                    }else{
                        i++;
                    }
                }
            }
        }
        return this.solucao;
    }

    //criar método da fase de construção usar matrizes
    /**
     *
     * @param alfa
     * @return Uma solução factível para o problema
     */
    public ArrayList<Item>[] binPackingGrasp(double alfa, ArrayList<Item> a) {
        this.solucao = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            this.solucao[i] = new ArrayList<>();
        }

        a = this.instancia;

        //Cria Lista Restrita de Candidatos para cada item, e a preenche   
        this.lrc = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            lrc[i] = new ArrayList<>();
        }
        for (int i = 1; i < numItens + 1; i++) {
            for (int j = 1; j < numItens + 1; j++) {
                if (this.matrizCompativel[i][j] == 1) {
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

        while (!this.instancia.isEmpty()) {
            itemAtual = instancia.get(i);
            if (bin.isEmpty()) {
                bin.add(itemAtual);
                somaAtual = this.somaPesos(bin);
            } else {
                somaAtual = this.somaPesos(bin);
            }
            if (this.verCapacidade(this.solucao[i],itemAtual)) {

            }
            for (int j = 1; i < solucao.length; i++) {
                if (solucao[j].isEmpty()) {
                    solucao[j].add(itemAtual);
                    somaAtual = this.somaPesos(solucao[j]);
                } else {
                    for (int k = 0; k < solucao[j].size(); k++) {

                    }
                }
            }
            instancia.remove(i);
            i++;
        }
        return this.solucao;
    }

    public ArrayList<Item>[] binPackingGrasp2(double alfa, ArrayList<Item> a) {
        this.solucao = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            this.solucao[i] = new ArrayList<>();
        }

        a = this.instancia;

        //Cria Lista Restrita de Candidatos para cada item, e a preenche   
        this.lrc = new ArrayList[numItens + 1];
        for (int i = 1; i < this.numItens + 1; i++) {
            lrc[i] = new ArrayList<>();
        }
        for (int i = 1; i < numItens + 1; i++) {
            for (int j = 1; j < numItens + 1; j++) {
                if (this.matrizCompativel[i][j] == 1) {
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
        Collections.sort(instancia);
        while (!this.instancia.isEmpty()) {
            itemAtual = this.sorteiaGrasp(instancia, alfa);

            for (int x = 0; x < solucao.length; x++) {

            }

            instancia.remove(i);
            i++;
        }
        return this.solucao;
    }

    //testar método
    public boolean verCapacidade(ArrayList<Item> a,Item b) {
        int somaAtual = 0;
        for(int i = 0;i<a.size();i++){
            somaAtual+=a.get(i).getPeso();
        }
        return somaAtual + b.getPeso() <= this.binCapacity;
    }

    //testar método
    public int somaPesos(ArrayList<Item> a) {
        int soma = 0;
        for (int i = 0; i < a.size(); i++) {
            soma += a.get(i).getPeso();
        }
        return soma;
    }

    //testar método
    public ArrayList<Item> removePorId(ArrayList<Item> entrada, int id) {
        for (int i = 0; i < entrada.size(); i++) {
            if (entrada.get(i).getId() == id) {
                entrada.remove(i);
            }
        }
        return entrada;
    }

    //Funcionando corretamente.
    public boolean binConflito(ArrayList<Item> a, Item b) {
        boolean bool = true;
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).conflitaCom.contains(b)||b.conflitaCom.contains(a.get(i))) {
                bool = false;
            } 
        }
        return bool;
    }

    //testar método
    public Item sorteiaGrasp(ArrayList<Item> list, double alfa) {
        Item sorteado;
        int j;
        Random r = new Random();
        j = (int) Math.ceil( alfa*(r.nextInt(list.size())));
        sorteado = list.get(j);

        return sorteado;
    }

    public Item sorteiaItem(ArrayList<Item> list) {
        Item sorteado;
        int j;
        Random r = new Random();
        j = (int) Math.ceil(r.nextInt(list.size()));
        sorteado = list.get(j);
        return sorteado;
    }
    public Item sorteiaItem2(ArrayList<Item> list) {
        Item sorteado;
        int j;
        Random r = new Random();
        j = (int) Math.ceil(r.nextInt(list.size())*0.5);
        sorteado = list.get(j);
        return sorteado;
    }

    /**
     * Este método verifica se existe possibilidade de melhora em uma solução a
     * partir de movimentos baseados na distribuição dos itens contidos no
     * contêiner com menor QUANTIDADE DE ITENS.
     *
     * @param solucao
     * @return Uma possível solução melhor, ou a própria solução.
     */
    public ArrayList<Item>[] localSearch(ArrayList<Item>[] solucao) {
        int smallerBin = solucao[1].size();
        int somaAtual;
        Item itemAtual = null;
        ArrayList<Item>[] bestSolution = new ArrayList[this.numItens + 1];
        for (int i = 1; i < solucao.length; i++) {
            bestSolution[i].addAll(solucao[i]);
            if (smallerBin < solucao[i].size()) {
                smallerBin = i;
            }
        }

        for (int i = 0; i < solucao[smallerBin].size(); i++) {
            if (itemAtual == null && !solucao[smallerBin].isEmpty()) {
                itemAtual = solucao[smallerBin].get(0);
            }

            for (int k = 1; k < solucao.length; k++) {
                if ((this.binConflito(this.solucao[k], itemAtual) == true)
                        && this.verCapacidade(this.solucao[k],itemAtual)) {
                    bestSolution[k].add(itemAtual);
                    solucao[smallerBin] = this.removePorId(solucao[smallerBin], itemAtual.getId());
                    itemAtual = null;
                }
            }
        }
        return bestSolution;
    }

    /**
     * Este método verifica se existe possibilidade de melhora em uma solução a
     * partir de movimentos baseados na distribuição dos itens contidos no
     * contêiner com menor PESO.
     *
     * @param solucao
     * @return Uma possível solução melhor, ou a própria solução.
     */
    public ArrayList<Item>[] localSearch2(ArrayList<Item>[] solucao) {
        int weightLessBin;
        ArrayList<Item>[] bestSolution = new ArrayList[this.numItens + 1];
        for (int i = 1; i < solucao.length; i++) {
            bestSolution[i].addAll(solucao[i]);
        }
        return bestSolution;
    }

    /**
     * Este método verifica a quantidade de contêineres necessários para
     * distribuir os contêineres, e mostra os itens alocados para cada um.
     *
     * @param sol
     *
     */
    public void mostraSolucao(ArrayList<Item>[] sol) {
        int total = 0;

        for (int i = 1; i < sol.length; i++) {
            if (sol[i].isEmpty()) {
            } else {
                total += 1;
            }
        }
        System.err.println("Total de Conteineres :" + total);

        for (int i = 1; i < sol.length; i++) {
            if (!sol[i].isEmpty()) {
                System.out.print(" " + sol[i].size());
                for (Item a : sol[i]) {
                    System.out.print(" " + a.getId());
                }
                System.out.println();
            }

        }

    }
//    public void confereSolucao(ArrayList<Item>[] a){
//        boolean x = true;
//        for(int i = 1;i<=a.length;i++){
//            for(int j = 0;j<a[i].size();j++){
//                for(int l = 0;l<a[i].size();l++){
//                    if( this.somaPesos(a[i])<=this.binCapacity){
//                        System.out.println("Solução Inválida.");
//                    }else{
//                        System.out.println("Solução Factível.");
//                    }
//                        
//                }    
//            }
//        }
//    }

/**
 * Função lowerBound: Determina um limite inferior para a busca de soluções, de 
 * modo que o valor se refere ao primeiro inteiro maior que o somatório do peso 
 * de todos os itens dividido pela capacidade do contêiner.
 * @param a
 * @return LB
 */
public double lowerBound(ArrayList<Item> a){
 double soma = 0;
 int i;
    for( i = 0;i<a.size();i++)
    {soma += a.get(i).getPeso();
    }
return Math.ceil(soma/this.binCapacity);
}

public void EscreveArquivoSolucao(ArrayList<Item>[] sol, String nomeSol) throws IOException {
    /*Modificar o caminho ao reproduzir testes.*/
    FileWriter arq = new FileWriter("E:\\UFOP\\Mestrado\\Projeto e Análise de Algoritmo\\TP2Grasp\\test\\"+nomeSol);
    PrintWriter gravarArq = new PrintWriter(arq);

    int total = 0;
        for (int i = 1; i < sol.length; i++) {
            if (sol[i].isEmpty()) {
            } else {
                total += 1;
            }
        }
        gravarArq.printf(total+"\n");

        for (int i = 1; i < sol.length; i++) {
            if (!sol[i].isEmpty()) {
                gravarArq.printf(sol[i].size()+"");
              for (Item a : sol[i]) {
                    gravarArq.printf(" " + a.getId());
                  
                }
                gravarArq.printf("\n");
            }

        }
  arq.close();
    }
}