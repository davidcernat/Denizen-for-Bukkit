package net.aufdemrand.denizen.events.world;


import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dLocation;
import net.aufdemrand.denizen.objects.dMaterial;
import net.aufdemrand.denizen.objects.dWorld;
import net.aufdemrand.denizen.utilities.DenizenAPI;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dList;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

import org.bukkit.Bukkit;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

import java.util.HashMap;

public class StructureGrowsScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // structure grows (naturally/from bonemeal) (in area)
    // <structure> grows (naturally/from bonemeal) (in area)
    // plant grows (naturally/from bonemeal) (in area)
    // <plant> grows (naturally/from bonemeal) (in area)
    //
    // @Cancellable true
    //
    // @Triggers when a structure (a tree or a mushroom) grows in a world.
    //
    // @Context
    // <context.world> returns the dWorld the structure grew in.
    // <context.location> returns the dLocation the structure grew at.
    // <context.structure> returns an Element of the structure's type.
    // <context.blocks> returns a dList of all block locations to be modified.
    // <context.new_materials> returns a dList of the new block materials, to go with <context.blocks>.
    //
    // -->

    public StructureGrowsScriptEvent() {
        instance = this;
    }

    public static StructureGrowsScriptEvent instance;
    public dWorld world;
    public dLocation location;
    public Element structure;
    public dList blocks;
    public dList new_materials;
    public StructureGrowEvent event;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.getXthArg(1, CoreUtilities.toLowerCase(s)).equals("grows");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        String struct = CoreUtilities.getXthArg(0, lower);
        if (!struct.equals("structure") && !struct.equals("plant") &&
                !struct.equals(CoreUtilities.toLowerCase(structure.asString()))) {
            return false;
        }

        if (!runInCheck(scriptContainer, s, lower, location)) {
            return false;
        }
        if (CoreUtilities.getXthArg(2,lower).equals("from") && !event.isFromBonemeal()) {
            return false;
        } else if (CoreUtilities.getXthArg(2,lower).equals("naturally") && event.isFromBonemeal()) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return "StructureGrow";
    }

    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, DenizenAPI.getCurrentInstance());
    }

    @Override
    public void destroy() {
        StructureGrowEvent.getHandlerList().unregister(this);
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public HashMap<String, dObject> getContext() {
        HashMap<String, dObject> context = super.getContext();
        context.put("world", world);
        context.put("location", location);
        context.put("structure", structure);
        context.put("blocks", blocks);
        context.put("new_materials", new_materials);
        return context;
    }

    @EventHandler
    public void onStructureGrow(StructureGrowEvent event) {
        world = new dWorld(event.getWorld());
        location = new dLocation(event.getLocation());
        structure = new Element(event.getSpecies().name());
        blocks = new dList();
        new_materials = new dList();
        for (BlockState block : event.getBlocks()) {
            blocks.add(new dLocation(block.getLocation()).identify());
            new_materials.add(dMaterial.getMaterialFrom(block.getType(), block.getRawData()).identify());
        }
        this.event = event;
        cancelled = event.isCancelled();
        fire();
        event.setCancelled(cancelled);
    }
}
