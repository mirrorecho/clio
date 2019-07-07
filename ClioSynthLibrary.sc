
// TO DO MAYBE: is this a factory? combine with SynthDefLibrary?
ClioSynthLibrary : ClioLibrary {


	// makes a ClioSynthDefFactory with function arguments, and adds to library
	makeFactory { arg key, args; // args should be an array with pairs of keys and argument arrays

		// collects functions from this synch library into a new pairwise array
		// [func, argArray, func, argArray ... etc.]
		var funcsWithArgs = {arg val, isKey;
			if(isKey, {this.at(*([\func] ++ val))}, val);
		}.flop.value(args, [true, false]);

		var mySynthDefFactory = ClioSynthDefFactory(*funcsWithArgs);

		this.put(*([\factory] ++ key ++ mySynthDefFactory));

		^mySynthDefFactory;
	}

	makeDefFromFactory {arg factoryKey, synthDefName, args=[];
		var myFactory = this.at(*([\factory] ++ factoryKey));

		var myDef = myFactory.make(synthDefName, args);

		this.put(*([\def] ++ synthDefName ++ myDef));

		^myDef;
	}


	// a shortcut to make a both factory and def, with the same sub-key
	makeDef {arg key, args=[];

		var myFactory = this.makeFactory(key, args);

		^this.makeDefFromFactory(key, key);
	}


	makeSynth { arg key, synthDefName, args=[];
		var mySynth = this.at(*([\synth] ++ key));

		if (mySynth != nil, { mySynth.free; });

		if (synthDefName == nil, {synthDefName=key.asArray.last;});

		mySynth = Synth(synthDefName, args);

		this.put(*([\synth] ++ key ++ mySynth));

		^mySynth;
	}


}


