

import java.util.*;

public class PageRank {

    CLI_RMP_Data CLI_RMP_Data;
    ArrayList<String> dico;
    double epsilon; //coeff pour savoir si le rank a convergé
    double dzap; //coeff pour la téléportation aléatoire
    double k;//nb de page aléatoirement choisit si une page n'a pas de lien externe

    public PageRank(CLI_RMP_Data CLI_RMP_Data, ArrayList<String> dico, double epsilon, double dzap, double k){
        this.epsilon = epsilon;
        this.dzap = dzap;
        this.k = k;
        this.CLI_RMP_Data = CLI_RMP_Data;
        this.dico = dico;
    }

    public TreeMap<Double, ArrayList<Integer>> generate(){
        ArrayList<Double> rank = new ArrayList<>();
        ArrayList<Double> rankNext = new ArrayList<>();
        for(int i = 0; i<CLI_RMP_Data.nbPage; i++) {
            rank.add(1.0 / CLI_RMP_Data.nbPage);
            rankNext.add(0.0);
        }
        while(true){
            //System.out.println("R1:"+rank);
            for(int i = 0; i<CLI_RMP_Data.nbPage; i++) {
                rankNext.set(i, 0.0);
            }
            for(int i = 0; i< CLI_RMP_Data.nbPage; i++){
                if(CLI_RMP_Data.ligne.get(i) == CLI_RMP_Data.ligne.get(i+1)){
                    ArrayList<Integer> A = kPagesAleatoire();
                    for(Integer p: A){
                        rankNext.set(p, rankNext.get(p) + rank.get(i) / k);
                    }
                }
                for(int j = CLI_RMP_Data.ligne.get(i); j<CLI_RMP_Data.ligne.get(i+1); j++){
                    rankNext.set(CLI_RMP_Data.index.get(j), rankNext.get(CLI_RMP_Data.index.get(j))
                            + CLI_RMP_Data.contenu.get(j) * rank.get(i));
                }
            }
            for(int i= 0; i<CLI_RMP_Data.nbPage; i++){
                rankNext.set(i, rankNext.get(i) * this.dzap + (1-this.dzap)/CLI_RMP_Data.nbPage);
            }
            if(distanceVecteur(rank, rankNext) < epsilon){
                return triPageRankParMot(rank);

            }
            rank = new ArrayList<>(rankNext);
        }
    }

    private ArrayList<Integer> kPagesAleatoire() {
        ArrayList<Integer> res = new ArrayList<Integer>();
        while(res.size() < k){
            //System.out.println("R2:"+res.size());
            int a = (int)(Math.random() * (CLI_RMP_Data.nbPage));
            if(!res.contains(a)){
                res.add(a);
            }
        }
        return res;
    }

    private TreeMap<Double, ArrayList<Integer>> triPageRankParMot(ArrayList<Double> rank) {
        //ordonner les pages par rank decroissant
        //rank -> liste des pages avec ce rank
        TreeMap<Double, ArrayList<Integer>> numero_rank = new TreeMap<>(Collections.reverseOrder());
        for (Integer numero: CLI_RMP_Data.numero_pages.values()){
            if(numero_rank.containsKey(rank.get(numero))){
                numero_rank.get(rank.get(numero)).add(numero);
            }
            else{
                ArrayList<Integer> nums = new ArrayList<>();
                nums.add(numero);
                numero_rank.put(rank.get(numero), nums);
            }
        }
        return numero_rank;
    }

    private double distanceVecteur(ArrayList<Double> l1, ArrayList<Double> l2){
        double d = 0.0;
        for(int i = 0; i < l1.size(); i++){
            d += Math.abs(l1.get(i) - l2.get(i));
        }
        //System.out.println("Distance: "+d);
        return d;
    }
}
