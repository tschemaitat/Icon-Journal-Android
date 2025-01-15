



```
data needs to be saved immediately
	data isn't held by the view
	view calls the save function passing:
		editData(Structure, Widget, ListId, Data)
		does the parent view class hold this code?


how is Items of a list identified in sql
	lists can look like a tree:
		item
			item1
				item2
				item3
			item4
				item5
				item6
		1 and 4 are the same widget
		2,3,5,6 are the same widget
		using indexList
			5 is id'd by 4->5 or index 2nd, 1st
			table: id, indexList, stringId, structureId, entryId, widgetId
			limit number of index to maybe 4
			performance
				easy to load all data, then sort
				moves items need to edit index of all children
		using adjacency
			5 is id'd by parent is item4, where item4 has a global id
			table: globalId, parentGlobalId, index, stringId

i could create tables for each structure
i don't think i need to

I think i would use seperate tables for items in different structures
	uncommon to use all structures when loading data, may only use a couple if depending if your structures references them
Entries of a structures use the same table
	you can reference many entries and need to load in data from many entries

table for structures
	id, name

table for widgets
	id, structureId, indexList, widgetTypeId, name

current state of program
	entry editor
		widgets store state of data, and pass all the data at once when closing the page
		need to make widgets load data, then when they change, call an edit right away
	widget hierarchy
		widgets are stored as a tree and use parents
		if anything, the widget storage stuff can stay the same for a while, maybe if i did store widget in sql, i would use the parent style


changing order of items
	i want to add a button for reording the widgets so i can forget about the advanced move for now

work on moving all ui views into the element library
this will allow to remove a lot of code bulk

work on moving features im not currently going to finish into a folder to remove bulk

this includes delete code

I also want to try to work on the idea of grouping code by function.

Like maybe when i make the delete code, the different maybe widget interface when an object that hold the functions that says what the hierarchy should do.




```
