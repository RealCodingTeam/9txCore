function run() {
	SERVER.getWorld("world").setStorm(false);
}

function shouldShow() {
	return SERVER.getWorld("world").hasStorm();
}
