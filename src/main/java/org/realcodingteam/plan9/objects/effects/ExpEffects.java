package org.realcodingteam.plan9.objects.effects;

import org.bukkit.Bukkit;

public final class ExpEffects extends Effects {

	public static final ExpEffects TIER_1 = new ExpEffects(15, 160);
	public static final ExpEffects TIER_2 = new ExpEffects(30, 550);
	public static final ExpEffects TIER_3 = new ExpEffects(50, 1395);
	
	protected ExpEffects(int cost, int xp) {
		super(cost);
		
		effect = () -> Bukkit.getOnlinePlayers().forEach(p -> p.giveExp(xp));
	}
	
	public static ExpEffects fromAmount(int i) {
		switch(i) {
			case 2:  return TIER_2;
			case 3:  return TIER_3;
			default: return TIER_1;
		}
	}
}
