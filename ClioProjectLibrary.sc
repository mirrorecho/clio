
// TO DO: make this NOT a library
ClioProjectLibrary : ClioLibrary {

	*getDefaultSettings {
		^Environment.make {
			~name = "A Clio Story";
			~path = "";

			~docs = ClioLibrary.new;
			~synthdefs = ClioSynthLibrary.new;
			~synths = ClioLibrary.new;
			~sounds = ClioSoundLibrary.new;
			~buffers = ClioBufferLibrary.new;
			~patterns = ClioLibrary.new;
		};
	}

	go {
		this.putFromEnvironment([], ClioProjectLibrary.getDefaultSettings);
	}

	// *new { arg ...paths;
	// 	^super.new.initMe(*paths);
	// }


}

