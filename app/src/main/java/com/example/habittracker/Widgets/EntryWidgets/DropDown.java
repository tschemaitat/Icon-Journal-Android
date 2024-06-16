package com.example.habittracker.Widgets.EntryWidgets;



import android.content.Context;
import android.view.View;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.ColorPalette;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.DropDownPage;
import com.example.habittracker.Structs.PayloadOption;
import com.example.habittracker.Structs.RefItemPath;
import com.example.habittracker.ViewWidgets.CustomPopup;
import com.example.habittracker.ViewWidgets.SelectionView;

import com.example.habittracker.defaultImportPackage.ArrayList;
import java.util.Collections;

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
    private DropDownPage previousDropDownPage = null;


    public DropDownOption selectedDropDownOption = null;
    public DropDown(Context context, DropDownOnSelected onSelected) {
        this.context = context;
        this.onSelectedListener = onSelected;
        init();
    }




    private void init(){
        createButton();
    }
    private void createButton(){
        buttonSelectionView = new SelectionView(context, new String[]{"select option"}, (stringValue, position, key) -> {
            //on button pressed
            createPopUp();
        });
        buttonSelectionView.setColor(ColorPalette.textPurple);
    }
    private void logPages(){
        MainActivity.log("parent: \n" + parentPage.hierarchyString());
        MainActivity.log("current: \n" + currentPage.hierarchyString());
    }
    //when drop down is initially pressed
    private void createPopUp(){
        customPopup = new CustomPopup(context, "", new ArrayList<>(), (cachedString, position, key) -> {
            MainActivity.log("item selected");
            logPages();
            onItemSelected(cachedString, position, key);
        }, () -> {
            MainActivity.log("back selected");
            onBackSelected();
        }, ()->{
            MainActivity.log("outside selected");
            //on nothing selected
            //dataChanged(null);
            onOutsideSelected();
        });
        setOptionsOfPage();
        customPopup.enableBack();
        customPopup.showPopupWindow(this.getView());
    }

    private void onOutsideSelected(){
        customPopup.close();
        customPopup = null;
    }


    private void setPopUpOptions(String title, ArrayList<PayloadOption> spinnerOptions){
        customPopup.setText(title, spinnerOptions);
    }
    private void setOptionsOfPage(){
        String headerName = defaultHintText;
        if(currentPage != parentPage)
            headerName = currentPage.getName();
        setPopUpOptions(headerName, formatOptions(currentPage, folder));
    }








    private void onItemSelected(CachedString cachedString, int position, Object payload){
        DropDownPage clickedPage = currentPage.getChildPage(position);
        if(currentPage == parentPage){
            if(customPopup.hasBackIcon()){
                customPopup.addBackIcon();
            }
        }
        //if the page clicked is not a folder
        if(!clickedPage.hasChildren()){
            onDataChanged(clickedPage, false);
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
        if(currentPage == parentPage){
            onDataChanged(currentPage, true);
            return;
        }
        //System.out.println("back button");
        //this never happened????
//        if(currentPage == parentPage){
//            currentPage = currentPage.getParent();
//            onDataChanged(null, true);
//            return;
//        }
        if(currentPage.getParent() == parentPage){
            customPopup.removeBackIcon();
        }
        currentPage = currentPage.getParent();
        //on back button

        setOptionsOfPage();
    }
    private void onDataChanged(DropDownPage clickedPage, boolean newValueIsNull){
        DropDownOption previousSelectedOption = selectedDropDownOption;
        //we didn't set the page clicked because its not a folder, so we have to add the name onto the end
        handleStateOnDataChanged(clickedPage, newValueIsNull);

        customPopup.close();
        customPopup = null;

        RefItemPath currentRefItemPath = null;
        RefItemPath prevRefItemPath = null;
        Object currentPayload = null;
        Object prevPayload = null;

        if(previousSelectedOption != null){
            prevRefItemPath = previousSelectedOption.refItemPath;
            prevPayload = previousSelectedOption.payload;
        }
        if(selectedDropDownOption != null){
            currentRefItemPath = selectedDropDownOption.refItemPath;
            currentPayload = selectedDropDownOption.payload;
        }
        if(currentRefItemPath == null){

        }
        if( ! isDataSame(prevRefItemPath, currentRefItemPath)){
            MainActivity.log("data is different: " + prevRefItemPath + ", " + currentRefItemPath);
            onSelectedListener.onSelected(currentRefItemPath, currentPayload, prevRefItemPath, prevPayload);
        }

    }

    private boolean isDataSame(RefItemPath prevPath, RefItemPath newPath){
        if(prevPath == null){
            if(newPath == null)
                return true;
            else
                return false;
        }else{
            if(newPath == null)
                return false;
            return prevPath.equals(newPath);
        }
    }

    private void handleStateOnDataChanged(DropDownPage clickedPage, boolean isValueNull){
        if(isValueNull){
            selectedDropDownOption = null;
            buttonSelectionView.setTextString(new ArrayList<>(Collections.singleton(new LiteralString(hint))));
        }else{
            selectedDropDownOption = new DropDownOption(clickedPage.getRefPathToPageWithName(),
                    clickedPage.getPayloadOption().getPayload());
            buttonSelectionView.setTextString(new ArrayList<>(Collections.singleton(clickedPage.getCachedName())));
        }
    }
    private ArrayList<PayloadOption> formatOptions(DropDownPage page, String folderString){
        if(page != parentPage && ! page.hasChildren()){
            MainActivity.log("parent: \n" + parentPage.hierarchyString() + "\nthis page: \n" + page.hierarchyString());
            throw new RuntimeException();
        }

        ArrayList<PayloadOption> payloadOptions = page.getOptions();
        ArrayList<PayloadOption> result = new ArrayList<>();

        ArrayList<DropDownPage> children = page.getChildren();
        for(int i = 0; i < children.size(); i++){
            DropDownPage childPage = children.get(i);
            Object payload = payloadOptions.get(i).getPayload();
            if(!childPage.hasChildren()){
                result.add(new PayloadOption(childPage.getCachedName(), payload));
                continue;
            }
            result.add(new PayloadOption(new LiteralString(folderString+" " + childPage.getName()), payload));
        }

        //System.out.println("formatted page: \n\t" + result);
        return result;
    }

    public View getView(){
        return buttonSelectionView.getView();
    }
    public void setDropDownPage(DropDownPage dropDownPage){
        this.parentPage = dropDownPage;
        currentPage = parentPage;
    }

    public void setSelected(RefItemPath refItemPath){
        if(setSelected)
            throw new RuntimeException("set selected shouldn't be called twice");
        setSelected = true;
        if(parentPage == null)
            throw new RuntimeException("tried to set selected path with no page set");
        if(currentPage != parentPage)
            throw new RuntimeException("weird state, tried to set selected when current page isn't parent page");
        try{
            for(int i = 0; i < refItemPath.size() - 1; i++){
                CachedString pageName = refItemPath.get(i);
                DropDownPage newPage = currentPage.getChildPage(pageName);
                currentPage = newPage;
            }
            DropDownPage lastPage = currentPage.getChildPage(refItemPath.getLast());
            handleStateOnDataChanged(lastPage, false);
        }catch(Exception exception){
            MainActivity.log("tried to set: " + refItemPath);
            MainActivity.log("page: " + parentPage.hierarchyString());
            MainActivity.log("throwing exception");
            throw exception;
        }

    }
    public RefItemPath getSelectedPath(){
        if(selectedDropDownOption == null)
            return null;
        return selectedDropDownOption.refItemPath;
    }

    public Object getPayload(){
        if(selectedDropDownOption == null)
            return null;
        return selectedDropDownOption.payload;
    }
    public void resetValue(){
        selectedDropDownOption = null;
        currentPage = parentPage;
    }
    public void setHint(String select_type) {
        System.out.println("new hint: " + select_type);
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

        if(page == null)
            throw new RuntimeException("payload: " + payload + ", pages: \n" + parentPage.hierarchyString());
        CachedString cachedString = page.getCachedName();
        if(page.parent == null){
            MainActivity.log("page parent is null: \n" + page.hierarchyString());
            throw new RuntimeException();
        }
        currentPage = page.parent;
        handleStateOnDataChanged(page, false);

    }

    private DropDownPage searchTree(DropDownPage page, Object payload){
        if( ! page.hasChildren()){
            if(page.getPayloadOption().getPayload().equals(payload)){
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
        void onSelected(RefItemPath refItemPath, Object payload, RefItemPath prevRefItemPath, Object prevPayload);
    }

    public static class DropDownOption{
        public RefItemPath refItemPath;
        public Object payload;

        public DropDownOption(RefItemPath refItemPath, Object payload) {
            this.refItemPath = refItemPath;
            this.payload = payload;
        }
    }
}
