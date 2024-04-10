package com.example.habittracker.Structs;

import java.util.ArrayList;
import java.util.HashMap;

public class Header {
    private HeaderNode parentNode;
    HashMap<TreeId, ValueTreePath> map;

    public Header(HeaderNode headerNode){
        parentNode = headerNode;
        ArrayList<ValueTreePath> pathList = getPathListFromTraverse();
        for(int i = 0; i < pathList.size(); i++){
            map.put(new TreeId(i), pathList.get(i));
        }
    }

    public HeaderNode getParentNode(){
        return parentNode;
    }

    public ArrayList<ValueTreePath> getPathListFromTraverse(){
        ArrayList<ValueTreePath> pathList = new ArrayList<>();
        parentNode.traverse(pathList, new ArrayList<>());
        return pathList;
    }

    public ValueTreePath getPath(TreeId treeId){
        return map.get(treeId);
    }

    public ArrayList<ValueTreePath> getPathList(ArrayList<TreeId> treeIdList){
        ArrayList<ValueTreePath> pathList = new ArrayList<>();
        for(TreeId treeId: treeIdList)
            pathList.add(getPath(treeId));
        return pathList;
    }


}
