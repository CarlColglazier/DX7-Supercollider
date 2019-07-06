s.boot;

s.waitForBoot({
	~mainCaller = ("./DX7.scd").loadRelative.wrapAt(-1);
	MIDIClient.init;
	~preset = 0;
	(
		// https://gist.github.com/umbrellaprocess/973d2aa16e95bf329ee2
		//var keys;
		//keys = Array.newClear(128);
		~sustain = false;

		~noteOnFunc = {arg val, num, chan, src;
			~mainCaller.value(num, val, ~preset);
		};

		MIDIdef.noteOn(\on, ~noteOnFunc);

		MIDIdef.cc(\test1, {arg val, x, y, time;
			~sustain = (val == 127);
			/*
			if (val == 0) {
			127.do({arg i;
			~mainCaller.value(i, 0);
			});
			}*/
		}); // match cc 1

		~noteOffFunc = {arg val, num, chan, src;
			if (~sustain == false) {
				~mainCaller.value(num, val);
			}
		};

		MIDIdef.noteOff(\off, ~noteOffFunc);
	)
})