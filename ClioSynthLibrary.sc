
// TO DO MAYBE: is this a factory? combine with SynthDefLibrary?
ClioSynthLibrary : ClioLibrary {

	makeSynth { arg key, synthDefName, args=[];
		var mySynth = this.atKey(key);

		if (mySynth != nil, { mySynth.free; });

		if (synthDefName == nil, {synthDefName=key.asArray.last;});

		mySynth = Synth(synthDefName, args);

		this.putKey(key, mySynth);

		^mySynth;

	}


}


