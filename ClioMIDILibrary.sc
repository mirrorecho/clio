

// TO DO... depreciate????

ClioMIDILibrary : ClioLibrary {

	var <>synthName;
	var <>notes;
	var <>postNote;
	var <>ccMapping;

	initMe {

		// TO DO... think about how to control functions, multiple devices, without reinventing the wheel
		// this.put(\ccFunc, {arg self, ccKey, msgValue, msgNum, chan, src; });
		// this.put(\noteOnFunc, {arg self, vel, midinote; });
		// this.put(\noteOffFunc, {arg self, vel, midinote; });

		// MIDI mappings for M-Audio Radium 61
		this.putTree(\ccMappings, [\radium61, [
			1:  \mod,
			82: \slide1,
			83: \slide2,
			28: \slide3,
			29: \slide4,
			16: \slide5,
			80: \slide6,
			18: \slide7,
			19: \slide8,
			74: \knob9,
			71: \knob10,
			81: \knob11,
			91: \knob12,
			2:  \knob13,
			10: \knob14,
			5:  \knob15,
			21: \knob16,
			7:  \slideData
		].asPairs]);


		MIDIIn.connectAll;

		// this.put(\ccMapping, this.at( *([\ccMappings] ++ ccMappingKey) ));

		this.ccMapping = \radium61;
		this.at(\ccMappings, this.ccMapping).do { arg ccKey;
			this.put(\ccCurrent, ccKey, 0);
		};

		this.notes = Array.newClear(128);
		this.synthName = \sampledPiano;
		this.postNote = false;

		MIDIdef.noteOn(\noteOn, {arg vel, midinote;
			this.notes[midinote] = Synth(this.synthName, [
				\freq, (midinote + rand(0.2) - 0.1).midicps,
				\amp, vel.linlin(0, 127, 0.001, 1.1)
			]);
			if (this.postNote, {("NOTE: " ++ (midinote-60) ++ ", MIDI: " ++ midinote ++ ", FREQ: " ++ midinote.midicps).postln;});
			// this.noteOnFunc(vel, midinote);
		}
		);

		MIDIdef.noteOff(\noteOff, {arg vel, midinote;
			this.notes[midinote].release; // same as ~noteArray[midinote].set(\gate, 0);
			// this.noteOffFunc(vel, midinote);
		});

		MIDIdef.cc(\listener,  {arg msgValue, msgNum, chan, src;
			var ccKey = this[\ccMappings, this.ccMapping, msgNum];
			this[\ccCurrent, ccKey] = msgValue;
			// this.ccFunc(ccKey, msgValue, msgNum, chan, src);
		});



	}


}

