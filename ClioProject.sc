
// TO DO: make this NOT a library
ClioProject {
	var <>name, <>path, <>docs, <>synthDefs, <>synths, <>sounds, <>samplers, <>patterns, <>tempo;


	*new { arg name, path;
	 	^super.new.initMe(name, path);
	}

	initMe { arg name, path;
		this.name = name;
		this.path = path;
		this.docs = ClioLibrary.new;
		this.synthDefs = ClioLibrary.new;
		this.synths = ClioSynthLibrary.new;
		this.sounds = ClioSoundLibrary.new;
		this.samplers = ClioLibrary.new;
		this.patterns = ClioPatternLibrary.new;
	}

	// TO DO: implement ability to only copy certain keys
	addLocalCatalog{ arg librarySymbol, catalogName, key=[];
		^Message(this, librarySymbol).value.putFromCatalog(key, this.path ++ catalogName.asString ++ ".sc");
	}

	// TO DO: copy from other library objects (may only need to be implemented in library)

	// TO DO: add any catalog from anywhere (and esp important to only copy needed keys)

	// TO DO: add a clear all

}

