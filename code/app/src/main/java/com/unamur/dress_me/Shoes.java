package com.unamur.dress_me;


import java.util.ArrayList;

/**
 * @author Amélie Dieudonné
 */
public class Shoes extends Clothes{

    //Count the number of casual shoes
    private static int nbrShoesCasual = 0;
    //Count the number of chic shoes
    private static int nbrShoesChic = 0;
    //Count the number of sport shoes
    private static int nbrShoesSport = 0;

    //Count the number of shoes
    private static int nbrShoes = getNumberShoesCasual() + getNumberShoesChic() + getNumberShoesSport();

    //Keep track of the created shoes
    private static ArrayList<Shoes> listShoes = new ArrayList<>();

    /**
     * @author Amélie Dieudonné
     * @param nType type of the shoes
     * @param nColor color of the shoes
     * @param nKind kind of the shoes
     * Constructor
     */
    public Shoes (DressingActivity.Type nType, int nColor, DressingActivity.Kind nKind){
        super(nType, nColor, nKind);
        if (nKind==DressingActivity.Kind.CASUAL){
            nbrShoesCasual++;
        }
        if (nKind==DressingActivity.Kind.CHIC){
            nbrShoesChic++;
        }
        if (nKind==DressingActivity.Kind.SPORT){
            nbrShoesSport++;
        }
        listShoes.add(this);
    }

    //--------------------------------------Observers

    /**
     * @author Amélie Dieudonné
     * @return nbrShoesCasual = the number of shoes of kind casual
     */
    public static int getNumberShoesCasual()
    {
        return nbrShoesCasual;
    }
    /**
     * @author Amélie Dieudonné
     * @return nbrShoesChic = the number of shoes of kind chic
     */
    public static int getNumberShoesChic()
    {
        return nbrShoesChic;
    }
    /**
     * @author Amélie Dieudonné
     * @return nbrShoesSport = the number of shoes of kind sport
     */
    public static int getNumberShoesSport()
    {
        return nbrShoesSport;
    }
    /**
     * @author Amélie Dieudonné
     * @return the number of created shoes
     */
    public static int getNbrShoes(){
        return getNumberShoesCasual() + getNumberShoesChic() + getNumberShoesSport();
    }

    /**
     * @author Amélie Dieudonné
     * @return listShoes = list of all the created shoes
     */
    public static ArrayList<Shoes> getListShoes(){
        return listShoes;
    }

    //-----------------------------------Mutators

    /**
     * @author Amélie Dieudonné
     * @param n new number of casual shoes
     */
    public static void setNumberShoesCasual(int n){ nbrShoesCasual = n;}
    /**
     * @author Amélie Dieudonné
     * @param n new number of chic shoes
     */
    public static void setNumberShoesChic(int n){ nbrShoesChic = n;}
    /**
     * @author Amélie Dieudonné
     * @param n new number of sport shoes
     */
    public static void setNumberShoesSport(int n){ nbrShoesSport = n;}

    /**
     * @author Amélie Dieudonné
     * Remove one shoes of kind casual and recompute the nbr of shoes
     */
    public static void setMinusOneNumberShoesCasual(){
        nbrShoesCasual--;
        nbrShoes = getNumberShoesCasual() + getNumberShoesChic() + getNumberShoesSport();
    }
    /**
     * @author Amélie Dieudonné
     * Remove one shoes of kind chic and recompute the nbr of shoes
     */
    public static void setMinusOneNumberShoesChic(){
        nbrShoesChic--;
        nbrShoes = getNumberShoesCasual() + getNumberShoesChic() + getNumberShoesSport();
    }
    /**
     * @author Amélie Dieudonné
     * Remove one shoes of kind sport and recompute the nbr of shoes
     */
    public static void setMinusOneNumberShoesSport(){
        nbrShoesSport--;
        nbrShoes = getNumberShoesCasual() + getNumberShoesChic() + getNumberShoesSport();
    }

    /**
     * @author Amélie Dieudonné
     * @param newListShoes new list containing all the created shoes
     * Set a new list containing all the created shoes
     */
    public static void setListShoes(ArrayList<Shoes> newListShoes){ listShoes = newListShoes;}

}
