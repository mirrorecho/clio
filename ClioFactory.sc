
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

	makeKwargs { arg ...args;
		var madeThisArgs = this.args.collect {|a| if (a.isKindOf(ClioFactory), {a.make}, {a})};
		var madeNewArgs = args.collect {|a| if (a.isKindOf(ClioFactory), {a.make}, {a})};
		^(madeThisArgs.asDict ++ madeNewArgs.asDict);
	}

	make { arg ...args;
		^this.makeType.new(*this.makeKwargs(*args).asPairs);
	}

	// performs a deep copy and replaces pairwise args
	mimic { arg ...args;
		var myCopy = this.deepCopy;
		myCopy.args = (this.args.asDict ++ args.asDict).asPairs;
		^myCopy;
	}

	// TO DO... remove arg pair


}


