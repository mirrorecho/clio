
ClioMastering {

	*go { arg callback;

		{

			Clio.busses.makeBus(\master);
			Clio.busses.makeBus(\masterOut);

			Clio.server.sync;

			"".postln;
			"Clio.busses[\\master] created... send all synth outputs here. Do not pass go. Do not collect $200.".postln;
			"".postln;

			SynthDef(\masterFx, { arg reverbRoom=0.44, reverbMix=0.2;
				var sig = In.ar(Clio.busses[\master], 2);
				sig = FreeVerb2.ar(sig[0], sig[1], room:reverbRoom, mix:reverbMix);
				Out.ar(Clio.busses[\masterOut], sig);
			}).add;

			SynthDef(\masterOut, {
				var sig = In.ar(Clio.busses[\masterOut],2);
				sig = Limiter.ar(sig, 0.9);
				Out.ar(0, sig);
			}).add;

			Clio.server.sync;

			// masterOut synth has to be created first before masterFx... WHY?
			Clio.synths.makeSynth(\masterOut);

			Clio.server.sync;

			Clio.synths.makeSynth(\masterFx);

			Clio.server.sync;

			callback.value;

		}.fork;

	}

}

