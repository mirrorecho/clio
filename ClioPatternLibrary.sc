
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
			ClioCell.fromArgs(*kwargs.asPairs);
		});
		this.putModFunc({arg self, pattern, kwargs;
			pattern;
		});
		this.putKwargs([streamKeys:[\note, \dur, \amp],].asDict);
	}

	putKwargs {arg kwargs=IdentityDictionary() ... args;
		this.put(*(args++[\_kwargs, kwargs]));
	}
	addKwargs {arg kwargs ... args;
		// NOTE: we're NOT calling getKwargs here,
		// since we don't want to get wildcard '*' kwargs
		var existingKwargs = this.at(*(args++[\_kwargs])) ?? ();
		this.putKwargs(existingKwargs++kwargs, *args);
	}
	putFunc {arg func ... args;
		this.put(*(args++[\_func, func]));
	}
	putModFunc {arg modFunc ... args;
		this.put(*(args++[\_modFunc, modFunc]));
	}

	wildAt {arg subKey ... args;
		var wildKey = ['*'] ++ args[args.size-1..] ++ [subKey];
		^this.at(*wildKey);
		// ^nil;
	}

	// NOTE, this will always return a dict of some kind
	getKwargs {arg ... args;
		^this.wildAt(\_kwargs, *args) ++ (
			this.at(*(args++[\_kwargs])) ?? ()
		);
	}
	getTreeKwargs {arg ... args;
		// not terribly elegant, but this works...
		var myKwargs = this.getKwargs;
		var myKey = [];
		args.do{|a|
			myKey = myKey++[a];
			myKwargs = myKwargs ++ this.getKwargs(*myKey) ?? ();
		};
		^myKwargs;
	}
	getFunc {arg ... args;
		^this.at(*(args++[\_func])) ?? {this.wildAt(\_func, *args)};
	}
	getTreeFunc {arg ... args;
		// even less elegant than getTreeKwargs, but again, works...
		var myFunc = this.getFunc;
		var myKey = [];
		args.do{|a|
			myKey = myKey++[a];
			myFunc = this.getFunc(*myKey) ?? {myFunc};
		};
		^myFunc;
	}
	getModFunc {arg ... args;
		^this.at(*(args++[\_modFunc])) ?? {this.wildAt(\_modFunc, *args)};
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

	placeAt {arg key ... args;

		args.pairsDo{|k,v|
			if (k.isKindOf(Symbol), {
				if (k==\_func, {this.putFunc(v,*key.asArray)}, {
					if (k==\_modFunc, {this.putModFunc(v,*key.asArray)}, {
						this.addKwargs([k,v].asDict, *key.asArray);
					});
				});
			},
			{
				if (k.isString, {k=k.asSymbol;});
				this.placeAt(key++k.asArray, *v);
			}
			);
		};
	}

	place {arg ... args;
		this.placeAt([], *args);
	}

	formPar {arg ... args;
		^ClioPseq(args.collect{|f|
			ClioPpar(f[1].clump(2).collect{|p|
				var myKey = [f[0], p[0]]++p[1].asArray;
				myKey.postln;
				this.getP(*myKey);
			}.asArray);
		});
	}

	parForm {arg ... args;}

}