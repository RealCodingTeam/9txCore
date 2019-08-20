Scripting: Some menus effects are entirely configurable.
Currently, two menus support scripting (Potions and Misc).

To customize either of these menus, edit the YAML config files found in plugins/9txCore/menus/

Sample:

(menus/misc_menu.yml)
<config>
items:
   sayHi:
      name: "\u00A76Say hello to everyone!"
      category: "Misc"
      dynamic: true
      slot: 0
      cost: 0
      amount: 1
      material: "EMERALD"
      lore:
         - "\u00A72Greetings"
      script: "misc/hello.js"
</config>
      
items is the root section of each menu, all entries must be in this section.
sayHi is dummy text, this can be whatever you'd like.
Each effect requires the following keys:
  - name: The name to give the menu entry in game
  - category: Either "misc" or "potions"
  - dynamic: Should the shouldShow function be called in order to dynamically show/hide this entry? this key is optional, and defaults to false.
  - slot: Where the item will be in the menu, 0 is the first slot
  - cost: How much the effect costs in DP (Donor Points)
  - amount: How many items are in the menu item stack
  - material: What material to use for the item in the menu
  - lore: Extra strings to add to the item in the menu. First line is used in broadcast.
  - script: path where the effect's script is located. Relative to plugins/9txCore/scripts/

Both the name and lore keys allow color codes using \u00A7 followed by the color code, i.e. "\u00A72Hello world" is dark green "Hello world".

Scripts:

Scripts are jdk8 nashorn javascript files located in the plugin's scripts directory.

A valid script must define a function called run. The run function is what is called when the effect is purchased by a player.
A "shouldShow" function may optionally be defined that determines if the effect should be displayed in the menu.
The Bukkit server is exposed to scripts in the SERVER global variable.

Sample:

(scripts/misc/hello.js)
<script>
//required for the effect to run
function run() {
	//loop through all online players and send a message
	SERVER.getOnlinePlayers().forEach(function(player) {
		player.sendMessage(Packages.org.bukkit.ChatColor.LIGHT_PURPLE + "Hello! :)");
	});
}

//optional, but useful if you want certain effects to be disabled at certain times
//this function will only be called if the dynamic key in the menu entry is set to true.
function shouldShow() {
	//only show this effect in the menu if more than 1 player is online
	return SERVER.getOnlinePlayers().size > 1;
}
</script>

Any changes made to scripts are updated as you save, you do not need to reload for changes to be reflected.
Changes made to menus, on the other hand, are not updated live. To update the menus, run /txrl or any of the aliases for the command.

Upcoming:
 * User defined menus
 * Remove category key from config entries, it's redundant
 * Bind the buyer of effects to a global variable BUYER
 * Add fields to menu configs to allow custom sizing and titles
 * Make main menu configurable to add user defined menus