

// TO DO... depreciate????

ClioBufferLibrary : ClioLibrary {

	makeBuffer { arg key, length, channels;
		^this.putKey(key, Buffer.alloc(Clio.server, Clio.server.sampleRate * 8.0, 2));
	}

	play { arg key;

	}

	// TO DO... add playback

}


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
