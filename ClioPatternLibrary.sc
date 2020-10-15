
ClioPatternLibrary : ClioLibrary {
	var <>tempoClock;

	play { arg key;
		^this.at(*key).play(clock:this.tempoClock);
	}

}




ClioBuildPatterns : ClioPatternLibrary {
	var <>defaultArgs;

	initMe {
		this[\base] = IdentityDictionary[];
		this[\base, \modelEvent] = (note:0, dur:1);
		this[\base, \func] = {|l, kw|
			ClioCell.fromEvents(()++kw[\modelEvent]);
		};
		this[\forms] = IdentityDictionary[];
		this[\layers] = IdentityDictionary[];
		this[\development] = IdentityDictionary[];
	}

	getKwargs { arg form=nil, layer=nil, development=0;
		this[\base] ++ this[\forms, form];
	}

	funcValue { arg form=nil, layer=nil, development=0;
		var myKwargs = this.getKwargs(form, layer, development);
		^myKwargs[\func].value(this, myKwargs);
	}





}