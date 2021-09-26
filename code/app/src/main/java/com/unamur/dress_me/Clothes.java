package com.unamur.dress_me;

/**
 * @author Amélie Dieudonné
 * @version 1.0
 */
public abstract class Clothes {

    protected DressingActivity.Type type;
    protected int color;
    protected DressingActivity.Kind kind;

    /**
     * @author Amélie Dieudonné
     * @param nType type of the cloth
     * @param nColor color of the cloth
     * @param nKind kind of the cloth
     * Constructor of the class
     */
    public Clothes (DressingActivity.Type nType, int nColor, DressingActivity.Kind nKind){
        this.type = nType;
        this.color = nColor;
        this.kind = nKind;
    }

    //Observers

    /**
     * @author Amélie Dieudonné
     * @return type of the Cloth
     */
    public DressingActivity.Type getType(){
        return this.type;
    }

    /**
     * @author Amélie Dieudonné
     * @return color of the Cloth
     */
    public int getColor(){
        return this.color;
    }

    /**
     * @author Amélie Dieudonné
     * @return kind of the Cloth
     */
    public DressingActivity.Kind getKind(){
        return this.kind;
    }

    //Mutators

    /**
     * @author Amélie Dieudonné
     * @param nType type of the Cloth
     */
    public void setType(DressingActivity.Type nType){
        this.type = nType;
    }

    /**
     * @author Amélie Dieudonné
     * @param nColor color of the Cloth
     */
    public void setColor(int nColor){
        this.color = nColor;
    }

    /**
     * @author Amélie Dieudonné
     * @param nKind kind of the cloth
     */
    public void setKind(DressingActivity.Kind nKind){
        this.kind = nKind;
    }

}
