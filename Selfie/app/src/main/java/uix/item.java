package uix;

import java.util.ArrayList;

/**
 * Created by JuanJ on 5/9/2014.
 */
// TEST ONLY !
public class item {
    public String iName;
    public int iId;
    public float iPrice;
    public ArrayList<item> recom;
    public boolean isSpecial;
    public int iCategory;

    public String getName() {return iName;}
    public float getPrice() {return iPrice;}
    public ArrayList<item> getRecom () {return recom;}
    public boolean getSpecial() {return isSpecial;}
    public int getiId() {
        return iId;
    }
    public int getiCategory() {
        return iCategory;
    }

    public item(String newName, int newId, int newCategory, float newPrice, boolean special) {
        this.iName = newName;
        this.iId = newId;
        this.iCategory = newCategory;
        this.iPrice = newPrice;
        this.isSpecial = special;
    }
}
