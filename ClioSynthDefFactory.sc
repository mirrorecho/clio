
ClioSynthDefFactory : ClioFactory {
	var <>defFunc, <>wrapFunc, <>outFunc;


	initMe { arg ...args;

		super.initMe(SynthDef, *args);

		// could be reset to add other universal kwargs, triggers, etc.
		this.defFunc = { arg name, self, synthKwargs;
			SynthDef(name, { arg gate=1;

				synthKwargs[\freq] = \freq.kr( synthKwargs[\freq] ? 440 );
				synthKwargs[\amp] = \amp.kr( synthKwargs[\amp] ? 0.6 );
				synthKwargs[\out] = \out.ir(synthKwargs[\out] ? Clio.busses[\master] );

				synthKwargs[\gate] = gate;

				self.wrapFunc.(synthKwargs);

			}, rates:synthKwargs[\rates], prependArgs:nil, variants:synthKwargs[\variants], metadata:nil);
		};

		this.wrapFunc = { arg synthKwargs;
			synthKwargs[\sig]=0;
			this.args.pairsDo { arg sigFunc, args;
				var kwargs = args.asDict;
				SynthDef.wrap(sigFunc, nil, [synthKwargs, kwargs]);
			};
			SynthDef.wrap(this.outFunc, nil, [synthKwargs]);
		};

		// could be reset to do fancy things with output:
		this.outFunc = {arg synthKwargs; Out.ar(synthKwargs[\out], synthKwargs[\sig]);};
	}

	make { arg name, args;
		var synthKwargs = this.kwargs(*args);
		^this.defFunc.(name, this, synthKwargs);
	}

	// TO DO: better handle nil name
	add { arg name, args;
		^this.make(name, args).add;
	}

	// TO DO: better handle nil name
	play { arg name, args;
		^this.make(name, args).play;
	}

}


// ClioSynthHelper {
// 	var <>function; // these should be pairs
// 	var <>synthKwargs;
// 	var <>kwargs;
//
// 	*new { arg function;
// 		myHeloper = ^super.new;
// 		myHelper.function = function;
// 		myHelper.
// 		^myHelper;
// 	};
//
// 	make {
// 		^this.function.
// 	};
//
// }
//
