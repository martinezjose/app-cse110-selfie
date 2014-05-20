package uix;

import java.util.ArrayList;

/**
 * Created by JuanJ on 5/9/2014.
 */
// TEST ONLY !
public class Test {

    private static ArrayList<ArrayList<item>> menu = new ArrayList<ArrayList<item>>();

    public static void init(int numCategory) {
        for(int i=0; i<numCategory; i++)
            menu.add(new ArrayList<item>());
    }

    public static void add(int category, item newItem) {
        menu.get(category).add(newItem);
    }

    public static ArrayList<item> getMenu(int category) {
        return menu.get(category);
    }

    public static item getItem(int itemId) {
        item theItem = null;
        for(int i=0; i<menu.size(); i++) {
            for(int j=0; j<menu.get(i).size(); j++) {
                if(menu.get(i).get(j).getiId() == itemId) {
                    theItem = menu.get(i).get(j);
                    break;
                }
            }
            if(theItem != null)
                break;
        }
        return theItem;
    }

    public static String[] getNames(int category) {
        String [] names = new String[menu.get(category).size()];
        for(int i=0; i<menu.get(category).size();i++) {
            names[i] = menu.get(category).get(i).getName();
        }
        return names;
    }

    public static ArrayList<item> getSpecials () {
        ArrayList<item> specials = new ArrayList<item>();
        for(int i=0; i<menu.size(); i++) {
            for(int j=0; j<menu.get(i).size(); j++) {
                if(menu.get(i).get(j).getSpecial()) {
                    specials.add(menu.get(i).get(j));
                }
            }
        }
        return specials;
    }
}
