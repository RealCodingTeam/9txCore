function run() {
	SERVER.getOnlinePlayers().forEach(function(p) {
		p.setSaturation(20.0);
		p.setFoodLevel(20);
	});
}
