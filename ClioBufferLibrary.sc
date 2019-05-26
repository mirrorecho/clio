
ClioBufferLibrary : ClioLibrary {

	var <>paths;

	*new { arg ...paths;
		^super.new.initMe(*paths);
	}

	initMe { arg ...paths;
		this.paths = paths.asSet;
		this.paths.do { arg pathString; this.addFolder(pathString); }
	}

	addFolder { arg folderString, key=[];

		SoundFile.collect(folderString ++ "*").do { arg file;
			var soundSymbol = file.path.basename.splitext[0].asSymbol;
			this.put(*(key ++ [soundSymbol, file]));
		};


		// recursively add subfolders:
		PathName(folderString).folders.do {arg folder;
			this.addFolder(folder.pathOnly, key ++ [folder.folderName.asSymbol]);
		};

	}


	// TO DO... add method to collect into buffer library

	// TO DO... add playback

}


	// load: { arg self, libraries=[];
	// 	{
	// 		s.sync;
	// 		libraries.do { arg libraryName;
	// 			self.loadLibrary(libraryName);
	// 			s.sync;
	// 		};
	// 	}.forkIfNeeded;
	// },
	//
	// loadLibrary: {arg self, libraryName, files=[]; // if empty arry for files, then will load all in the dir
	// 	var postMsgs = ["Loading buffers from '" ++ libraryName ++ "' library:"], eLibrary = ();
	// 	SoundFile.collectIntoBuffers(self.libraryPath ++ libraryName ++ "/*").do { arg buffer;
	// 		var bufferName = buffer.path.basename.splitext[0];
	// 		eLibrary[bufferName.asSymbol] = buffer;
	// 		postMsgs = postMsgs.add("~ss.buf['" ++ libraryName ++ "']['" ++ bufferName ++ "']");
	// 	};
	// 	self.makeModule(libraryName, eLibrary);
	// 	~ss.postPretty(postMsgs);
	// },
	//
	//
	// loadLibraries: {arg self, libraryName, files=[]; // if empty arry for files, then will load all in the dir
	// 	var postMsgs = ["Loading buffers from '" ++ libraryName ++ "' library:"], eLibrary = ();
	// 	SoundFile.collectIntoBuffers(self.libraryPath ++ libraryName ++ "/*").do { arg buffer;
	// 		var bufferName = buffer.path.basename.splitext[0];
	// 		eLibrary[bufferName.asSymbol] = buffer;
	// 		postMsgs = postMsgs.add("~ss.buf['" ++ libraryName ++ "']['" ++ bufferName ++ "']");
	// 	};
	// 	self.makeModule(libraryName, eLibrary);
	// 	~ss.postPretty(postMsgs);
	// },
	//
	//
	//
	// makeBus { arg name, channels = 2, msgBusType = \audio;
	// 	var myBus = this.at(name);
	//
	// 	if (myBus != nil, { myBus.free; });
	//
	// 	myBus = Message(Bus, msgBusType, [Clio.server, channels]).value;
	//
	// 	this.put(name, myBus);
	//
	// 	^myBus;
	// }
	//
	//
	// makeControlBus { arg name, channels = 1;
	// 	^this.makeBus(name, channels, \control);
	// }
	//
	//
	//
	// }
	//
	//
	// sounds = ClioBufferLibrary("/Users/rwest/Echo/Sounds/Library/");
	//
	//
	//
	// loadLibrary: {arg self, libraryName, files=[]; // if empty arry for files, then will load all in the dir
	// var postMsgs = ["Loading buffers from '" ++ libraryName ++ "' library:"], eLibrary = ();
	// SoundFile.collectIntoBuffers(self.libraryPath ++ libraryName ++ "/*").do { arg buffer;
	// 	var bufferName = buffer.path.basename.splitext[0];
	// 	eLibrary[bufferName.asSymbol] = buffer;
	// 	postMsgs = postMsgs.add("~ss.buf['" ++ libraryName ++ "']['" ++ bufferName ++ "']");
	// };
	// self.makeModule(libraryName, eLibrary);
	// ~ss.postPretty(postMsgs);
	// },
	//
	// load: { arg self, libraries=[];
	// {
	// 	s.sync;
	// 	libraries.do { arg libraryName;
	// 		self.loadLibrary(libraryName);
	// 		s.sync;
	// 	};
	// }.forkIfNeeded;
	// },




// SynthDef(
// 	"bufPlay", {arg bufnum, amp=1.0, rate=1.0, start=0, out=~ss.bus.master;
// 		var sig = PlayBuf.ar(2,
// 			bufnum:bufnum,
// 			rate:BufRateScale.kr(bufnum)*rate,
// 			startPos:BufSampleRate.kr(bufnum) * start,
// 			doneAction:2,
// 		);
// 		sig = sig * amp;
// 		Out.ar(out, sig);
// }).add;
//
// SynthDef("bufPerc", {
// 	arg bufnum, amp=1.0, rate=1.0, start=0, attackTime=0.001, releaseTime=1, curve= -4, out=~ss.bus.master;
// 	var sig = PlayBuf.ar(2,
// 		bufnum:bufnum,
// 		rate:BufRateScale.kr(bufnum)*rate,
// 		startPos:BufSampleRate.kr(bufnum) * start,
// 		doneAction:2,
// 	);
// 	var env = Env.perc(attackTime:attackTime, releaseTime:releaseTime, level:amp, curve:curve);
// 	sig = sig * amp * EnvGen.ar(env, doneAction: 2);
// 	Out.ar(out, sig);
// }).add;
//
//
// // NOTE: this implementation based delays causes amp to increase slightly after synth starts...
// /// .. and sounds different at beginning before overlap starts ... rethink?
// SynthDef("bufDrone",{
// 	arg bufnum, amp=1.0, rate=1.0, gate=1,
// 	attackTime=0.1, decayTime=0.2, sustainLevel=1, releaseTime=1, curve= -4,
// 	out=~ss.bus.master;
//
// 	var sigs, envs, sig;
// 	var length = BufDur.ir(bufnum);
// 	var envTimes = (length / rate / 4)!3;
//
//
// 	sigs = 4.collect{ |i|
// 		PlayBuf.ar(
// 			numChannels:2,
// 			bufnum:bufnum,
// 			rate:BufRateScale.ir(bufnum)*rate,
// 			startPos:BufSampleRate.ir(bufnum) * length * i/4,
// 		loop:1);
// 	};
//
// 	envs = [
// 		EnvGen.kr(Env.new(levels: [0, 0.25, 0.5, 0.25, 0], times: envTimes, curve: [6,-6,6,-6,]).circle),
// 		EnvGen.kr(Env.new(levels: [0.25, 0.5, 0.25, 0, 0.25], times: envTimes, curve: [-6,6,-6,6]).circle),
// 		EnvGen.kr(Env.new(levels: [0.5, 0.25, 0, 0.25, 0.5], times: envTimes, curve: [6,-6,6,-6]).circle),
// 		EnvGen.kr(Env.new(levels: [0.25, 0, 0.25, 0.5, 0.25], times: envTimes, curve: [-6,6,-6, 6,]).circle),
// 	];
// 	sigs = sigs * envs;
//
// 	sig = Mix.ar(sigs) * EnvGen.kr(
// 		Env.adsr(attackTime, decayTime, sustainLevel, releaseTime, peakLevel:amp, curve:curve,
// 		),
// 	gate:gate, doneAction:2);
//
// 	Out.ar(out, sig);
//
// }).add;
//
// SynthDef("bufSwell", {
// 	arg bufnum, amp=1.0, rate=1.0, start=0, dur=1, releaseTime=0.01, curve=4, tempo=1, out=~ss.bus.master;
// 	var sig = PlayBuf.ar(2,
// 		bufnum:bufnum,
// 		rate:BufRateScale.kr(bufnum)*rate,
// 		startPos:BufSampleRate.kr(bufnum) * start,
// 		doneAction:2,
// 	);
// 	var env = Env.perc(attackTime:dur/tempo, releaseTime:releaseTime, level:amp, curve:curve);
// 	sig = sig * amp * EnvGen.ar(env, doneAction: 2);
// 	Out.ar(out, sig);
// }).add;
//
// },
//
//
// playBuf: { arg self, libraryName, bufferName, args=[];
// 	var bufnum = self[libraryName.asSymbol][bufferName.asSymbol];
// 	~ss.synther.makeSynth("bufPlay", "bufPlay", [bufnum:bufnum]++args);
// },
//
// drone: { arg self, libraryName, bufferName, args=[];
// 	var bufnum = self[libraryName.asSymbol][bufferName.asSymbol];
// 	~ss.synther.makeSynth("bufDrone", "bufDrone", [bufnum:bufnum]++args);
// },
//
// perc: { arg self, libraryName, bufferName, args=[];
// 	var bufnum = self[libraryName.asSymbol][bufferName.asSymbol];
// 	~ss.synther.makeSynth("bufPerc", "bufPerc", [bufnum:bufnum]++args);
// },
//
//


