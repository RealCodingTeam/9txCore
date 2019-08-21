function run() {
	SERVER.getWorld("world").setTime(1000);
}

function shouldShow() {
	var time = SERVER.getWorld("world").getTime();
	return time >= 13050 || time < 1000;
}
