
ClioSynthFunc : ClioFactory {
	var <>func;


	initMe { arg func ...args;
		super.initMe(Object, *args);
		this.func = func;
		this.universalKeys = [\synth];
	}

	make { arg ...args;
		// NOTE: need to explicitly set synthKwargs before calling make
		^this.func.(this.kwargs(*args));
	}


	wrap { arg args;
		SynthDef.wrap(this.func, nil, [this.kwargs(*args)]);
	}


	// // TO DO: implement this?
	// play { arg name, args;
	// 	^this.make(name, args).play;
	// }

}


