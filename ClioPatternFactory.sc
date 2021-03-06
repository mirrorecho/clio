
ClioPatternFactory : ClioFactory {

	// TO DO MAYBE: move to ClioFactory for general use?
	play { arg clock;
		^this.make.play(clock:clock);
	}

	asStream {
		^this.make.asStream;
	}

	embedInStream {
		^this.make.embedInStream;
	}

}
// -------------------------------------------------

// TO DO: simpler default of makeType without having to override initMe and call super every time

ClioPbind : ClioPatternFactory {

	initMe { arg ...args;
		super.initMe(Pbind, *args);
	}

	patternpairs {
		^this.make.patternpairs
	}

}

// for some odd reason, Rest() gets screwy within patterns... so creating this alternative
ClioPrest : ClioPbind {
	initMe { arg dur;
		super.initMe(*[note:Pn(\rest, 1), dur:dur]);
	}

}

// -------------------------------------------------
// TO DO, this Base is not so elegant:

ClioPmonoBase : ClioPatternFactory {
	var <>instrument;

	make { arg ...args;
		^this.makeType.new(this.instrument, *this.kwargs(*args).asPairs );
	}
}


ClioPmono : ClioPmonoBase {

	initMe { arg instrument ...args;
		this.instrument = instrument;
		super.initMe(Pmono, *args);
	}

}

ClioPmonoArtic : ClioPmonoBase {

	initMe { arg instrument ...args;
		this.instrument = instrument;
		super.initMe(PmonoArtic, *args);
	}

}

// -------------------------------------------------
// TO DO, this Base is not so elegant:

ClioPseqBase  : ClioPatternFactory {

	make {
		var madeList = this.args[0].collect {|a| if (a.isKindOf(ClioFactory), {a.make}, {a})};
		^this.makeType.new(madeList, *this.args[1..]);
	}

}

ClioPseq : ClioPseqBase {

	initMe { arg ...args;
		super.initMe(Pseq, *args);
	}

}

ClioPpar : ClioPseqBase {

	initMe { arg ...args;
		super.initMe(Ppar, *args);
	}


}

// -------------------------------------------------

ClioPfadeBase : ClioPatternFactory {

	make {
		var myP = this.args[0];
		var madePattern = if (myP.isKindOf(ClioFactory), {myP.make}, {myP});
		^this.makeType.new(madePattern, *this.args[1..]);
	}
}

ClioPfadeIn : ClioPfadeBase {

	initMe { arg ...args;
		super.initMe(PfadeIn, *args);
	}

}

ClioPfadeOut : ClioPfadeBase {

	initMe { arg ...args;
		super.initMe(PfadeOut, *args);
	}

}



// create factory classes for standard pattern classes? (ClioPbind, Pmono, Pseq, etc.?)
// SoundFile


// (
// title: "Arrangement Tools with Patterns",
//
//
// initModule: { | self |
//
// },
//
// makeWork: {arg self, workName, eWorkInit=();
// 	var myW = self.makeModule(workName, (clock:TempoClock.new) ++ eWorkInit);
//
// 	myW.protoP = (
// 		patternType:Pbind,
// 		playMe: { arg myP, eValues=();
// 			myP.bind(eValues).play(clock:myW.clock);
// 		},
// 		bind: { arg myP, eBindValues=();
// 			myP.patternType.new(*(myP ++ eBindValues).asPairs);
// 		},
// 	);
//
// 	myW.getP = {arg myW, eValues=();
// 		myW.getModule(myW.protoP ++ eValues);
// 	};
//
// 	myW.getMonoP = {arg myW, eValues=();
// 		myW.getModule(myW.protoP ++ (
// 			patternType:Pmono, // could also be PmonoArtic
// 			bind: { arg myP, eBindValues=();
// 				var myValues = myP ++ eBindValues;
// 				myP.patternType.new(myValues.instrument, *myValues.asPairs);
// 			},
// 		) ++ eValues);
// 	};
//
// 	myW.getPar = {arg myW, list=[], eValues=();
// 		myW.getModuleList(list, myW.protoP ++ (
// 			patternType:Ppar,
// 			bind:{arg myB, eValues=();
// 				myB.patternType.new(
// 					myB.listSize.collect{|i| myB.byIndex(i).bind(eValues[myB.nameList[i]]) };
// 				);
// 			};
// 		) ++ eValues);
// 	};
//
// 	myW.getSeq = {arg myW, list=[], eValues=();
// 		myW.getPar(list, (patternType:Pseq) ++ eValues);
// 	};
//
// 	myW.getRest = {arg myW, dur, eValues=();
// 		myW.getModule(myW.protoP ++ eValues ++ (dur:dur, type:Pn(\rest,1)));
// 	};
//
// 	myW.getFadeIn = {arg myW, fadeP, eValues=(fadeTime:2);
// 		myW.getModule(myW.protoP ++ (
// 			patternType:PfadeIn,
// 			bind:{arg myB, eBindValues=(); myB.patternType.new(fadeP.bind(eBindValues), myB.fadeTime);},
// 		) ++ eValues);
// 	};
//
// 	myW.getFadeOut = {arg myW, fadeP, eValues=(fadeTime:2);
// 		myW.getFadeIn(fadeP, (patternType:PfadeOut) ++ eValues);
// 	};
//
// 	// for making modules underneath the work....
//
// 	myW.makeP = {arg myW, name, eValues=();
// 		myW.makeModule(name, myW.getP(eValues));
// 	};
//
// 	myW.makeMonoP = {arg myW, name, eValues=();
// 		myW.makeModule(name, myW.getMonoP(eValues));
// 	};
//
// 	myW.makePar = {arg myW, name, list, eValues=();
// 		myW.makeModule(name, myW.getPar(list, eValues));
// 	};
//
// 	myW.makeSeq = {arg myW, name, list, eValues=();
// 		myW.makeModule(name, myW.getSeq(list, eValues));
// 	};
//
// 	myW.makeFadeIn = {arg myW, name, fadeP, eValues=(fadeTime:2);
// 		myW.makeModule(name, myW.getFadeIn(fadeP, eValues));
// 	};
//
// 	myW.makeFadeOut = {arg myW, name, fadeP, eValues=(fadeTime:2);
// 		myW.makeModule(name, myW.getFadeOut(fadeP, eValues));
// 	};
//
// 	myW;
//
// },
// )

