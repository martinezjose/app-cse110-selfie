package cse110.selfie.app;

import java.util.ArrayList;

/**
 * Created by JuanJ on 5/9/2014.
 */
// TEST ONLY !
public class item {
    public String iName;
    public float iPrice;
    public ArrayList<item> recom;
    public boolean isSpecial;

    public String getName() {return iName;}
    public float getPrice() {return iPrice;}
    public ArrayList<item> getRecom () {return recom;}
    public boolean getSpecial() {return isSpecial;}

    public item(String newName, float newPrice, boolean special) {
        this.iName = newName;
        this.iPrice = newPrice;
        this.isSpecial = special;
    }
}
