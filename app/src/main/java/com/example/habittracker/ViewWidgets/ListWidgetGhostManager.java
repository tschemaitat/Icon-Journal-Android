package com.example.habittracker.ViewWidgets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.Rectangle;
import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Widgets.ListWidgets.ListWidget;
import com.example.habittracker.Widgets.Widget;

import com.example.habittracker.defaultImportPackage.ArrayList;

public class ListWidgetGhostManager {
    private ListWidget listWidget;
    private Widget draggedWidget;
    private ArrayList<Rectangle> groupWidgetBoundList;
    private Context context;
    private int currentVacantIndex = -1;
    private WidgetLayout widgetLayout;
    private int vacantHeight = 200;
    private int originalIndex = -1;
    private ArrayList<GroupWidget> groupWidgetList;

    public ListWidgetGhostManager(ListWidget listWidget, Widget draggedWidget, Context context, WidgetLayout widgetLayout) {
        this.listWidget = listWidget;
        this.draggedWidget = draggedWidget;
        this.context = context;
        this.widgetLayout = widgetLayout;
        init();
    }


    public void init(){
        MainActivity.log("starting drag");
        ArrayList<Widget> groupWidgetList = widgetLayout.widgets();
        originalIndex = groupWidgetList.indexOf(draggedWidget);
        widgetLayout.remove(draggedWidget);
        groupWidgetList.remove(draggedWidget);
        View draggedView = draggedWidget.getView();
        int shadowWidth = draggedView.getWidth();
        int shadowHeight = getShadowHeight(draggedView);
        generateRectangleList(groupWidgetList, shadowWidth, shadowHeight, draggedView.getHeight(), MainActivity.scrollView.getScrollY());

        Drag drag = new Drag(shadowWidth, shadowHeight, this, context);
        drag.setIterateFunction((x, y, scrollY) -> {
            //MainActivity.log("iterating: " + x + ", " + y + ", scrollY: " + scrollY);
            int boundIndex = getIndexInBounds(x, y + scrollY);
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
                widgetLayout.add(draggedWidget, temp);
            }else{
                widgetLayout.add(draggedWidget, originalIndex);
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

    public void generateRectangleList(ArrayList<Widget> groupWidgetList, int shadowWidth, int shadowHeight,
                                      int draggedViewHeight, int scrollY){
        MainActivity.log("generating bounds list, starting scrollY: " + scrollY);
        groupWidgetBoundList = new ArrayList<>();
        int indexCount = 0;
        int addedY = 0;
        for(Widget widget: groupWidgetList){
            if(indexCount == originalIndex){
                addedY = draggedViewHeight;
            }
            View groupWidgetView = widget.getView();
            int[] tempPosArray = new int[2];
            int width = groupWidgetView.getWidth();
            int height = groupWidgetView.getHeight();
            groupWidgetView.getLocationOnScreen(tempPosArray);
            groupWidgetBoundList.add(new Rectangle(tempPosArray[0], tempPosArray[1] - addedY + scrollY, width, height));
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
