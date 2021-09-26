package com.unamur.dress_me;


import java.util.ArrayList;

/**
 * @author Amélie Dieudonné
 */
public class Pants extends Clothes{

    //Count the number of casual pants
    private static int nbrPantsCasual = 0;
    //Count the number of chic pants
    private static int nbrPantsChic = 0;
    //Count the number of sport pants
    private static int nbrPantsSport = 0;

    //Count the number of pants
    private static int nbrPants = getNumberPantsCasual() + getNumberPantsChic() + getNumberPantsSport();

    //Keep track of the created pants
    private static ArrayList<Pants> listPants = new ArrayList<>();

    /**
     * @author Amélie Dieudonné
     * @param nType type of the pants
     * @param nColor color of the pants
     * @param nKind kind of the pants
     * Constructor
     */
    public Pants (DressingActivity.Type nType, int nColor, DressingActivity.Kind nKind){
        super(nType, nColor, nKind);
        if (nKind==DressingActivity.Kind.CASUAL){
            nbrPantsCasual++;
        }
        if (nKind==DressingActivity.Kind.CHIC){
            nbrPantsChic++;
        }
        if (nKind==DressingActivity.Kind.SPORT){
            nbrPantsSport++;
        }
        listPants.add(this);
    }

    //---------------------------------------Observers

    /**
     * @author Amélie Dieudonné
     * @return nbrPantsCasual = number of pants of kind casual
     */
    public static int getNumberPantsCasual()
    {
        return nbrPantsCasual;
    }
    /**
     * @author Amélie Dieudonné
     * @return nbrPantsChic = number of pants of kind chic
     */
    public static int getNumberPantsChic()
    {
        return nbrPantsChic;
    }
    /**
     * @author Amélie Dieudonné
     * @return nbrPantsSport = number of pants of kind sport
     */
    public static int getNumberPantsSport()
    {
        return nbrPantsSport;
    }
    /**
     * @author Amélie Dieudonné
     * @return number of pants
     */
    public static int getNbrPants(){
        return getNumberPantsCasual() + getNumberPantsChic() + getNumberPantsSport();
    }
    /**
     * @author Amélie Dieudonné
     * @return listPants = the list with every pants created
     */
    public static ArrayList<Pants> getListPants(){
        return listPants;
    }

    //-------------------------------------Mutators

    /**
     * @author Amélie Dieudonné
     * @param n new number of Pants of kind Casual
     */
    public static void setNumberPantsCasual(int n){ nbrPantsCasual = n;}
    /**
     * @author Amélie Dieudonné
     * @param n new number of Pants of kind Chic
     */
    public static void setNumberPantsChic(int n){ nbrPantsChic = n;}
    /**
     * @author Amélie Dieudonné
     * @param n new number of Pants of kind Sport
     */
    public static void setNumberPantsSport(int n){ nbrPantsSport = n;}


    /**
     * @author Amélie Dieudonné
     * Remove one pants of kind casual and recompute the number of pants
     */
    public static void setMinusOneNumberPantsCasual(){
        nbrPantsCasual--;
        nbrPants = getNumberPantsCasual() + getNumberPantsChic() + getNumberPantsSport();
    }
    /**
     * @author Amélie Dieudonné
     * Remove one pants of kind chic and recompute the number of pants
     */
    public static void setMinusOneNumberPantsChic(){
        nbrPantsChic--;
        nbrPants = getNumberPantsCasual() + getNumberPantsChic() + getNumberPantsSport();
    }
    /**
     * @author Amélie Dieudonné
     * Remove one pants of kind sport and recompute the number of pants
     */
    public static void setMinusOneNumberPantsSport(){
        nbrPantsSport--;
        nbrPants = getNumberPantsCasual() + getNumberPantsChic() + getNumberPantsSport();
    }

    /**
     * @author Amélie Dieudonné
     * @param newListPants set a new list containing all the pants created
     */
    public static void setListPants(ArrayList<Pants> newListPants){ listPants = newListPants;}

}
