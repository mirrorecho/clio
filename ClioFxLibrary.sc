
ClioFxLibrary : ClioSynthDefLibrary {

	makeFx { arg key, channels = 2; // creates a new audio bus
		var myBus = this.atKey(key);

		if (myBus != nil, { myBus.free; });

		myBus = Message(Bus, msgBusType, [Clio.server, channels]).value;

		this.putKey(key, myBus);

		^myBus;
	}



}
