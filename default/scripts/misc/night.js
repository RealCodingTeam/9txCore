function run() {
	SERVER.getWorld("world").setTime(13050);
}

function shouldShow() {
	var time = SERVER.getWorld("world").getTime();
	return time < 13050 && time >= 1000;
}
