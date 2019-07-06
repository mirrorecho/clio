
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
	makeFx { arg name, synthFactoryName, args=[], numChannels=2, callback; // NOTE: to keep things simple the same args are passed to SynthDef as to Synth

		synthFactoryName = synthFactoryName ?? name;

		{
			var fxBus = this.busses.makeBus([\fx, synthFactoryName], numChannels); // creates a new audio bus

			Clio.server.sync;

			this.synths.makeDef(synthFactoryName, [
				[\fx, \busIn]:[numChannels:numChannels, bus:fxBus],
				[\fx, name]:args,
			]).add;

			Clio.server.sync;

			this.synths.makeSynth([\func, \fx, synthFactoryName], synthFactoryName, args);

			Clio.server.sync;

			callback.value;

		}.fork;

	}

	// TO DO: copy from other library objects (may only need to be implemented in library)

	// TO DO: add any catalog from anywhere (and esp important to only copy needed keys)

	// TO DO: add a clear all

}

