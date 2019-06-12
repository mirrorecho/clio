
ClioPatternLibrary : ClioLibrary {
	var <>tempoClock;

	play { arg key;
		this.atKey(key).play(clock:this.tempoClock);
	}

}
