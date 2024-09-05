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
import java.util.Objects;

public class DropDown{

    public static final String className = "drop down";
    public static final String defaultHintText = "select option";
    public static final String folder = "\uD83D\uDCC1";

    private Context context;

    private DropDownPage parentPage = null;
    private DropDownPage currentPage = null;

    private String hint = defaultHintText;

    private CustomPopup customPopup;
    private SelectionView buttonSelectionView;
    private DropDownOnSelected onSelectedListener;

    private boolean setSelectedBool = false;


    //public DropDownOption selectedDropDownOption = null;
    private SelectedOption selectedDropDownOption = null;



    /*
    this saves a selected value as an array of payloadOptions representing the pages
    it can also have an initial value of a single string
        it checks if the value can be found anywhere in the pages
        it does not attached the value to a page because it could belong to many different pages
    whether the value is path or non-path is tracked by "SelectedOption selectedDropDownOption"
    if the user tries to access the payload of a value that is non-path, it will crash

     */



    public DropDown(Context context, DropDownOnSelected onSelected) {
        this.context = context;
        this.onSelectedListener = onSelected;
        init();
    }

    //region init
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
        setOptionsOfPageBasedOnCurrentPage();
        customPopup.enableBack();
        customPopup.showPopupWindow(this.getView());
    }

    private void onOutsideSelected(){
        customPopup.close();
        customPopup = null;
    }



    //endregion

    //region setting visuals
    private void setOptionsOfPageBasedOnCurrentPage(){
        String headerName = defaultHintText;
        if(currentPage != parentPage)
            headerName = currentPage.getName();
        setPopUpOptions(headerName, formatOptions(currentPage, folder));
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


    private void setPopUpOptions(String title, ArrayList<PayloadOption> spinnerOptions){
        customPopup.setText(title, spinnerOptions);
    }

    //endregion

    //region state management

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
        setOptionsOfPageBasedOnCurrentPage();
    }

    private void onBackSelected() {
        if(currentPage == parentPage){
            onDataChanged(currentPage, true);
            return;
        }
        if(currentPage.getParent() == parentPage){
            customPopup.removeBackIcon();
        }
        currentPage = currentPage.getParent();
        //on back button

        setOptionsOfPageBasedOnCurrentPage();
    }

    private void onDataChanged(DropDownPage clickedPage, boolean newValueIsNull){
        SelectedOption previousSelectedOption = selectedDropDownOption;
        //we didn't set the page clicked because its not a folder, so we have to add the name onto the end
        handleStateOnDataChanged(clickedPage, newValueIsNull);

        customPopup.close();
        customPopup = null;

        RefItemPath currentRefItemPath = null;
        CachedString prevRefItemPath = null;
        Object currentPayload = null;
        Object prevPayload = null;

        if(previousSelectedOption != null){
            prevRefItemPath = previousSelectedOption.getCachedStringFromEitherState();
            prevPayload = previousSelectedOption.payload;
        }
        if(selectedDropDownOption != null){
            currentRefItemPath = selectedDropDownOption.getPathSelected();
            currentPayload = selectedDropDownOption.payload;
        }
        if(currentRefItemPath == null){

        }
//        if( ! isDataSame(prevRefItemPath, currentRefItemPath.getArrayString())){
//            MainActivity.log("data is different: " + prevRefItemPath + ", " + currentRefItemPath);
//            onSelectedListener.onSelected(currentRefItemPath, currentPayload, prevRefItemPath, prevPayload);
//        }
        if( ! Objects.equals(prevRefItemPath, currentRefItemPath.getArrayString())){
            MainActivity.log("data is different: " + prevRefItemPath + ", " + currentRefItemPath);
            onSelectedListener.onSelected(currentRefItemPath, currentPayload, prevRefItemPath, prevPayload);
        }

    }

    private void handleStateOnDataChanged(DropDownPage clickedPage, boolean isNewValueNull){
        if(isNewValueNull){
            selectedDropDownOption = null;
            buttonSelectionView.setTextString(new ArrayList<>(Collections.singleton(new LiteralString(hint))));
        }else{
            selectedDropDownOption = new SelectedOption(clickedPage.getRefPathToPageWithName(),
                    clickedPage.getPayloadOption().getPayload());
            buttonSelectionView.setTextString(new ArrayList<>(Collections.singleton(clickedPage.getCachedName())));
        }
    }

    //endregion

    //region public
    public View getView(){
        return buttonSelectionView.getView();
    }

    public void setDropDownPage(DropDownPage dropDownPage){
        if(parentPage != null)
            throw new RuntimeException();
        this.parentPage = dropDownPage;
        currentPage = parentPage;
    }



    public void setSelectedPath(RefItemPath refItemPath){
        if(setSelectedBool)
            throw new RuntimeException("set selected shouldn't be called twice");
        setSelectedBool = true;
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

    public void setSelectedNonPath(CachedString item) {
        if(setSelectedBool)
            throw new RuntimeException("set selected shouldn't be called twice");
        setSelectedBool = true;
        if(parentPage == null)
            throw new RuntimeException("tried to set selected path with no page set");
        if(currentPage != parentPage)
            throw new RuntimeException("weird state, tried to set selected when current page isn't parent page");
        buttonSelectionView.setTextString(new ArrayList<>(Collections.singleton(item)));
        selectedDropDownOption = new SelectedOption(item);

        if( ! recursiveSearchPageForValue(parentPage, item)){
            throw new RuntimeException("tried to set non-path item to drop down, not found in pages\n" +
                    "item: " + item.getString() + "\n" +
                    "pages: \n" + parentPage.hierarchyString());
        }
    }

    private boolean recursiveSearchPageForValue(DropDownPage currentParentPage, CachedString searchValue){
        if(currentParentPage.hasChildPage(searchValue)){
            return true;
        }
        for(DropDownPage child: currentParentPage.getChildren()){
            if(recursiveSearchPageForValue(child, searchValue))
                return true;
        }
        return false;
    }

    public RefItemPath getSelectedPath(){
        if(selectedDropDownOption == null)
            return null;
        return selectedDropDownOption.getPathSelected();
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
    //endregion

    //region algorithms
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
    //endregion




    public interface DropDownOnSelected{
        void onSelected(RefItemPath refItemPath, Object payload, CachedString prevRefItemPath, Object prevPayload);
    }

    public static class DropDownOption{
        public RefItemPath refItemPath;
        public Object payload;

        public DropDownOption(RefItemPath refItemPath, Object payload) {
            this.refItemPath = refItemPath;
            this.payload = payload;
        }
    }

    public static class SelectedOption{
        private CachedString nonPathSelected;
        private RefItemPath pathSelected;
        private Object payload;

        private boolean pathSelectedBoolean;

        public SelectedOption(CachedString nonPathSelected){
            this.nonPathSelected = nonPathSelected;
            pathSelectedBoolean = false;
        }

        public SelectedOption(RefItemPath pathSelected, Object payload){
            this.pathSelected = pathSelected;
            this.payload = payload;
            pathSelectedBoolean = true;
        }

        public CachedString getCachedStringFromEitherState(){
            if(pathSelectedBoolean)
                return pathSelected.getArrayString();
            else
                return nonPathSelected;
        }

        public boolean isSelectedPath(){
            return pathSelectedBoolean;
        }

        public CachedString getNonPathSelected() {
            return nonPathSelected;
        }

        public RefItemPath getPathSelected() {
            return pathSelected;
        }

        public Object getPayload() {
            return payload;
        }
    }
}
