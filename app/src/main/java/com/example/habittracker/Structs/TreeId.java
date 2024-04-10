package com.example.habittracker.Structs;

public class TreeId {
    private int id;
    public TreeId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String toString(){
        return "tree id: " + id;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }
    @Override
    public boolean equals(Object object){
        if(object instanceof TreeId){
            TreeId treeId = ((TreeId) object);
            if(treeId.id == id)
                return true;
        }
        return false;
    }
}
