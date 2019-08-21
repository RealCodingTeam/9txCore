var DonorPlayer = Java.type('org.realcodingteam.plan9.data.DonorPlayer');
var PotionEffect = Java.type('org.bukkit.potion.PotionEffect');
var PotionEffectType = Java.type('org.bukkit.potion.PotionEffectType');
function run() {
	SERVER.getOnlinePlayers().forEach(function(p) {
		var dp = DonorPlayer.getDonorPlayer(p.getUniqueId());
		if(!dp.receivesEffects()) return;
		p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30 * 20, 1, true, true, true), true);
	});
}
