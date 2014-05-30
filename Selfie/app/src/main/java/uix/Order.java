package uix;

import java.util.ArrayList;
import android.util.Log;

import classes.Item;

/**
 * Created by JuanJ on 5/11/2014.
 * Singleton pattern.
 * This class keeps the order.
 */

public class Order {
    private static ArrayList<OrderDetail> theOrder = new ArrayList<OrderDetail>();
    private static int TABLE_ID = 1;
    private static int PAIRING_CODE = 0;
    private static float TAX = 0.0825f;

    //add item to the order
    public static void add(Item newItem, int quantity) {
        int itemPos = -1;
        for(int i=0; i<theOrder.size(); i++) {
            if(theOrder.get(i).getTheItem().getItemID() == newItem.getItemID()) {
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
        for(int i=0; i<theOrder.size(); i++) {
        }
    }

    //gets the order
    public static ArrayList<OrderDetail> getTheOrder() {
        return theOrder;
    }

    public static float getSubtotal() {
        float temp = 0;
        float singleItem = 0;
        for(int i=0; i<theOrder.size(); i++) {
            singleItem = (float)theOrder.get(i).getQuantity() * theOrder.get(i).getTheItem().getPrice();
            temp += singleItem;
        }
        return temp;
    }

    public static void setTax (float tax) { TAX = tax; }

    public static float getTax () { return TAX; }

    public static void setTableId (int tableId) {
        TABLE_ID = tableId;
    }

    public static int getTableId () { return TABLE_ID; }

    public static void setPairing_Code (int pairing_code) { PAIRING_CODE = pairing_code; }

    public static int getPairing_Code () { return PAIRING_CODE; }
}
