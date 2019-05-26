
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

