
ClioPatternLibrary : ClioLibrary {
	var <>tempoClock;

	play { arg key;
		this.atKey(key).play(tempoClock:this.tempoClock);
	}

}
