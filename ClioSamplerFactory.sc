
ClioSamplerFactory : ClioSynthDefFactory {
	var <>defineFunction;


	make { arg name, soundLibrary, libraryKeys, soundFreqs ...args; // warning these cannot contain factories, should they?

		var buffersAndFreqs = libraryKeys.collect { arg key, i;
			[soundLibrary.atKey(key).buffer, soundFreqs[i]];
		};

		var cutoverFreqs = soundFreqs[..soundFreqs.size-2].collect {arg freq, i;
			freq + ((soundFreqs[i+1] - freq) / 2);
		};

		cutoverFreqs.postln;

		// TO DO... make this cleaner with kwargs!!!
		args = args ++ [\getSample, {arg selfKwargs, freq;
			var whichSample = 0;
			cutoverFreqs[..cutoverFreqs.size-2].do {|cutoverFreq, i|
				whichSample = whichSample + (i * (freq >= cutoverFreq) * (freq < cutoverFreqs[i+1]));
			};
			Select.kr(whichSample, buffersAndFreqs);
		}];


		/*

		// TO DO... could use pairsDo to simplify this...
		// TO DO... move this logic to SampleDataLibrary
		// adds the cutover frequencies as third element to each array, if doesn't already exist (except for the last array);
		(mySampleData.data.size).do{ |i|
			var mySample = mySampleData.data[i];
			mySample[0] = mySample[0].bufnum;
			if (mySample.size < 3 && (i+1) < mySampleData.data.size, {
				mySample.add(mySample[1] + ((mySampleData.data[i+1][1] - mySample[1]) / 2) );
			});
		};

		// TO DO replace myS with args references
		// TO DO? ...could use Select to simply the below?
		// gets the appropriate sample data element based on frequency
		myS.getSample = {arg myS, freq;
			var mySample = myS.sampleData.data[0] * (freq < myS.sampleData.data[0][2]);
			(myS.sampleData.data.size-2).do{ |i|
				mySample = mySample + (myS.sampleData.data[i+1] * (freq >= myS.sampleData.data[i][2]) * (freq < myS.sampleData.data[i+1][2]));
			};
			mySample = mySample + (myS.sampleData.data[myS.sampleData.data.size-2] * (freq >= myS.sampleData.data[myS.sampleData.data.size-2][2]));
		};*/

		^super.make(name, *args);
	}


}


/*(
var buffersAndFreqs = [[0,110], [11,220], [22,440], [33,880], [44,1760]];

var soundFreqs = [110, 220, 440, 880, 1760];
var cutoverFreqs = soundFreqs[..soundFreqs.size-2].collect {arg freq, i;
	freq + ((soundFreqs[i+1] - freq) / 2);
};

var args = [\getSample, {arg selfKwargs, freq;
	var whichSample = 0;
	cutoverFreqs[..cutoverFreqs.size-2].do {|cutoverFreq, i|
		// [cutoverFreq, i, i*(freq >= cutoverFreq)].postln;
		whichSample = whichSample + (i * (freq >= cutoverFreq) * (freq < cutoverFreqs[i+1]));
	};
	Select.kr(2, buffersAndFreqs);
}];

var kwargs = args.asDict;
kwargs.know = true;

kwargs.getSample(440);

)*/



/*
a = ["aa","bb","Cc","dd"];
a[..a.size-2].do {|a1,i| [a1,i].postln;};*/