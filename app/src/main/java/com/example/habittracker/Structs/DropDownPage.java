package com.example.habittracker.Structs;

import java.util.ArrayList;

public interface DropDownPage {
    public String getName();
    public DropDownPage getByIndex(int index);
    public ArrayList<String> getOptions();
    public ArrayList<DropDownPage> getChildren();
    public boolean hasChildren();
    public DropDownPage getParent();
    public ArrayList<String> getPath();

}
