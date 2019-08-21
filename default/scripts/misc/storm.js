function run() {
	SERVER.getWorld("world").setStorm(true);
	SERVER.getWorld("world").setThundering(true);
}

function shouldShow() {
	return !SERVER.getWorld("world").hasStorm();
}
