
ClioSynthLibrary : ClioLibrary {

	makeSynth { arg name, synthDefName, args=[];
		var mySynth = this.at(name);

		if (mySynth != nil, { mySynth.free; });

		if (synthDefName == nil, {synthDefName=name;});

		mySynth = Synth(synthDefName, args);

		this.put(name, mySynth);

		^mySynth;

	}


}

