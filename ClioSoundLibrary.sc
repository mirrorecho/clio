
ClioSoundLibrary : ClioLibrary {

	var <>paths;

	*new { arg ...paths;
		^super.new.initMe(*paths);
	}

	initMe { arg ...paths;
		this.paths = paths.asSet;
		this.paths.do { arg pathString; this.addFolder(pathString); }
	}

	addFolder { arg folderString, key=[];

		SoundFile.collect(folderString ++ "*").do { arg file;
			var soundSymbol = file.path.basename.splitext[0].asSymbol;
			this.put(*(key ++ [soundSymbol, file]));
		};


		// recursively add subfolders:
		PathName(folderString).folders.do {arg folder;
			this.addFolder(folder.pathOnly, key ++ [folder.folderName.asSymbol]);
		};

	}


	// TO DO... add method to collect into buffer library

	// TO DO... add playback

}



