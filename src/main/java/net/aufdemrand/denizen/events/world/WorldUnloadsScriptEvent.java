package net.aufdemrand.denizen.events.world;


import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.HashMap;

public class WorldUnloadsScriptEvent extends ScriptEvent implements Listener {

    // <--[event]
    // @Events
    // world unloads
    // <world> unloads
    //
    // @Cancellable true
    //
    // @Triggers when a world is unloaded.
    //
    // @Context
    // <context.world> returns the dWorld that was unloaded.
    //
    // -->

    public WorldUnloadsScriptEvent() {
        instance = this;
    }

    public static WorldUnloadsScriptEvent instance;
    public dWorld world;
    public WorldUnloadEvent event;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.getXthArg(1,CoreUtilities.toLowerCase(s)).equals("unloads");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String wCheck = CoreUtilities.getXthArg(0,CoreUtilities.toLowerCase(s));
        if (!wCheck.equals("world") && !wCheck.equals(CoreUtilities.toLowerCase(world.getName()))) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "WorldUnloads";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        WorldUnloadEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public HashMap<String, dObject> getContext() {
        HashMap<String, dObject> context = super.getContext();
        context.put("world", world);
        return context;
    }

    @EventHandler
    public void onWorldUnloads(WorldUnloadEvent event) {
        world = new dWorld(event.getWorld());
        cancelled = event.isCancelled();
        this.event = event;
        fire();
        event.setCancelled(cancelled);
    }
}
