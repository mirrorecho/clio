
ClioSound {
	var <>name,
	<>path,
	<length,
/*	<numChannels,
	<sampleRate,
	<>freq,*/

	myBuffer,
	<soundFile;

	*new { arg path, file;
		^super.new.initMe(path, file);
	}


	*collect { arg path;
		^SoundFile.collect(path ++ "*").collect { arg file;
			ClioSound(file:file);
		};

	}

	soundFile_ { arg file;
		soundFile = file;
		this.name = soundFile.path.basename.splitext[0].asSymbol;
		this.path = soundFile.path;
	}

	initMe { arg path, file;
		this.soundFile = file ?? SoundFile(path);
	}

	clip {
		// TO DO: implement
	}

	buffer {
		^myBuffer ?? { this.load; };
	}

	numFrames { ^this.buffer.numFrames }

	numChannels { ^this.buffer.numChannels }

	sampleRate { ^this.buffer.sampleRate }

	play { ^this.buffer.play }

	freq {}

	//
	// loads mySoundFile into buffer
	load {
		// this.path.postln;
		if (myBuffer != nil, { myBuffer.free; });
		myBuffer = SoundFile.collectIntoBuffers(this.path)[0];
		^myBuffer;
	}

	alloc {
	}

	// initMe { arg myName, path;
	// 	name = myName;
	// }

}


/*	initMe { arg name, path;
		this.name = name;
		this.path = path;
		this.docs = ClioLibrary.new;
		this.synthDefs = ClioLibrary.new;
		this.synths = ClioSynthLibrary.new;
		this.sounds = ClioSoundLibrary.new;
		this.buffers = ClioBufferLibrary.new;
		this.patterns = ClioPatternLibrary.new;
	}

	// TO DO: implement ability to only copy certain keys
	addLocalCatalog{ arg librarySymbol, catalogName, key=[];
		^Message(this, librarySymbol).value.putFromCatalog(key, this.path ++ catalogName.asString ++ ".sc");
	}*/
