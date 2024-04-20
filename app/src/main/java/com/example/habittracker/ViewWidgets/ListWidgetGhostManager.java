package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.Rectangle;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidget;

import java.util.ArrayList;

public class ListWidgetGhostManager {
    private ListWidget listWidget;
    private GroupWidget draggedGroupWidget;
    private ArrayList<Rectangle> groupWidgetBoundList;
    private Context context;
    private int currentVacantIndex = -1;
    private WidgetLayout widgetLayout;
    private int vacantHeight = 200;
    private int originalIndex = -1;

    public ListWidgetGhostManager(ListWidget listWidget, GroupWidget draggedGroupWidget, Context context, WidgetLayout widgetLayout) {
        this.listWidget = listWidget;
        this.draggedGroupWidget = draggedGroupWidget;
        this.context = context;
        this.widgetLayout = widgetLayout;
        init();
    }


    public void init(){
        MainActivity.log("starting drag");
        ArrayList<GroupWidget> groupWidgetList = listWidget.getGroupWidgets();
        originalIndex = groupWidgetList.indexOf(draggedGroupWidget);
        widgetLayout.remove(draggedGroupWidget);
        groupWidgetList.remove(draggedGroupWidget);
        View draggedView = draggedGroupWidget.getView();
        int shadowWidth = draggedView.getWidth();
        int shadowHeight = getShadowHeight(draggedView);
        generateRectangleList(groupWidgetList, shadowWidth, shadowHeight, draggedView.getHeight());

        Drag drag = new Drag(shadowWidth, shadowHeight, this, context);
        drag.setIterateFunction((x, y) -> {
            //MainActivity.log("iterating: " + x + ", " + y);
            int boundIndex = getIndexInBounds(x, y);
            if(currentVacantIndex != boundIndex){
                MainActivity.log("new bounds index: " + boundIndex + ", at: " + x + ", " + y);
            }
            if(boundIndex == -1){
                if(currentVacantIndex != -1)
                    removeVacant();
                return;
            }
            if(currentVacantIndex != boundIndex){
                if(currentVacantIndex != -1)
                    removeVacant();
                addVacant(boundIndex);
            }


        });
        drag.setOnActionUpListener(()->{
            if(currentVacantIndex != -1){
                int temp = currentVacantIndex;
                removeVacant();
                widgetLayout.add(draggedGroupWidget, temp);
            }else{
                widgetLayout.add(draggedGroupWidget, originalIndex);
            }

        });
    }

    public int getIndexInBounds(int x, int y){
        int index = 0;
        for(Rectangle rectangle: groupWidgetBoundList){
            if(rectangle.inside(x, y))
                return index;
            index++;
        }
        return -1;
    }

    public void generateRectangleList(ArrayList<GroupWidget> groupWidgetList, int shadowWidth, int shadowHeight,
                                      int draggedViewHeight){
        groupWidgetBoundList = new ArrayList<>();
        int indexCount = 0;
        int addedY = 0;
        for(GroupWidget groupWidget: groupWidgetList){
            if(indexCount == originalIndex){
                addedY = draggedViewHeight;
            }
            View groupWidgetView = groupWidget.getView();
            int[] tempPosArray = new int[2];
            int width = groupWidgetView.getWidth();
            int height = groupWidgetView.getHeight();
            groupWidgetView.getLocationOnScreen(tempPosArray);
            groupWidgetBoundList.add(new Rectangle(tempPosArray[0], tempPosArray[1] - addedY, width, height));
            indexCount++;
        }
        Rectangle lastRectangle = groupWidgetBoundList.get(groupWidgetBoundList.size()-1);
        groupWidgetBoundList.add(new Rectangle(lastRectangle.getX(), lastRectangle.getY() + lastRectangle.getHeight(),
                shadowWidth, shadowHeight));
        MainActivity.log("widget bounds:\n");
        for(Rectangle bounds: groupWidgetBoundList)
            MainActivity.log("\t"+bounds.toString());

    }

    public void addVacant(int index){
        View view = new View(context);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, vacantHeight));
        widgetLayout.getLinLayout().addWithoutSettingParams(view, index);
        currentVacantIndex = index;
    }

    public void removeVacant(){
        widgetLayout.getLinLayout().removeAt(currentVacantIndex);
        currentVacantIndex = -1;
    }

    public int getShadowHeight(View groupWidgetView){
        int groupWidgetHeight = groupWidgetView.getHeight();
        return Math.min(groupWidgetHeight, 120);
    }


}
