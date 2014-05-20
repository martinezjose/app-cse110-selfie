package uix;

import java.util.ArrayList;

import classes.Item;

/**
 * Created by JuanJ on 5/11/2014.
 * Keeps a list of the current order
 */

public class Order {
    private static ArrayList<OrderDetail> theOrder = new ArrayList<OrderDetail>();
    public final static int TABLE_ID = 1;

    //add item to the order
    public static void add(item newItem, int quantity) {
        int itemPos = -1;
        for(int i=0; i<theOrder.size(); i++) {
            if(theOrder.get(i).getTheItem().getiId() == newItem.getiId()) {
                itemPos = i;
                break;
            }
        }

        if(itemPos != -1) {
            theOrder.get(itemPos).setQuantity(quantity);
        }
        else {
            OrderDetail od = new OrderDetail(newItem, quantity);
            theOrder.add(od);
        }
    }

    public static void remove(int position) {
        theOrder.remove(position);
    }

    public static void clear() {
        theOrder.clear();
    }

    //gets the order
    public static ArrayList<OrderDetail> getTheOrder() {
        return theOrder;
    }

    //gets the names of the items in the order
    public static String[] getNames() {
        String[] names = new String[theOrder.size()];
        for(int i=0; i<theOrder.size(); i++) {
            names[i] = theOrder.get(i).getTheItem().getName();
        }
        return names;
    }
}
