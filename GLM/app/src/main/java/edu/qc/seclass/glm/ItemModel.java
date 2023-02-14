package edu.qc.seclass.glm;

/**
 * ITEM CLASS
 */
public class ItemModel {

    // PAGE VARIABLES
    public String name, type;
    public int quantity;
    public boolean checkedOff;

    // EMPTY ITEM CONSTRUCTOR
    ItemModel() {

    }

    // REGULAR CONSTRUCTOR
    ItemModel(String n, String t, int q) {
        this.name = n;
        this.type = t;
        this.quantity = q;
        this.checkedOff = false;
    }

    // CHECK/UNCHECK AN ITEM
    public void check() {
        if (checkedOff) checkedOff = false;
        else checkedOff = true;
    }

    // CHANGE QUANTITY OF ITEM
    public void changeQuantity(int q) {
        this.quantity = q;
    }
}
