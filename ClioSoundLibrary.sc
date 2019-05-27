
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
			this.putKey(key ++ [soundSymbol], file);
		};


		// recursively add subfolders:
		PathName(folderString).folders.do {arg folder;
			this.addFolder(folder.pathOnly, key ++ [folder.folderName.asSymbol]);
		};

	}

	toBufferLibrary { arg keyPath, bufferLibrary;
		bufferLibrary = bufferLibrary ?? ClioBufferLibrary.new;

		this.leafDoFrom(keyPath, {arg key, file;
			bufferLibrary.putKey(key, SoundFile.collectIntoBuffers(file.path)[0]);
		});
		^bufferLibrary;
	}


	// TO DO... add playback...

	// THIS DOES'T WORK HERE AS IT DOES IN THE INTERPRETER ... WHY???
	// play {arg key;
	// 	// TO DO... this leaves buffers lying around... rethink
	// 	var myEvent = this.atKey(key).cue;
	// 	myEvent.play;
	// }


	// TO DO... add method to create new sound file

}


