package com.example.habittracker.Widgets.StructureWidgets;

import android.content.Context;
import android.view.View;

import com.example.habittracker.Layouts.WidgetLayout;
import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.DropDownPageFactory;
import com.example.habittracker.Layouts.LinLayout;
import com.example.habittracker.StaticClasses.Margin;
import com.example.habittracker.R;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.Widgets.EntryWidgets.CustomEditText;
import com.example.habittracker.Widgets.EntryWidgets.DropDown;
import com.example.habittracker.Widgets.EntryWidgets.EntryDropDown;
import com.example.habittracker.Widgets.ListWidget;
import com.example.habittracker.Widgets.StaticDropDown;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.Widget;
import com.example.habittracker.Widgets.WidgetParams.DropDownParam;
import com.example.habittracker.Widgets.WidgetParams.EditTextParam;
import com.example.habittracker.Widgets.WidgetParams.ListParam;

public class StructureWidget implements Widget {
    private StructureWidgetHeaderView headerView = null;

    private StaticDropDown typeDropDown = null;

    private StructureWidgetList structureWidgetList = null;
    private StructureWidgetDropDown structureWidgetDropDown = null;
    private StructureWidgetEditText structureWidgetEditText = null;

    private Integer widgetIdTracker;

    private Runnable onDelete;

    Runnable onDataChangeListener;

    private Context context;

    private LinLayout layout;
    private Runnable moveUp;
    private Runnable moveDown;

    public StructureWidget(Context context, WidgetLayout parent) {
        MainActivity.log("new structure widget");
        this.context = context;
        layout = new LinLayout(context);
        layout.getView().setId(R.id.structureWidget);

        headerView = new StructureWidgetHeaderView(context, ()->{
            headerView.nameEditor.resetError();
        }, ()->{
            MainActivity.log("deleting structureWidget");
            parent.delete(this);
        }, ()->{
            MainActivity.log("moving up structureWidget");
            parent.moveUp(this);
        }, ()->{
            MainActivity.log("moving down structureWidget");
            parent.moveDown(this);
        }, ()->{
            //star button
        });
        layout.add(headerView.getView());

        typeDropDown = new StaticDropDown(context);
        layout.add(typeDropDown.getView());
        typeDropDown.setup(DropDownPageFactory.getTypes(), (itemPath, payload, prevRefItemPath, prevPayload) ->
                onTypeChange((String)payload, (String)prevPayload));
        typeDropDown.setHint("select type");
        Margin.setStructureWidgetLayout(layout);
        headerView.disableStar();
    }

    public void onDelete(){
        onDataChangeListener.run();
    }

    public LinLayout getLinLayout(){
        return layout;
    }

    public void disableNameEditor(){
        headerView.nameEditor.disableEdit();
    }

    public void onTypeChange(String type, String prevType){
        typeDropDown.resetError();
        //System.out.println("<StructureWidget>data changed");
        //System.out.println("type = " + type)
        if(type == null){
            if(prevType == null)
                return;
            setType();
            return;
        }

        if( ! type.equals(prevType) ){
            setType();
        }



    }

    public void onDataChange(){
        onDataChangeListener.run();
    }

    public String getType(){
        return (String)typeDropDown.getPayload();
    }

    public void setType(){
        String currentType = getType();
        //System.out.println("reset type");
        clearWidgets();
        typeSwitch:{
            if(currentType == null){
                if(headerView.isStarEnabled())
                    headerView.disableStar();
                //System.out.println("structure type null");
                return;
            }
            if(currentType.equals("list")){
                MainActivity.log("trying to disable star is it enabled?: " + headerView.isStarEnabled());

                if(headerView.isStarEnabled())
                    headerView.disableStar();
                MainActivity.log("after: " + headerView.isStarEnabled());
                structureWidgetList = new StructureWidgetList(context, layout);
                return;
            }
            if(currentType.equals("drop down")){
                if( ! headerView.isStarEnabled())
                    headerView.enableStar();
                structureWidgetDropDown = new StructureWidgetDropDown(context, layout);
                return;
            }


            if(currentType.equals("edit text")){
                if( ! headerView.isStarEnabled())
                    headerView.enableStar();
                structureWidgetEditText = new StructureWidgetEditText(context, layout);
                return;
            }
        }
    }

    public void clearWidgets(){
        if(structureWidgetList != null)
            layout.remove(structureWidgetList.getView());
        structureWidgetList = null;
        if(structureWidgetDropDown != null)
            layout.remove(structureWidgetDropDown.getView());
        structureWidgetDropDown = null;
    }


    public EntryWidgetParam getWidgetInfo(){
        EntryWidgetParam result = null;

        String type = getType();
        typeSwitch:{
            if(type == null){
                typeDropDown.setError();
                //System.out.println("structure type null");
                break typeSwitch;
            }

            if(type.equals("list")){
                ListParam param = (ListParam)structureWidgetList.getParam();
                result = param;
                break typeSwitch;
            }
            if(type.equals("drop down")){
                DropDownParam param = (DropDownParam) structureWidgetDropDown.getParam();
                result = param;
                break typeSwitch;
            }

            if(type.equals("edit text")){
                EditTextParam param = (EditTextParam) structureWidgetEditText.getParam();
                result = param;
                break typeSwitch;
            }
        }
        if(headerView.nameEditor.getText() == null){
            headerView.nameEditor.setError();

            return null;
        }


        if(result != null){
            result.name = headerView.nameEditor.getText();
            result.setIsUniqueAttribute(headerView.getStarOn());
            result.widgetIdTracker = widgetIdTracker;
        }

        return result;
    }



    @Override
    public void setParam(EntryWidgetParam param){
        System.out.println("setting param: " + param);
        String type = param.className;
        headerView.nameEditor.setText(param.name);
        typeDropDown.setSelectedByPayload(type);
        widgetIdTracker = param.widgetIdTracker;
        MainActivity.log(param.name + ": " + param.isUniqueAttribute);
        setType();
        headerView.setStarOn(param.isUniqueAttribute);
        typeSwitch:{
            if(type == null)
                throw new RuntimeException();

            if(type.equals(ListWidget.className)){
                System.out.println("type is list");
                structureWidgetList.setParam(param);
                break typeSwitch;
            }
            if(type.equals(DropDown.className)){
                System.out.println("type is drop down");
                structureWidgetDropDown.setParam(param);
                break typeSwitch;
            }

            if(type.equals(CustomEditText.className)){
                System.out.println("type is edit text");
                structureWidgetEditText.setParam(param);
                break typeSwitch;
            }
            throw new RuntimeException("invalid type: " + type);
        }
    }

    @Override
    public View getView() {
        return layout.getView();
    }

    @Override
    public void setOnDataChangedListener(Runnable runnable) {
        onDataChangeListener = runnable;
    }

    public boolean hasUniqueAttribute() {
        if(headerView.getStarOn())
            return true;
        if(getType().equals(ListWidget.className)){
            return structureWidgetList.hasUniqueAttribute();
        }
        return false;
    }

    public interface StructureWidgetLayout{
        void delete(StructureWidget structureWidget);
        void moveUp(StructureWidget structureWidget);
        void moveDown(StructureWidget structureWidget);
    }
}
