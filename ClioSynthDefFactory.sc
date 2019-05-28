
ClioSynthDefFactory : ClioFactory {
	var <>defineFunction;

	initMe { arg ...args;
		this.makeType = SynthDef;
		this.defineFunction = args.pop;
		this.args = args;
	}

	make { arg name ...args; // warning these cannot contain factories, should they?

		var madeThisArgs = this.args.collect {|a| if (a.isKindOf(ClioFactory), {a.make}, {a})};
		var madeMakeArgs = args.collect {|a| if (a.isKindOf(ClioFactory), {a.make}, {a})};
		var kwargs = (madeThisArgs.asDict ++ madeMakeArgs.asDict);
		kwargs.know = true;

		^this.defineFunction.value(name, kwargs);
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



