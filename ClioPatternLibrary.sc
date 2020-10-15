
ClioPatternLibrary : ClioLibrary {
	var <>tempoClock;

	play { arg key;
		^this.at(*key).play(clock:this.tempoClock);
	}

}



// TREE STRUCTURE
// - for any given node, can get:
// - - - a base function that returns a ClioPatternFactory
// - - - a mod function that (can) modify this ClioPatternFactory (hook)
// - - - a set of kwargs passed to these two functions
// - child nodes develop the parent node by:
// - - - can completely replace the base function
// - - - can add a mod function (these are applied recursively down the tree)
// - - - can add/replace kwargs (these are combined down the tree first before using in the above functions)

ClioBuildPatterns : ClioPatternLibrary {

	initMe {
		this.putFunc({arg self, kwargs;
			// TO DO: use kwargs here ...
			ClioCell.fromEvents((note:\rest, dur:1));
		});
		this.putModFunc({arg self, pattern, kwargs;
			pattern;
		});
		this.putKwargs(IdentityDictionary());
	}

	putKwargs {arg kwargs ... args;
		this.put(*(args++[\_kwargs, kwargs]));
	}
	putFunc {arg func ... args;
		this.put(*(args++[\_func, func]))
	}
	putModFunc {arg modFunc ... args;
		this.put(*(args++[\_modFunc, modFunc]))
	}

	getKwargs {arg ... args;
		^this.at(*(args++[\_kwargs]));
	}
	getTreeKwargs {arg ... args;
		// not terribly elegant, but this works...
		var myKwargs = this.getKwargs;
		var myKey = [];
		args.do{|a|
			myKey = myKey++[a];
			myKwargs = myKwargs ++ (
				this.getKwargs(*myKey) ?? ()
			)
		};
		^myKwargs;
	}
	getFunc {arg ... args;
		^this.at(*(args++[\_func]));
	}
	getTreeFunc {arg ... args;
		// even less elegant than getTreeKwargs, but again, works...
		var myFunc = this.getFunc;
		var myKey = [];
		args.do{|a|
			// var myBranchFunc;
			myKey = myKey++[a];
			// TO DO... does this work? ...
			myFunc = this.getFunc(*myKey) ?? {myFunc};
/*			myBranchFunc = this.getFunc(*myKey);
			if (not(myBranchFunc.isNil), {
				myFunc = myBranchFunc;
			});*/
		};
		^myFunc;
	}
	getModFunc {arg ... args;
		^this.at(*(args++[\_modFunc]));
	}
	getP {arg ... args;
		var myKwargs = this.getTreeKwargs(*args);
		var myFunc = this.getTreeFunc(*args);
		var myPattern = myFunc.value(this, myKwargs);
		var myKey = [];
		// TO DO MAYBE... process myPattern with default modFunc?
		args.do{|a|
			var myModFunc;
			myKey = myKey++[a];
			myModFunc = this.getModFunc(*myKey);
			if (not(myModFunc.isNil), {
				myPattern = myModFunc.value(this, myPattern, myKwargs);
			});
		};
		^myPattern;
	}

	formPar {} // TO IMPLEMENT
	parForm {} // TO IMPLEMENT

}