
ClioBusLibrary : ClioLibrary {

	makeBus { arg key, numChannels = 2, msgBusType = \audio;
		var myBus = this.at(*key);

		if (myBus != nil, { myBus.free; });

		myBus = Message(Bus, msgBusType, [Clio.server, numChannels]).value;

		this.put(*(key.asArray ++ myBus));

		^myBus;
	}


	makeControlBus { arg name, numChannels = 1;
		^this.makeBus(name, numChannels, \control);
	}



}
