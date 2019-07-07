
ClioLibrary : Library {

	var <>catalogPaths;

	*new { arg ...paths;
		var myLibrary = super.new;
		myLibrary.initMe;
		^myLibrary.go(*paths);
	}

	*catalog { arg key, catalogFunction, isMainFunction={}, isNotMainFunction={};
		var catalogEnvir = Environment.make, myCatalog;
		catalogEnvir.use {catalogFunction.value};
		myCatalog = [key, catalogEnvir];

		if (currentEnvironment === topEnvironment,
			{ isMainFunction.value(*myCatalog) },
			{ isNotMainFunction.value(*myCatalog) }
		);
		^myCatalog;
	}

	initMe {
		// just a hook for any init stuff
	}

	go { arg ...paths;
		this.catalogPaths = this.catalogPaths ++ paths;
		this.catalogPaths.do { arg catalogPath; this.putFromCatalog([], catalogPath); }
	}


	// TO DO: consider renaming?
	removeFree { arg key;
		// removes item from library and frees it (for libraries of synths, busses, etc.)

		var myItem = this.at(*key);

		if (myItem != nil, { myItem.free; });

		^this.removeAt(*key);
	}

	putFromEnvironment { arg key, envir; //, maxDepth=9; // maxDepth to prevent infinite recursion of environments refer to one another
		envir.collect { arg envirValue, envirKey;
			// TO DO MAYBE: if item is a kind of environment, then recursively add (with maxDepth);
			this.put(*(key.asArray ++ envirKey ++ envirValue));
		};
	}


	// TO DO: consider only loading some envir variables
	putFromCatalog { arg catalogPath;
		var loadingEnvir = Environment.make, myCatalog;
		myCatalog = loadingEnvir.use {catalogPath.load};
		this.putFromEnvironment(*myCatalog);
	}


	// TO MAYBE: put factories?

}

