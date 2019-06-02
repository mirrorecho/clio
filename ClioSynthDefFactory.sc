
ClioSynthDefFactory : ClioFactory {
	var <>defineFunction;

	initMe { arg ...args;
		this.makeType = SynthDef;
		this.defineFunction = args.pop;
		this.args = args;
	}

	make { arg name ...args; // warning these cannot contain factories, should they?

		^this.defineFunction.value(name, this.makeKwargs(*args));
	}

	// TO DO: better handle nil name
	add { arg name ...args;
		^this.make(name, *args).add;
	}

	// TO DO: better handle nil name
	play { arg name ...args;
		^this.make(name, *args).play;
	}

}



