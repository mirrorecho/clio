
Clio {
	classvar <>server, <>busses, <>synths;

	*go { arg callback;

		Server.killAll;  // just in case things are crazy

		CmdPeriod.removeAll;
		CmdPeriod.add({
			MIDIdef.freeAll;
			OSCdef.freeAll;
			Server.freeAll;
			Server.all.do{ |s| // freeing everything for ALL servers, just in case things are crazy
				Buffer.freeAll(s);
				s.newAllocators; // new allocators (numbers) for busses, buffers, etc.
				s.freeAll;
			};

			this.busses = ClioBusLibrary.new;
			this.synths = ClioSynthLibrary.new;

			ClioMastering.go { callback.value; };

		});

		this.server = Server.local;
		this.server.waitForBoot({CmdPeriod.run;});
		this.server.reboot;

	}

	*path {
		^PathName(this.filenameSymbol.asString).pathOnly;
	}

	*openAll {
		(this.path ++ "*.sc").pathMatch.do{|doc|doc.openDocument;};
	}

	*testMe {
		"I am Clio".postln;
	}

}



// TO DO
/*
- YAY! A single SoundLibrary combining the above.
- - - load buffers lazily, or,  upfront with loadBuffers(keys)
- - - create new abstract definition of a sound, with ability to allocate buffer from it
- - - create soundfile from buffer and write it
- - - read from bus into buffer
- - - (eventually) visualization tools with SoundFileView
- - - (eventually) tagging and query tools
- finish sampler
- default kwargs for SynthDefFactory and SamplerFactory
- SynthDefLibrary
- SamplerLibrary
- SampleDataLibrary
- OSC setup
- MIDI setup
- MIDI/OSC pattern playback
*/


// ATTEMPT TO WORK WITH ServerBoot... but creates infite loop on 2nd reboot... WHY?...
/*Clio {
	classvar <>server, <>busses, <>synths;

	*go { arg callback;

		this.server = Server.local;

		CmdPeriod.removeAll;
			CmdPeriod.add({
				MIDIdef.freeAll;
				OSCdef.freeAll;
				Server.freeAll;
				Server.all.do{ |s| // freeing everything for ALL servers, just in case things are crazy
					Buffer.freeAll(s);
					s.newAllocators; // new allocators (numbers) for busses, buffers, etc.
					s.freeAll;
				};

				this.busses = ClioBusLibrary.new;
				this.synths = ClioSynthLibrary.new;

				ClioMastering.go { callback.value; };

			});

		ServerBoot.removeAll;
		ServerBoot.add({
			// Server.killAll;  // just in case things are crazy

			this.server.waitForBoot({CmdPeriod.run;});

			// this.server.reboot;
		}, this.server);

	}

	*path {
		^PathName(this.filenameSymbol.asString).pathOnly;
	}

	*openAll {
		(this.path ++ "*.sc").pathMatch.do{|doc|doc.openDocument;};
	}

	*testMe {
		"I am Clio".postln;
	}

}*/

