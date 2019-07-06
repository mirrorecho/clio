
ClioSynthDefFactory : ClioFactory {
	var <>defFunc, <>wrapFunc, <>outFunc;


	initMe { arg ...args;

		super.initMe(SynthDef, *args);
		this.universalKeys = [\synth];

		// could be reset to add other universal kwargs, triggers, etc.
		this.defFunc = { arg name, self, synthKwargs;
			SynthDef(name, { arg gate=1;

				synthKwargs[\sig]=0;

				synthKwargs[\freq] = \freq.kr( synthKwargs[\freq] ? 440 );
				synthKwargs[\amp] = \amp.kr( synthKwargs[\amp] ? 0.6 );
				synthKwargs[\out] = \out.ir(synthKwargs[\out] ? Clio.busses[\master] );

				synthKwargs[\gate] = gate;

				self.wrapFunc.(synthKwargs);

			}, rates:synthKwargs[\rates], prependArgs:nil, variants:synthKwargs[\variants], metadata:nil);
		};

		this.wrapFunc = { arg synthKwargs;
			this.args.pairsDo { arg funcFactory, args;
				args = args ++ [synth:synthKwargs];
				funcFactory.wrap(args);
			};
			SynthDef.wrap(this.outFunc, nil, [synthKwargs]);
		};

		// could be reset to do fancy things with output:
		this.outFunc = {arg synthKwargs; Out.ar(synthKwargs[\out], synthKwargs[\sig]);};
	}

	// TO DO MAYBE: should this make signature be the same as other factories??? i.s. make { arg ... args;
	make { arg name, args=[];
		^this.defFunc.(name, this, args.asDict);
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

