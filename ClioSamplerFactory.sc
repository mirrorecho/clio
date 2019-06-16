
ClioSamplerFactory : ClioSynthDefFactory {

	kwargs { arg ...args;
		var myKwargs = super.kwargs(*args);

		var buffersAndFreqs = myKwargs[\keysAndFreqs].collect { arg keyAndFreq, i;
			[
				myKwargs[\soundLibrary].atKey(keyAndFreq[0]).buffer, // the buffer
				keyAndFreq[1], // the frequency of the sound for this buffer
			];
		};

		var cutoverFreqs = buffersAndFreqs[..buffersAndFreqs.size-2].collect {arg bufferAndFreq, i;
			var
			freq = bufferAndFreq[1],
			nextFreq = buffersAndFreqs[i+1][1];

			freq * ((nextFreq/freq)**0.5);
		};


		myKwargs[\getSample] = {arg freq;
			var whichSample = 0;

			cutoverFreqs[..cutoverFreqs.size-2].do {|cutoverFreq, i|
				whichSample = whichSample + ((i+1) * (freq >= cutoverFreq) * (freq < cutoverFreqs[i+1]));
			};

			if (cutoverFreqs.size > 0,
				{ whichSample = whichSample + ((cutoverFreqs.size) * (freq >= cutoverFreqs[cutoverFreqs.size-1]));}
			);

			Select.kr(whichSample, buffersAndFreqs);
		};

		^myKwargs;

	}


}

