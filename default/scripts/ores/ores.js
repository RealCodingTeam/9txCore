var Instant = Java.type("java.time.Instant");
var GameMode = Java.type("org.bukkit.GameMode");
var Material = Java.type("org.bukkit.Material");
var ItemStack = Java.type("org.bukkit.inventory.ItemStack");
var Enchantment = Java.type("org.bukkit.enchantments.Enchantment");

function init() {
	if(CTX.getVar("DoubleOreEffectTime") != null) return;

	CTX.setVar("DoubleOreEffectTime", Instant.now().toEpochMilli());

	CTX.onEvent(Events.BlockBreakEvent, function(event) {
		if(!isDoubleSmelt()) return;
		if(!event.getBlock().getType().name().contains("ORE")) return;
		if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;

		var pick = event.getPlayer().getInventory().getItemInMainHand();
		var fortune = pick != null && pick.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS);
		
		event.getBlock().getDrops().forEach(function(item) {
			event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), smelt(item, fortune));
		});

		event.setDropItems(false);
	});
}

function run(player, cost) {
	var time = 0;
	switch(cost) {
		case 50:
			time = 60000;
			break;
		case 100:
			time = 120000;
			break;
		case 175:
			time = 240000;
			break;
	}
	
	if(isDoubleSmelt()) {
		var timer = CTX.getVar("DoubleOreEffectTime");
		timer += time;
		CTX.setVar("DoubleOreEffectTime", timer);
		return;
	}

	time += Instant.now().toEpochMilli();
	CTX.setVar("DoubleOreEffectTime", time);
}

function isDoubleSmelt() {
	return CTX.getVar("DoubleOreEffectTime") > Instant.now().toEpochMilli();
}

function smelt(item, fortune) {
	var amount = item.getAmount() * 2;
	if(fortune) amount = item.getAmount() * 3;

	item.setAmount(amount);

	if(item.getType() == Material.COAL_ORE) {
		return new ItemStack(Material.COAL, amount);
	}

	if(item.getType() == Material.IRON_ORE) {
		return new ItemStack(Material.IRON_INGOT, amount);
	}

	if(item.getType() == Material.GOLD_ORE) {
		return new ItemStack(Material.GOLD_INGOT, amount);
	}

	if(item.getType() == Material.LAPIS_ORE) {
		return new ItemStack(Material.LAPIS_LAZULI, amount);
	}

	if(item.getType() == Material.REDSTONE_ORE) {
		return new ItemStack(Material.REDSTONE, amount);
	}

	if(item.getType() == Material.DIAMOND_ORE) {
		return new ItemStack(Material.DIAMOND, amount);
	}

	if(item.getType() == Material.EMERALD_ORE) {
		return new ItemStack(Material.EMERALD, amount);
	}

	if(item.getType() == Material.NETHER_QUARTZ_ORE) {
		return new ItemStack(Material.QUARTZ, amount);
	}
	
	return item;
}
