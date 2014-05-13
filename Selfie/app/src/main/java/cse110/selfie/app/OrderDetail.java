package cse110.selfie.app;

/**
 * Created by JuanJ on 5/11/2014.
 * Keeps track of a single item in the order
 */
public class OrderDetail {
    public item theItem;
    public int quantity;

    public OrderDetail(item newItem, int newQuantity) {
        this.theItem = newItem;
        this.quantity = newQuantity;
    }

    //gets the item
    public item getTheItem() {
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
