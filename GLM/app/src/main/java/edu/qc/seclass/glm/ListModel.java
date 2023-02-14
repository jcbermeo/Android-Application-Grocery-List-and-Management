package edu.qc.seclass.glm;

import java.util.ArrayList;

/**
 * LIST CLASS
 */
public class ListModel {

    // LIST PARAMETERS
    public String listName;
    public ArrayList<ItemModel> items = new ArrayList<>();

    // EMPTY LIST CONSTRUCTOR
    public ListModel(){

    }

    // LIST NAME CONSTRUCTOR
    public ListModel(String name) {
        this.listName = name;
    }

    // STRING NAME AND ITEMS CONSTRUCTOR
    public ListModel(String l, ArrayList i) {

        listName = l;
        items = i;
    }

    // CHECK IF THE LIST CONTAINS AN ITEM OR NOT
    public boolean contains(String itemName) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).name.equals(itemName)) {
                return true;
            }
        }
        return false;
    }
}