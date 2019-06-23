
ClioSynthDefFactory : ClioFactory {
	var <>defFunc, <>wrapFunc, <>sigFuncs, <>outFunc;

	*new { arg args=[] ...sigFuncs;
		^super.new.initMe(args, *sigFuncs);
	}


	initMe { arg args ...sigFuncs;

		// could be reset to add other universal kwargs, triggers, etc.
		this.defFunc = { arg name, self, kwargs;
			SynthDef(name, { arg gate=1;

				kwargs[\freq] = \freq.kr( kwargs[\freq] ? 440 );
				kwargs[\amp] = \amp.kr( kwargs[\amp] ? 0.6 );
				kwargs[\out] = \out.ir(kwargs[\out] ? Clio.busses[\master] );

				kwargs[\gate] = gate;

				self.wrapFunc.value(kwargs);

			}, rates:kwargs[\rates], prependArgs:nil, variants:kwargs[\variants], metadata:nil);
		};

		this.wrapFunc = { arg kwargs;
			kwargs[\sig]=0;
			this.sigFuncs.do { arg sigFunc;
				SynthDef.wrap(sigFunc, nil, [kwargs]);
			};
			SynthDef.wrap(this.outFunc, nil, [kwargs]);
		};

		this.sigFuncs = sigFuncs;
		this.outFunc = {arg kwargs; Out.ar(kwargs[\out], kwargs[\sig]);}; // could be reset to do fancy things with output
		this.makeType = SynthDef; // not really used, but setting for consistency sake alongside other factories
		this.args = args;
	}

	make { arg name, args;
		var kwargs = this.kwargs(*args);
		^this.defFunc.value(name, this, kwargs);
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


