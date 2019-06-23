
// TO DO MAYBE: is this a factory? combine with SynthDefLibrary?
ClioSynthLibrary : ClioLibrary {

	initMe {
		this.put(\func, \out, \default, {arg kwargs; Out.ar(kwargs[\out], kwargs[\sig]);});
	}


	// makes a ClioSynthDefFactory with function arguments, and adds to library
	makeFactory { arg key, sig, out=\default, args=[];

		var sigFuncs = sig.collect { arg sigKey;
			this.at(*([\func, \sig] ++ sigKey));
		};

		var outFunc = this.at(*([\func, \out] ++ out));

		var mySynthDefFactory = ClioSynthDefFactory(sigFuncs, outFunc, *args);

		this.put(*([\factory] ++ key ++ mySynthDefFactory));

		^mySynthDefFactory;
	}

	makeDefFromFactory {arg factoryKey, synthDefName, args=[];
		var myFactory = this.at(*([\factory] ++ factoryKey));

		var myDef = myFactory.make(synthDefName, *args);

		this.put(*([\def] ++ synthDefName ++ myDef));

		^myDef;
	}


	// a shortcut to make a both factory and def, with the same sub-key
	makeDef {arg key, sig, out=\default, args=[];

		var myFactory = this.makeFactory(key, sig, out, args);

		^this.makeDefFromFactory(key, key);
	}


	makeSynth { arg key, synthDefName, args=[];
		var mySynth = this.at(*([\synth] ++ synthDefName));

		if (mySynth != nil, { mySynth.free; });

		if (synthDefName == nil, {synthDefName=key.asArray.last;});

		mySynth = Synth(synthDefName, args);

		this.put(*(key.asArray ++ mySynth));

		^mySynth;
	}


}

