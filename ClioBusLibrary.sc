
ClioBusLibrary : ClioLibrary {

	makeBus { arg name, channels = 2, msgBusType = \audio;
		var myBus = this.at(name);

		if (myBus != nil, { myBus.free; });

		myBus = Message(Bus, msgBusType, [Clio.server, channels]).value;

		this.put(name, myBus);

		^myBus;
	}


	makeControlBus { arg name, channels = 1;
		^this.makeBus(name, channels, \control);
	}



}
