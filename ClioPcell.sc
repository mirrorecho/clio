//
// ClioPcell : ClioPatternFactory {
//
// 	durs {
// 		^this.kwargs[\durs]
// 	}
//
// 	durs_ { | newValue |
// 		this.setKwarg(\durs, newValue);
// 	}
//
// 	amps {
// 		^this.kwargs[\amps]
// 	}
//
// 	amps_ { | newValue |
// 		this.setKwarg(\amps, newValue);
// 	}
//
// 	notes {
// 		^this.kwargs[\notes]
// 	}
//
// 	notes_ { | newValue |
// 		this.setKwarg(\notes, newValue);
// 	}
//
// 	/*
// 	=======================================================
// 	THOUGHTS;
// 	- tree data structure (like calliope)?????
// 	--- at minimum model ClioEvent, ClioCell, and ClioCellBlock?
// 	--- or only reinventing event stream wheel?
// 	- selection modeling
// 	- arbitrary ordered kwargs
//
// 	TO DO:
// 	- fuse
// 	- trim (remove beginning, end # of values)
// 	FROM CALLIOPE
// 	- fuseRepeats
// 	- cropChords(indices=(None,), above=(None,), below=(None,))
// 	- onlyFirst(count=1)
// 	- onlyLast(count=1)
// 	- crop(crop_start=0, crop_end=0)
// 	- pop_from(*args)
// 	- scramble
// 	- poke(*args) : everything other than specified events become rests
// 	- mask(*) : specifed events become rests (replace "rest"?)
// 	- t(indices) : transpose within scale
// 	- ts(self, steps, scale=None, new_scale=None):
// 	--- shortcut for transposing within scale, since used A LOT
// 	- stack_p(intervals=[12])
// 	- sc(self, scale=2) : scale rhythm
// 	- bookend_pad(beats_before=0, beats_after=0)
// 	- smear_before
// 	- smear_after
//
// 	MAYBE
// 	- with_only(*args) : leave behind only matching condition events
// 	- smart_ranges(ranges=( (0,12), ) )
// 	- durs, note, amp (or other kwargs) process/hook function
// 	=======================================================
// 	*/
//
// 	// TO DO: make this work for patterns other than Pbind (e.g. Pmono, PmonoArtic)
// 	initMe { arg ...args;
// 		super.initMe(Pbind, *args);
// 		if (this.durs.isNil, {this.durs=[1]});
// 		if (this.amps.isNil, {this.amps = 1!this.durs.size});
// 		if (this.notes.isNil, {this.notes = 0!this.durs.size});
// 	}
//
// 	make { arg ...args;
// 		var cellArgs = [
// 			dur:Pseq(this.durs),
// 			amp:Pseq(this.amps),
// 			note:Pseq(this.notes),
// 		];
// 		var myKwargs = this.args.asDict ++ cellArgs.asDict ++ args.asDict;
// 		[\durs, \amps, \notes].do{|k|
// 			myKwargs.removeAt(k);
// 		};
// 		^super.make(*(myKwargs.asPairs));
// 	}
//
// 	// TO DO: align with calliope
// 	tr { arg transposition=0;
// 		^this.mimic(*[notes:this.notes + transposition]);
// 	}
//
// 	// TO DO: align with calliope
// 	* { arg times=1;
// 		^this.mimic(*[
// 			durs:(this.durs!times).flat,
// 			amps:(this.amps!times).flat,
// 			notes:(this.notes!times).flat,
// 		]);
// 	}
//
// 	++ { arg addCell;
// 		var c = this.mimic;
// 		c.durs = c.durs ++ addCell.durs;
// 		c.amps = c.amps ++ addCell.amps;
// 		c.notes = c.notes ++ addCell.notes;
// 		^c;
// 	}
//
// 	cut { arg tocks;
// 		var c = this.mimic;
// 		var tockCount = 0;
// 		// TO DO: this could be more elegant
// 		c.durs = [];
// 		c.amps = [];
// 		c.notes = [];
// 		this.durs.do{|d,i|
// 			if (tockCount < tocks, {
// 				[d,i].postln;
// 				c.durs = c.durs.add([d, tocks-tockCount].minItem);
// 				c.amps = c.amps.add(this.amps[i]);
// 				c.notes = c.notes.add(this.notes[i]);
// 				tockCount = tockCount + d;
// 			});
// 		};
// 		^c;
// 	}
//
// 	mul { arg ampMul;
// 		^this.mimic(*[amps:this.amps * ampMul]);
// 	}
//
//
// 	rest {
// 		arg indices = [0];
// 		var c = this.mimic;
// 		indices.do{|i|
// 			c.notes[i] = \rest
// 		};
// 		^c;
// 	}
//
// 	accent {
// 		arg indices = [0], accentMul=1, offMul=0.5;
// 		^this.mimic(*[
// 			amps:this.amps.collect{|v,i|
// 				if (indices.includes(i), {v*accentMul}, {v*offMul});
// 			}
// 		]);
// 	}
//
// 	ghost {
// 		arg accentMul=1, offMul=0.5, ghostDur=1;
// 		var c = this.mimic;
// 		c.durs = [];
// 		c.amps = [];
// 		c.notes = [];
// 		this.durs.do{|d,i|
// 			var selfAmp = this.amps[i];
// 			// note, unusual durations should be expected if dur not divisible by ghostDur
// 			var numEvents = d/ghostDur;
// 			c.durs = c.durs ++ (ghostDur!numEvents);
// 			c.amps = c.amps ++ [selfAmp*accentMul] ++ ((selfAmp*offMul)!(numEvents-1));
// 			c.notes = c.notes ++ (this.notes[i]!numEvents);
// 		};
// 		^c;
// 	}
//
// }
//
