
ClioSynthDefFactory : ClioFactory {
	var <>defFunc, <>wrapFunc, <>genFunc, <>processFuncs, <>outFunc;

	*new { arg genFunc, processFuncs=[], outFunc ...args;
		^super.new.initMe(genFunc, processFuncs, outFunc, *args);
	}


	initMe { arg genFunc, processFuncs, outFunc ...args;

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
			SynthDef.wrap(this.genFunc, nil, [kwargs]);
			this.processFuncs.do { arg processFunc;
				SynthDef.wrap(processFunc, nil, [kwargs]);
			};
			SynthDef.wrap(this.outFunc, nil, [kwargs]);
		};

		this.genFunc = genFunc;
		this.processFuncs = processFuncs;
		this.outFunc = outFunc;

		this.makeType = SynthDef;
		// this.defineFunction = args.pop;
		this.args = args;
	}

	make { arg name ...args;
		var kwargs = this.kwargs(*args);
		^this.defFunc.value(name, this, kwargs);
	}

	// TO DO: better handle nil name
	add { arg name ...args;
		^this.make(name, *args).add;
	}

	// TO DO: better handle nil name
	play { arg name ...args;
		^this.make(name, *args).play;
	}

}


//
// ClioSynthDefFactory : ClioFactory {
// 	var <>defineFunction;
//
// 	initMe { arg ...args;
// 		this.makeType = SynthDef;
// 		this.defineFunction = args.pop;
// 		this.args = args;
// 	}
//
// 	make { arg name ...args;
// 		^this.defineFunction.value(name, this.kwargs(*args));
// 	}
//
// 	// TO DO: better handle nil name
// 	add { arg name ...args;
// 		^this.make(name, *args).add;
// 	}
//
// 	// TO DO: better handle nil name
// 	play { arg name ...args;
// 		^this.make(name, *args).play;
// 	}
//
// }
//
//



//
//
//
//
// 	// The makeEffect function below wraps a simpler function within itself and provides
// 	// a crossfade into the effect (so you can add it without clicks), control over wet
// 	// and dry mix, etc.
// 	// Such functionality is useful for a variety of effects, and SynthDef-wrap
// 	// lets you reuse the common code.
// 	(
// 		// the basic wrapper
// 		~makeEffect = { |name, func, lags, numChannels = 2|
//
// 			SynthDef(name, { | i_bus = 0, gate = 1, wet = 1|
// 				var in, sound, env, lfo;
// 				in = In.ar(i_bus, numChannels);
// 				env = Linen.kr(gate, 2, 1, 2, 2); // fade in the effect
//
// 				// call the wrapped function. The in and env arguments are passed to the function
// 				// as the first two arguments (prependArgs).
// 				// Any other arguments of the wrapped function will be Controls.
// 				sound = SynthDef.wrap(func, lags, [in, env]);
//
// 				XOut.ar(i_bus, wet * env, sound);
// 			}, [0, 0, 0.1] ).add;
//
// 		};
// 	)
//
//
//
// 	// now make a wah
// 	(
// 		~makeEffect.value(\wah, { |in, env, rate = 0.7, ffreq = 1200, depth = 0.8, rq = 0.1|
// 			// in and env come from the wrapper. The rest are controls
// 			var lfo;
// 			lfo = LFNoise1.kr(rate, depth * ffreq, ffreq);
// 			RLPF.ar(in, lfo, rq, 10).distort * 0.15;
// 			},
// 			[0.1, 0.1, 0.1, 0.1],  // lags for rate ffreq, depth and rq
// 			2    // numChannels
// 		);
// 	)
//
// 	// now make a simple reverb
// 	(
// 		~makeEffect.value(\reverb, {|in, env|
// 			// in and env come from the wrapper.
// 			var input;
// 			input = in;
// 			16.do({ input = AllpassC.ar(input, 0.04, Rand(0.001,0.04), 3)});
// 			input
// 			},
// 			nil,  // no lags
// 			2    // numChannels
// 		);
// 	)