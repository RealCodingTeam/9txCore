Scripting: Some menus effects are entirely configurable.

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

===========================================
Scripts:
===========================================

Scripts are jdk8 nashorn javascript files located in the plugin's scripts directory.

A valid script must define a function called run. The run function is what is called when the effect is purchased by a player.
The run function is passed two arguments: The player who purchased the effect and the cost of the effect.
The cost of the effect can be used to determine what tier of an effect was purchased. This can be used to allow multiple effects to allow one script.

A "shouldShow" function may optionally be defined that determines if the effect should be displayed in the menu.

An "init" function may optionally be defined. The init function is called one time when a script is loaded.
This is where you should define your event handlers.
If multiple effects are set to use the same script, you should make a variable that prevents the event handler from being loaded more than once:

function init() {
	if(CTX.getVar("UniqueVarName") != null) return;
	CTX.setVar("UniqueVarName", true);
}

Sample:

(scripts/misc/hello.js)
<script>
//required for the effect to run
function run(player, cost) {
	SERVER.broadcastMessage("Thank you " + player.getName() + " for spending " + cost + " DP!");
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

Any changes made to scripts are updated as you save, you do not need to reload for changes to be reflected.*
Changes made to menus, on the other hand, are not updated live. To update the menus, run /txrl or any of the aliases for the command.

*If you use the init function or register event handlers, you must run /txrl for the scripts to be updated.

Global bindings:
	* Events => Map<String, String> (shorthand names for FQNs of Bukkit event classes)
	* SERVER => Bukkit.getServer()
	* CTX => A script context that provides access to registering event handlers and the global variable table

Function signatures:
	* init()
	* run(player, cost)
	* shouldShow()

===========================================
Variables:
===========================================

All scripts have access to a global variable table. This table is shared across all scripts.
To set a variable, do:
CTX.setVar(key, value)

key = variable name (string)
value = the value of the variable (object)

Setting a variable to null will remove the variable.

To get a variable, do:
CTX.getVar(key)

getVar will return either null if the variable doesn't exist, or the value the variable is set to.

===========================================
Event handlers
===========================================

Scripts may register bukkit event handlers using CTX.onEvent(String event, Function handler)
It is recommended to only register events in the init function of your script.
If multiple effects use one script that registers events, make sure the init function has a variable gate that early returns before registering the event handler again.

event:  fully qualified class name of a subclass of org.bukkit.event.Event
	Scripts have access to a shortcut for this called Events.
	Events provides properties that shortcut to event names:
	Events.BlockBreakEvent => org.bukkit.event.block.BlockBreakEvent

handler: function that will process the event

Sample:
<script>
//called one time when the effect is loaded
function init() {
	CTX.onEvent(Events.AsyncPlayerChatEvent, function(event) {
		event.getPlayer().sendMessage("Thanks for contributing to chat!");
	});
}

//The run function is required, even if it does nothing.
//This script is unusual in that it only has an event handler and no logic.
//If you have an event handler, the script should have optional behavior
//regarding the event handler based on circumstances you design, i.e.
//an effect timer that is added to whenever the effect is purchased,
//changing how the event acts based on if the timer is active or not.
function run() {}
</script>

Upcoming:
 * User defined menus
 * Add fields to menu configs to allow custom sizing and titles
 * Make main menu configurable to add user defined menus