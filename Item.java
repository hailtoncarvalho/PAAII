/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp2grasp;

import java.util.ArrayList;

/**
 *
 * @author Hailton
 */
public class Item implements Comparable <Item> {
    private int id;
    private int peso;
    public ArrayList<Item> conflitaCom = new ArrayList<>();
    public Item(){
    
    }
    
//    public void addAll(ArrayList<Item> a){
//    
//        for(int i = 0;i<a.size();i++){
//            this.conflita.add(a.get(i));
//        }
//    }
     
    public void setId(int id){
        this.id = id;
}
    public int getId(){
    return this.id;
    }
        public void setPeso(int peso){
        this.peso = peso;
}
    public int getPeso(){
    return this.peso;
    }

    @Override
    public int compareTo(Item t) {
        if (this.peso < t.getPeso()){
            return -1;
           }else
            if(this.peso > t.getPeso()){
           return 1;
           }else{
            return 0;
            }
    }
}
