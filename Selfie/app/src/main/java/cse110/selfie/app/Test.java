package cse110.selfie.app;

import java.util.ArrayList;

/**
 * Created by JuanJ on 5/9/2014.
 */
// TEST ONLY !
public class Test {

    public ArrayList<item> menu;

    public Test() {
        menu = new ArrayList<item>();
    }

    public Test(ArrayList<item> newMenu) {
        this.menu = newMenu;
    }

    public ArrayList<item> getMenu () {
        return menu;
    }

    public void addMenu(item newItem) {
        menu.add(newItem);
    }

    public String[] getNames() {
        String [] names = new String[menu.size()];
        for(int i=0; i<menu.size();i++) {
            names[i] = menu.get(i).getName();
        }
        return names;
    }

}
