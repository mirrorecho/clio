
ClioLibrary : Library {

	putKey { arg key, item; // key can be either a symbol or an array of symbols
		^this.put(*(key.asArray ++ [item]));
	}

	atKey { arg key; // key can be either a symbol or an array of symbols
		^this.at(*(key.asArray));
	}

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

	putFromFile { arg key = [], filePath;
		var envir = Environment.make;
		envir.use {filePath.load};
		this.putFromEnvironment(key, envir);
	}

}

