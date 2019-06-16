
ClioLibrary : Library {

	var <>catalogPaths;

	*new { arg ...paths;
		var myLibrary = super.new;
		myLibrary.initMe;
		^myLibrary.go(*paths);
	}

	*catalog { arg catalogFunction, isMainFunction={}, isNotMainFunction={};
		var catalogEnvir = Environment.make;
		catalogEnvir.use {catalogFunction.value};

		if (currentEnvironment === topEnvironment,
			{ isMainFunction.value(catalogEnvir) },
			{ isNotMainFunction.value(catalogEnvir) }
		);
		^catalogEnvir;
	}

	initMe {
		// just a hook for any init stuff
	}

	go { arg ...paths;
		this.catalogPaths = this.catalogPaths ++ paths;
		this.catalogPaths.do { arg catalogPath; this.putFromCatalog([], catalogPath); }
	}

	// TO DO... is this really needed????
	putKey { arg key, item; // key can be either a symbol or an array of symbols
		^this.put(*(key.asArray ++ [item]));
	}

	// TO DO... is this really needed????
	atKey { arg key; // key can be either a symbol or an array of symbols
		^this.at(*(key.asArray));

	}
	// TO DO... is this really needed????
	removeAtKey { arg key; // key can be either a symbol or an array of symbols
		^this.removeAt(*(key.asArray));
	}

	// TO DO: consider renaming?
	removeFree { arg key;
		// removes item from library and frees it (for libraries of synths, busses, etc.)

		var myItem = this.atKey(key);

		if (myItem != nil, { myItem.free; });

		this.removeAtKey(key);
	}

	putFromEnvironment { arg key = [], envir; //, maxDepth=9; // maxDepth to prevent infinite recursion of environments refer to one another
		envir.collect { arg envirValue, envirKey;
			// TO DO MAYBE: if item is a kind of environment, then recursively add (with maxDepth);
			this.putKey(key.asArray ++ [envirKey], envirValue);
		};
	}

	putFromCatalog { arg key = [], catalogPath;
		var loadingEnvir = Environment.make, catalogEnvir;
		catalogEnvir = loadingEnvir.use {catalogPath.load};
		this.putFromEnvironment(key, catalogEnvir);
	}

	// TO MAYBE: put factories?

}

