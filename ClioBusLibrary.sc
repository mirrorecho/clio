
ClioBusLibrary : ClioLibrary {

	makeBus { arg key, channels = 2, msgBusType = \audio;
		var myBus = this.atKey(key);

		if (myBus != nil, { myBus.free; });

		myBus = Message(Bus, msgBusType, [Clio.server, channels]).value;

		this.putKey(key, myBus);

		^myBus;
	}


	makeControlBus { arg name, channels = 1;
		^this.makeBus(name, channels, \control);
	}



}
