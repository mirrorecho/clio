
ClioPatternLibrary : ClioLibrary {
	var <>tempoClock;

	play { arg key;
		^this.at(*key).play(clock:this.tempoClock);
	}

}
