package com.example.djokica.execom_hackaton;

/**
 * Created by Danijel Sudar on 10/26/2016.
 */

public class Model {
    private String name;
    private boolean selected;
    private String id;

    public Model(String name, boolean selected,String id) {
        this.name = name;
        this.selected = selected;
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
