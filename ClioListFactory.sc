
ClioL : ClioFactory {

	make { arg ...args;
		var myArgs = args ++ this.args;
		^myArgs.collect {|a| if (a.isKindOf(ClioFactory), {a.make}, {a})};
	}

	initMe { arg ...args;
		this.args = args;
	}

}



