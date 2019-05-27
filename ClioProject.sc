
// TO DO: implement based on a library
ClioProject {

/*	*new { | func |
		^super.new.init(func);
	}*/

	showArgs1 { arg aa, bb, cc;
		(aa ++ bb ++ cc).postln;
	}


	showArgs2 { arg ...args;
		args.postln;
	}

}







