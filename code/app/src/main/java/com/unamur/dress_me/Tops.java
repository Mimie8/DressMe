package com.unamur.dress_me;


import java.util.ArrayList;

/**
 * @author Amélie Dieudonné
 */
public class Tops extends Clothes{

    //Count the number of casual tops
    private static int nbrTopsCasual = 0;
    //Count the number of chic tops
    private static int nbrTopsChic = 0;
    //Count the number of sport tops
    private static int nbrTopsSport = 0;

    //Count the number of tops
    private static int nbrTops = getNumberTopsCasual() + getNumberTopsChic() + getNumberTopsSport();

    //Keep track of the created tops
    private static ArrayList<Tops> listTops = new ArrayList<>();

    /**
     * @author Amélie Dieudonné
     * @param nType type of the top
     * @param nColor color of the top
     * @param nKind kind of the top
     */
    public Tops (DressingActivity.Type nType, int nColor, DressingActivity.Kind nKind){
        super(nType, nColor, nKind);
        if (nKind==DressingActivity.Kind.CASUAL){
            nbrTopsCasual++;
        }
        if (nKind==DressingActivity.Kind.CHIC){
            nbrTopsChic++;
        }
        if (nKind==DressingActivity.Kind.SPORT){
            nbrTopsSport++;
        }
        listTops.add(this);
    }

    //---------------------------------------Observers

    /**
     * @author Amélie Dieudonné
     * @return nbrTopsCasual = number of casual tops
     */
    public static int getNumberTopsCasual()
    {
        return nbrTopsCasual;
    }
    /**
     * @author Amélie Dieudonné
     * @return nbrTopsChic = number of chic tops
     */
    public static int getNumberTopsChic()
    {
        return nbrTopsChic;
    }
    /**
     * @author Amélie Dieudonné
     * @return nbrTopsSport = number of sport tops
     */
    public static int getNumberTopsSport()
    {
        return nbrTopsSport;
    }

    /**
     * @author Amélie Dieudonné
     * @return the nbr of tops
     */
    public static int getNbrTops(){
        return getNumberTopsCasual() + getNumberTopsChic() + getNumberTopsSport();
    }

    /**
     * @author Amélie Dieudonné
     * @return listTops = the list containing every created tops
     */
    public static ArrayList<Tops> getListTops(){
        return listTops;
    }

    //------------------------------------Mutators

    /**
     * @author Amélie Dieudonné
     * @param n new number of casual tops
     */
    public static void setNumberTopsCasual(int n){ nbrTopsCasual = n;}
    /**
     * @author Amélie Dieudonné
     * @param n new number of chic tops
     */
    public static void setNumberTopsChic(int n){ nbrTopsChic = n;}
    /**
     * @author Amélie Dieudonné
     * @param n new number of sport tops
     */
    public static void setNumberTopsSport(int n){ nbrTopsSport = n;}

    /**
     * @author Amélie Dieudonné
     * Remove one casual tops and recompute the number of tops
     */
    public static void setMinusOneNumberTopsCasual(){
        nbrTopsCasual--;
        nbrTops = getNumberTopsCasual() + getNumberTopsChic() + getNumberTopsSport();
    }
    /**
     * @author Amélie Dieudonné
     * Remove one chic tops and recompute the number of tops
     */
    public static void setMinusOneNumberTopsChic(){
        nbrTopsChic--;
        nbrTops = getNumberTopsCasual() + getNumberTopsChic() + getNumberTopsSport();
    }
    /**
     * @author Amélie Dieudonné
     * Remove one sport tops and recompute the number of tops
     */
    public static void setMinusOneNumberTopsSport(){
        nbrTopsSport--;
        nbrTops = getNumberTopsCasual() + getNumberTopsChic() + getNumberTopsSport();
    }

    /**
     * @author Amélie Dieudonné
     * @param newListTops new of list of created tops
     * Set a new list containing all the created tops
     */
    public static void setListTops(ArrayList<Tops> newListTops){ listTops = newListTops;}

}
