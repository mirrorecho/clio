

ClioSoundLibrary : ClioLibrary {

	var <>soundPaths;

	go { arg ...paths;
		this.soundPaths = this.soundPaths ++ paths;
		this.soundPaths.do { arg folderString; this.addFolder(folderString); }
	}

	addFolder { arg folderString, key=[];

		ClioSound.collect(folderString).do { arg mySound;
			this.putKey(key ++ [mySound.name], mySound);
		};

		// recursively add subfolders:
		PathName(folderString).folders.do {arg folder;
			this.addFolder(folder.pathOnly, key ++ [folder.folderName.asSymbol]);
		};

	}

	bufArray { arg ...args;
		^args.collect { arg key;
			this.atKey(key).buffer;
		};
	}


/*	toBufferLibrary { arg keyPath, bufferLibrary;
		bufferLibrary = bufferLibrary ?? ClioBufferLibrary.new;

		this.leafDoFrom(keyPath, {arg key, file;
			bufferLibrary.putKey(key, SoundFile.collectIntoBuffers(file.path)[0]);
		});
		^bufferLibrary;
	}*/


	// TO DO... add playback...

	// THIS DOES'T WORK HERE AS IT DOES IN THE INTERPRETER ... WHY???
	// play {arg key;
	// 	// TO DO... this leaves buffers lying around... rethink
	// 	var myEvent = this.atKey(key).cue;
	// 	myEvent.play;
	// }


	// TO DO... add method to create new sound file

}


