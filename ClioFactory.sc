
ClioFactory {

	var <>makeType;
	var <>args; // these should be pairs
	var <>universalKeys; // these will be added to all

	*new { arg ...args;
		^super.new.initMe(*args);
	}

	initMe { arg makeType ...args;
		this.makeType = makeType;
		this.args = args;
		this.universalKeys = [];
	}

	setKwarg { arg name, value;
		var myKwargs = this.args.asDict;
		myKwargs[name] = value;
		this.args = myKwargs.asPairs;
	}

	setKwargs { arg ... args;
		this.args = (this.args.asDict ++ args.asDict).asPairs;
	}

	kwargs { arg ...args;
		var myKwargs = this.args.asDict ++ args.asDict;
		var universalArgs = myKwargs.getPairs(this.universalKeys);

		^myKwargs.collect {|a| if (a.respondsTo(\make), {a.make(*universalArgs)}, {a})};
	}

	make { arg ...args;
/*		args.postln;
		"----------------------------------".postln;*/
		^this.makeType.new( *this.kwargs(*args).asPairs );
	}

	// performs a deep copy and replaces pairwise args
	mimic { arg ...args;
		var myCopy = this.deepCopy;
		myCopy.setKwargs(*args);
		^myCopy;
	}

	postKwargs { arg ...args;
		this.kwargs(*args).pairsDo { arg n,v; (n.asString ++ ": " ++ v.asString).postln; };
	}

	removeKwargs { arg ... args;
		var myKwargs = this.args.asDict;
		args.do{|k|
			myKwargs.removeAt(k);
		};
		this.args = myKwargs.asPairs;
	}


}


