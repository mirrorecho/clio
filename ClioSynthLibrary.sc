
// TO DO MAYBE: is this a factory? combine with SynthDefLibrary?
ClioSynthLibrary : ClioLibrary {


	// makes a ClioSynthDefFactory with function arguments, and adds to library
	makeFactory { arg key, args=[] ...sigKeys;

		var sigFuncs = sigKeys.collect { arg sigKey;
			this.at(*([\func] ++ sigKey));
		};

		var mySynthDefFactory = ClioSynthDefFactory(args, *sigFuncs);

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
	makeDef {arg key, args=[] ...sigKeys;

		var myFactory = this.makeFactory(key, args, *sigKeys);

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

