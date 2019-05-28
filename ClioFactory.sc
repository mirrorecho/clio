
ClioFactory {

	var <>makeType;
	var <>args;

	*new { arg ...args;
		^super.new.initMe(*args);
	}

	initMe { arg makeType ...args;
		this.makeType = makeType;
		this.args = args;
	}

	make {
		var madeArgs = this.args.collect {|a| if (a.isKindOf(ClioFactory), {a.make}, {a})};
		^this.makeType.new(*madeArgs);
	}

	// performs a deep copy and replaces pairwise args
	mimic { arg ...args;
		var myCopy = this.deepCopy;
		myCopy.args = (this.args.asDict ++ args.asDict).asPairs;
		^myCopy;
	}

	// TO DO... remove arg pair


}


