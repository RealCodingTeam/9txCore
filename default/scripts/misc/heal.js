function run() {
	SERVER.getOnlinePlayers().forEach(function(p) {
		if(p.isDead()) return;
		p.setHealth(p.getMaxHealth());
	});
}
