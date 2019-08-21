#!/bin/bash
while read LINE; do
	IFS=',' read -ra ARR <<< "$LINE"
	filename="${ARR[0]}.js"
	effect="${ARR[2]}"
	duration="${ARR[3]}"
	amp="${ARR[4]//$'\r'}"

	cat > "$filename" << EOF
var DonorPlayer = Java.type('org.realcodingteam.plan9.data.DonorPlayer');
var PotionEffect = Java.type('org.bukkit.potion.PotionEffect');
var PotionEffectType = Java.type('org.bukkit.potion.PotionEffectType');
function run() {
	SERVER.getOnlinePlayers().forEach(function(p) {
		var dp = DonorPlayer.getDonorPlayer(p.getUniqueId());
		if(!dp.receivesEffects()) return;
		p.addPotionEffect(new PotionEffect(PotionEffectType.${effect}, ${duration}, ${amp//$'\r'}, true, true, true), true);
	});
}
EOF
done < "types.csv"
