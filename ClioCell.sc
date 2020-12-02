
ClioCell : ClioPatternFactory {


	var <>modelEvent;
	var <>events;

	*fromEvents { arg ...args;
		var c = super.new(args[0].deepCopy);
		c.streamAppends(*args);
		^c;
	}

	*fromArgs { arg ...args;
		var dict = args.asDict;
		var streamKeys = dict[\streamKeys] ?? dict.keys;
		var streamDict = dict.select({|v, k| streamKeys.includes(k) };);
		// TO DO MAYBE: explicit pattenKeys
		var patternDict = dict.reject({|v, k| (streamKeys++[\streamKeys, \streamSize]).includes(k) };);
		var streamSize = dict[\streamSize] ?? streamDict.asArray[0].size;
		var eventsArray = streamSize.collect{|i|
			streamDict.collect{|stream|stream[i]}.asEvent;
		};
		var c = ClioCell.fromEvents(*eventsArray);
		c.setKwargs(*patternDict.asPairs);
		^c;
	}

	apply { arg func = {|c|};
		func.value(this);
		^this;
	}

		// TO DO: make this work for patterns other than Pbind (e.g. Pmono, PmonoArtic)
	initMe { arg modelEvent=() ...args;
		this.modelEvent = modelEvent;
		super.initMe(Pbind, *args);
	}

	streamKeys {
		^this.modelEvent.keys;
	}

	streamSize {
		^this.events.size;
	}

	streamInsert{ arg index=0, event=();
		var myIndex = this.convertIndex(index);
		this.events = this.events.insert(myIndex,this.modelEvent ++ event );
	}

	streamInsertAfter{ arg index=0, event=();
		var myIndex = this.convertIndex(index)+1;
		this.events = this.events.insert(myIndex,this.modelEvent ++ event );
	}

	streamAppend{ arg event=();
		this.events = this.events.insert(this.streamSize,this.modelEvent ++ event );
	}

	streamAppends{ arg ...events;
		this.events = this.events ++ events.collect{|e|this.modelEvent ++ e};
	}

	streamExtend { arg count, event=();
		this.events = this.events ++ ( (this.modelEvent++event)!count);
	}

	convertIndex{arg index;
		^if (index<0, {this.streamSize+index}, {index});
	}

	convertIndices {arg indices;
		^indices.collect{|i|this.convertIndex(i)};
	}

	get{arg index;
		^this.events[this.convertIndex(index)];
	}

	cellPseqs {
		^this.streamKeys.asArray.collect{|k|
			[k,this.streamPseq(k)]
		}.flat.asDict;
	}

	kwargs { arg ...args;
		^super.kwargs(*((this.cellPseqs ++ args.asDict).asPairs))
	}

	streamArray {arg key;
		^this.events.collect{|e|e[key]};
	}

	setStreamArray {arg key, values;
		if ( not(this.modelEvent.keys.includes(key)), {
			this.modelEvent[key] = values[0];
		});
		^this.events.do{|e,i|
			e[key] = values[i];
		};
	}

	streamPseq {arg key;
		^Pseq(this.streamArray(key));
	}

	durs {
		^this.streamArray(\dur);
    }

    durs_ { | newValues |
		this.setStreamArray(\dur, newValues);
    }

	dur {
		^this.durs.sum;
	}

	amps {
		^this.streamArray(\amp);
    }

    amps_ { | newValues |
		this.setStreamArray(\amp, newValues);
    }

	notes {
		^this.streamArray(\note);
    }

    notes_ { | newValues |
		this.setStreamArray(\note, newValues);
    }


	doIndices {arg indices, func;
		var myIndices = this.convertIndices(indices);
		this.events.do{|e,i|
			if (myIndices.includes(i), { func.value(e) });
		};
	}

	exceptIndices {arg indices;
		^(0..this.streamSize-1).removeAll(this.convertIndices(indices));
	}

	// =======================================================
	// MANIPULATIONS THAT RETURN NEW COPIES:

	++ { arg addCell;
		var c = this.mimic;
		c.events = c.events ++ (addCell.events.deepCopy);
		^c;
	}

	* { arg times=1;
		var c = this.mimic;
		// TO DO: extra deepCopy here... refactor?
		c.events = ({c.events.deepCopy}!times).flat;
		^c;
	}

	// TRANSPOSE
	t { arg transposition=0;
		var c = this.mimic;
		c.notes = this.notes + transposition;
		^c;
	}

	mul { arg ampMul;
		var c = this.mimic;
		c.amps = c.amps * ampMul;
		^c
	}

	mask { arg ...indices;
		var c = this.mimic;
		this.convertIndices(indices).do{|i| c.events[i].note=\rest };
		^c;
	}

	poke { arg ...indices;
		^this.mask(*this.exceptIndices(indices));
	}

	select { arg ...indices;
		var c = this.mimic;
		c.events = c.convertIndices(indices).collect{|i|c.events[i].deepCopy};
		^c;
	}

	remove { arg ...indices;
		var c = this.mimic;
		c.events = c.exceptIndices(indices).collect{|i|c.events[i]};
		^c;
	}

	before {arg index;
		var keepIndices = (0..this.convertIndex(index)-1);
		var c = this.mimic;
		// note, can't use slice here since it doesn't return array
		// for single-index slices
		c.events = keepIndices.collect{|i|c.events[i]};
		^c;
	}

	after {arg index;
		var keepIndices = (this.convertIndex(index)..this.streamSize-1);
		var c = this.mimic;
		// note, can't use slice here since it doesn't return array
		// for single-index slices
		c.events = keepIndices.collect{|i|c.events[i]};
		^c;
	}

	beforeDur { arg dur;
		var c = this.mimic;
		var durCount = 0;
		if (dur<0, {dur=this.dur+dur});
		// TO DO: this could be more elegant...
		c.events = [];
		this.durs.do{|d,i|
			if (durCount < dur, {
				c.streamAppend((
					dur:[d, dur-durCount].minItem
				));
				durCount = durCount + d;
			});
		};
		^c;
	}

	afterDur { arg dur;
		var c = this.mimic;
		var durCount = 0;
		if (dur<0, {dur=this.dur+dur});
		// TO DO: this could be more elegant...
		c.events = [];
		this.durs.do{|d,i|
			durCount = durCount + d;
			if (durCount > dur, {
				c.streamAppend((
					dur:[d, durCount-dur].minItem
				));
			});
		};
		^c;
	}


	bookend {arg before=0, after=0;
		var c = this.mimic;
		if (before>0, { c.streamInsert(0, (dur:before, note:\rest)); } );
		if (after>0, { c.streamInsertAfter(-1, (dur:after, note:\rest)); } );
		^c;
	}

	accent { arg indices=[0], accentMul=1, offMul=0.5;
		var c = this.mimic;
		var myIndices = this.convertIndices(indices);

		c.amps = this.amps.collect{|v,i|
				if (myIndices.includes(i), {v*accentMul}, {v*offMul});
			}
		^c;
	}

	fadeIn {
		var c = this.mimic;
		c.amps = c.amps * (1..this.streamSize)/this.streamSize;
		^c;
	}

	fadeOut {
		var c = this.mimic;
		c.amps = c.amps * (this.streamSize..1)/this.streamSize;
		^c;
	}

	ghost { arg accentMul=1, offMul=0.5, ghostDur=1;
		var c = this.mimic;
		c.events = [];
		this.events.do{|e|
			// note, unusual durations should be expected if dur not divisible by ghostDur
			var numGhosts = ((e.dur/ghostDur)-1).floor;

			c.streamAppend(e ++ (
				amp:e.amp*accentMul,
				dur:ghostDur,
			));
			c.streamExtend(numGhosts, e ++ (
				amp:e.amp*offMul,
				dur:ghostDur,
			));
		};
		^c;
	}

	/*
	=======================================================

	TO DO:
	- fuse
	FROM CALLIOPE
	- fuseRepeats
	- cropChords(indices=(None,), above=(None,), below=(None,))
	- ts(self, steps, scale=None, new_scale=None):
    --- shortcut for transposing within scale
	- stack_p(intervals=[12])
	- sc(self, scale=2) : scale rhythm
	- smear_before
	- smear_after

    MAYBE
	- scramble (with supercollider, not calliope meaning)
	- with_only(*args) : leave behind only matching condition events
	- smart_ranges(ranges=( (0,12), ) )
	- durs, note, amp (or other kwargs) process/hook function

	MORE THOUGHTS;
	- tree data structure (like calliope)?????
	--- at minimum model ClioEvent, ClioCell, and ClioCellBlock?
	--- or only reinventing event stream wheel?
	- selection modeling?

	=======================================================
	*/


}


