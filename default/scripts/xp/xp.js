var Instant = Java.type("java.time.Instant");

function init() {
	if(CTX.getVar("DoubleXpEffectTime") != null) return;

	CTX.setVar("DoubleXpEffectTime", Instant.now().toEpochMilli());

	CTX.onEvent(Events.PlayerExpChangeEvent, function(event) {
		if(!isDoubleExp()) return;
		event.setAmount(event.getAmount() * 2);
	});
}

function run(player, cost) {
	var time = 0;
	switch(cost) {
		case 15:
			time = 60000;
			break;
		case 30:
			time = 120000;
			break;
		case 50:
			time = 180000;
			break;
	}
	
	if(isDoubleExp()) {
		var timer = CTX.getVar("DoubleXpEffectTime");
		timer += time;
		CTX.setVar("DoubleXpEffectTime", timer);
		return;
	}

	time += Instant.now().toEpochMilli();
	CTX.setVar("DoubleXpEffectTime", time);
}

function isDoubleExp() {
	return CTX.getVar("DoubleXpEffectTime") > Instant.now().toEpochMilli();
}
