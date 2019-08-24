package org.realcodingteam.plan9.inv.scripts;

import java.util.*;

import org.bukkit.event.Event;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.*;

public final class EventRegistrar {
    private static final List<Class<? extends Event>> EVENT_CLASSES = Collections.unmodifiableList(Arrays.asList(
            AsyncPlayerChatEvent.class, AsyncPlayerPreLoginEvent.class, BlockBreakEvent.class, BlockBurnEvent.class,
            BlockCanBuildEvent.class, BlockDamageEvent.class, BlockDispenseEvent.class, BlockFormEvent.class,
            BlockFadeEvent.class, BlockFromToEvent.class, BlockGrowEvent.class, BlockIgniteEvent.class,
            BlockPhysicsEvent.class, BlockPistonExtendEvent.class, BlockPistonRetractEvent.class, BlockPlaceEvent.class,
            BlockRedstoneEvent.class, BlockSpreadEvent.class, BrewEvent.class, ChunkLoadEvent.class,
            ChunkPopulateEvent.class, ChunkUnloadEvent.class, CraftItemEvent.class, CreatureSpawnEvent.class,
            CreeperPowerEvent.class, EnchantItemEvent.class, EntityBlockFormEvent.class, EntityBreakDoorEvent.class,
            EntityChangeBlockEvent.class, EntityCombustByBlockEvent.class, EntityCombustByEntityEvent.class,
            EntityCombustEvent.class, EntityDamageByBlockEvent.class, EntityDamageByEntityEvent.class,
            EntityDamageEvent.class, EntityDeathEvent.class, EntityExplodeEvent.class, EntityInteractEvent.class,
            EntityPortalEnterEvent.class, EntityPortalEvent.class, EntityPortalExitEvent.class,
            EntityRegainHealthEvent.class, EntityShootBowEvent.class, EntityTameEvent.class, EntityTargetEvent.class,
            EntityTargetLivingEntityEvent.class, EntityTeleportEvent.class, EntityUnleashEvent.class,
            ExpBottleEvent.class, ExplosionPrimeEvent.class, FoodLevelChangeEvent.class, FurnaceBurnEvent.class,
            FurnaceSmeltEvent.class, HangingBreakByEntityEvent.class, HangingBreakEvent.class, HangingPlaceEvent.class,
            HorseJumpEvent.class, InventoryClickEvent.class, InventoryCloseEvent.class, InventoryCreativeEvent.class,
            InventoryDragEvent.class, InventoryClickEvent.class, InventoryDragEvent.class, TradeSelectEvent.class,
            InventoryMoveItemEvent.class, InventoryOpenEvent.class, InventoryPickupItemEvent.class,
            ItemDespawnEvent.class, ItemSpawnEvent.class, LeavesDecayEvent.class, LightningStrikeEvent.class,
            MapInitializeEvent.class, NotePlayEvent.class, PigZapEvent.class, PlayerAnimationEvent.class,
            PlayerBedEnterEvent.class, PlayerBedLeaveEvent.class, PlayerBucketEmptyEvent.class,
            PlayerBucketFillEvent.class, PlayerChangedWorldEvent.class, PlayerCommandPreprocessEvent.class,
            PlayerDeathEvent.class, PlayerDropItemEvent.class, PlayerEditBookEvent.class, PlayerEggThrowEvent.class,
            PlayerExpChangeEvent.class, PlayerFishEvent.class, PlayerGameModeChangeEvent.class,
            PlayerInteractAtEntityEvent.class, PlayerInteractEntityEvent.class, PlayerInteractEvent.class,
            PlayerItemBreakEvent.class, PlayerItemConsumeEvent.class, PlayerItemHeldEvent.class, PlayerJoinEvent.class,
            PlayerKickEvent.class, PlayerLeashEntityEvent.class, PlayerLevelChangeEvent.class, PlayerLoginEvent.class,
            PlayerMoveEvent.class, PlayerPortalEvent.class, PlayerQuitEvent.class, PlayerRegisterChannelEvent.class,
            PlayerRespawnEvent.class, PlayerShearEntityEvent.class, PlayerStatisticIncrementEvent.class,
            PlayerTeleportEvent.class, PlayerToggleFlightEvent.class, PlayerToggleSneakEvent.class,
            PlayerToggleSprintEvent.class, PlayerUnleashEntityEvent.class, PlayerUnregisterChannelEvent.class,
            PlayerVelocityEvent.class, PluginDisableEvent.class, PluginEnableEvent.class, PortalCreateEvent.class,
            PotionSplashEvent.class, PrepareItemCraftEvent.class, PrepareItemEnchantEvent.class,
            ProjectileHitEvent.class, ProjectileLaunchEvent.class, RemoteServerCommandEvent.class,
            ServerCommandEvent.class, ServerListPingEvent.class, ServiceRegisterEvent.class,
            ServiceUnregisterEvent.class, SheepDyeWoolEvent.class, SheepRegrowWoolEvent.class, SignChangeEvent.class,
            SlimeSplitEvent.class, SpawnChangeEvent.class, StructureGrowEvent.class, ThunderChangeEvent.class,
            VehicleBlockCollisionEvent.class, VehicleCreateEvent.class, VehicleDamageEvent.class,
            VehicleDestroyEvent.class, VehicleEnterEvent.class, VehicleEntityCollisionEvent.class,
            VehicleExitEvent.class, VehicleMoveEvent.class, VehicleUpdateEvent.class, WeatherChangeEvent.class,
            WorldInitEvent.class, WorldLoadEvent.class, WorldSaveEvent.class, WorldUnloadEvent.class));

    protected static final Map<String, String> names;

    static {
        Map<String, String> namesmut = new HashMap<>();

        EVENT_CLASSES.forEach(event -> {
            namesmut.put(event.getSimpleName(), event.getCanonicalName());
        });

        names = Collections.unmodifiableMap(namesmut);
    }
}
