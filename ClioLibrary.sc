
ClioLibrary : Library {

	// TO DO: consider renaming?
	removeFree { arg name;
		// removes item from library and frees it (for libraries of synths, busses, etc.)

		var myItem = this.at(name);

		if (myItem != nil, { myItem.free; });

		this.removeAt(name);
	}

}
