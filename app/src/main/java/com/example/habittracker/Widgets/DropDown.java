package com.example.habittracker.Widgets;



import android.content.Context;
import android.view.View;

import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.StaticClasses.EnumLoop;
import com.example.habittracker.Structs.CachedString;
import com.example.habittracker.Structs.DropDownPages.DropDownPage;
import com.example.habittracker.Structs.ItemPath;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.ViewWidgets.CustomPopup;
import com.example.habittracker.ViewWidgets.SelectionView;

import java.util.ArrayList;

public class DropDown{

    public static final String className = "drop down";
    public static final String defaultHintText = "select option";
    private Context context;



    private String folder = "\uD83D\uDCC1";

    private DropDownPage parentPage = null;
    private DropDownPage currentPage = null;

    public String hint = defaultHintText;

    private CustomPopup customPopup;

    private SelectionView buttonSelectionView;

    private DropDownOnSelected onSelectedListener;

    boolean setSelected = false;


    public  ItemPath selectedValuePath = null;
    public DropDown(Context context, DropDownOnSelected onSelected) {
        this.context = context;
        this.onSelectedListener = onSelected;
        init();
    }




    private void init(){
        selectedValuePath = null;
        createButton();
    }
    private void createButton(){
        buttonSelectionView = new SelectionView(context, new String[]{"select option"}, (stringValue, position, key) -> {
            //on button pressed
            createPopUp();
        });
        buttonSelectionView.setColor(ColorPalette.textPurple);
    }
    //when drop down is initially pressed
    private void createPopUp(){
        customPopup = new CustomPopup(context, "", new ArrayList<>(), (stringValue, position, key) -> {
            onItemSelected(position, key);
        }, () -> {
            onBackSelected();
        }, ()->{
            //on nothing selected
            //dataChanged(null);
            customPopup.close();
            customPopup = null;
        });
        setOptionsOfPage();
        customPopup.enableBack();
        customPopup.showPopupWindow(this.getView());
    }


    private void setPopUpOptions(String title, ArrayList<PayloadOption> spinnerOptions){
        customPopup.setText(title, spinnerOptions);
    }
    private void setOptionsOfPage(){
        setPopUpOptions(currentPage.getName(), formatOptions(currentPage, folder));
    }








    private void onItemSelected(int position, Object payload){
        DropDownPage clickedPage = currentPage.getChildPage(position);
        //if the page clicked is not a folder
        if(!clickedPage.hasChildren()){
            onDataChanged(clickedPage.getName(), payload);
            return;
        }
        //if we are going into a new page
        if(currentPage == parentPage){//if we are leaving parent page
            customPopup.addBackIcon();
        }
        currentPage = clickedPage;
        setOptionsOfPage();
    }
    private void onBackSelected() {
        //System.out.println("back button");
        if(currentPage == parentPage){
            onDataChanged(null, null);
            return;
        }
        if(currentPage.getParent() == parentPage){
            customPopup.removeBackIcon();
        }
        //on back button
        currentPage = currentPage.getParent();
        setOptionsOfPage();
    }
    private void onDataChanged(String newValue, Object payload){
        //we didn't set the page clicked because its not a folder, so we have to add the name onto the end
        handleStateOnDataChanged(newValue);

        customPopup.close();
        customPopup = null;
        onSelectedListener.onSelected(selectedValuePath, payload);
    }

    private void handleStateOnDataChanged(String newValue){
        ItemPath newPath = currentPage.getPathToPageWithName();
        selectedValuePath = newPath.createNewPathAdd(newValue);
        if(newValue == null)
            buttonSelectionView.setText(new String[]{hint});
        else
            buttonSelectionView.setText(new String[]{newValue});
    }

    private static ArrayList<PayloadOption> formatOptions(DropDownPage page, String folderString){
        ArrayList<PayloadOption> payloadOptions = page.getOptions();
        ArrayList<PayloadOption> result = new ArrayList<>();
        if(!page.hasChildren())
            throw new RuntimeException("page doesn't have children");
        ArrayList<DropDownPage> children = page.getChildren();
        for(int i = 0; i < children.size(); i++){
            DropDownPage childPage = children.get(i);
            Object payload = payloadOptions.get(i);
            if(!childPage.hasChildren()){
                result.add(new PayloadOption(childPage.getCachedName(), payload));
                continue;
            }
            result.add(new PayloadOption(new CachedString(folderString+" " + childPage.getName()), payload));
        }

        //System.out.println("formatted page: \n\t" + result);
        if(result.size() == 0)
            throw new RuntimeException("result doesn't have any values");
        return result;
    }

    public View getView(){
        return buttonSelectionView.getView();
    }
    public void setDropDownPage(DropDownPage dropDownPage){
        this.parentPage = dropDownPage;
        currentPage = parentPage;
    }

    public void setSelected(ItemPath itemPath){
        if(setSelected)
            throw new RuntimeException("set selected shouldn't be called twice");
        setSelected = true;
        if(parentPage == null)
            throw new RuntimeException("tried to set selected path with no page set");
        if(currentPage != parentPage)
            throw new RuntimeException("weird state, tried to set selected when current page isn't parent page");
        EnumLoop.loop(itemPath.getStringPath(), (index, pageName)->{
            DropDownPage newPage = currentPage.getChildPage(pageName);
            currentPage = newPage;
        });
        handleStateOnDataChanged(itemPath.getName());
    }
    public ItemPath getSelectedPath(){
        return selectedValuePath;
    }
    public void resetValue(){
        selectedValuePath = null;
        currentPage = parentPage;
    }
    public void setHint(String select_type) {
        hint = select_type;
        buttonSelectionView.setText(new String[]{select_type});
    }
    public void setError() {
        System.out.println("<drop down> setting error");
        buttonSelectionView.setColor(ColorPalette.redText);
    }
    public void resetError() {
        buttonSelectionView.setColor(ColorPalette.textPurple);
    }

    public void setByPayload(Object payload) {
        DropDownPage page = searchTree(parentPage, payload);
        CachedString cachedString = page.getCachedName();
        if(page == null)
            throw new RuntimeException("payload: " + payload + "pages: \n" + parentPage.hierarchyString());
        currentPage = page.parent;
        handleStateOnDataChanged(cachedString.getString());

    }

    public DropDownPage searchTree(DropDownPage page, Object payload){
        if( ! page.hasChildren()){
            if(page.getPayloadOption().equals(payload)){
                return page;
            }else{
                return null;
            }
        }
        for(DropDownPage child: page.getChildren()){
            DropDownPage result = searchTree(child, payload);
            if(result != null)
                return result;
        }
        return null;
    }

    public interface DropDownOnSelected{
        void onSelected(ItemPath itemPath, Object payload);
    }
}
