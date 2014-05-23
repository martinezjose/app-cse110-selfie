package uix;

import classes.Item;

/**
 * Created by JuanJ on 5/11/2014.
 * This class keeps track of a single item in the order
 */
public class OrderDetail {
    public Item theItem;
    public int quantity;

    public OrderDetail(Item newItem, int newQuantity) {
        this.theItem = newItem;
        this.quantity = newQuantity;
    }

    //gets the item
    public Item getTheItem() {
        return theItem;
    }

    //gets the quantity of the item
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
