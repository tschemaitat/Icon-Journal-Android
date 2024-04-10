package com.example.habittracker.Structs.DropDownPages;

import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.PayloadOption;

import java.util.ArrayList;

public interface DropDownPage {
    public ItemPath getPathToPageWithName();
    public String getName();
    public DropDownPage getChildPage(String name);
    public DropDownPage getChildPage(int index);
    public boolean hasChildren();
    public ArrayList<DropDownPage> getChildren();
    public DropDownPage getParent();
    public ArrayList<PayloadOption> getOptions();
}
