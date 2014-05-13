package cse110.selfie.app;

import android.content.ClipData;

import java.util.ArrayList;
import java.util.List;

import cse110.selfie.app.OrderDetail;
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
            if(theOrder.get(i).getTheItem().getName() == newItem.getName()) {
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
