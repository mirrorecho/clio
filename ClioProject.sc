
// TO DO: make this NOT a library
ClioProject {
	var <>name, <>path, <>synths, <>busses, <>sounds, <>midi, <>patterns, <>tempo;


	*new { arg name, path;
	 	^super.new.initMe(name, path);
	}

	initMe { arg name, path;
		this.name = name;
		this.path = path;
		// this.docs = ClioLibrary.new;
		// this.synthDefs = ClioLibrary.new;
		this.synths = ClioSynthLibrary.new;
		this.busses = ClioBusLibrary.new;
		this.sounds = ClioSoundLibrary.new;
		this.midi = ClioMIDILibrary.new; // TO DO: consider whether this should really be project speicifc, or universal to CLio
		this.patterns = ClioPatternLibrary.new;
	}

	// TO DO: implement ability to only copy certain keys
	addLocalCatalog{ arg librarySymbol, catalogName, key=[];
		^Message(this, librarySymbol).value.putFromCatalog(key, this.path ++ catalogName.asString ++ ".sc");
	}


	// this lives at the project level because it needs a new bus, a synthdef, and a synth
	makeFx { arg name, args=[], numChannels=2, callback;

		{
			var fxBus = this.busses.makeBus([\fx, name], numChannels); // creates a new audio bus

			Clio.server.sync;

			this.synths.makeDef(name, [
				[\fx, \busIn]:[numChannels:numChannels, bus:fxBus],
			] ++ args).add;

			Clio.server.sync;

			this.synths.makeSynth([\fx, name]);

			Clio.server.sync;

			callback.value;

		}.fork;

	}

	// TO DO: copy from other library objects (may only need to be implemented in library)

	// TO DO: add any catalog from anywhere (and esp important to only copy needed keys)

	// TO DO: add a clear all

}

