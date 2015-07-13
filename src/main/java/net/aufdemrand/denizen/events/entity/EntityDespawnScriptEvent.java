package net.aufdemrand.denizen.events.entity;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizencore.events.ScriptEvent;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;

import java.util.HashMap;

public class EntityDespawnScriptEvent extends ScriptEvent {

    // <--[event]
    // @Events
    // entity despawns
    // <entity> despawns
    //
    // @Warning this event fires very rapidly.
    //
    // @Switch cause death|chunk_unload|other
    //
    // @Triggers when an entity despawns permanently from the world. May fire repeatedly for one entity.
    //
    // @Context
    // <context.entity> returns the entity that despawned.
    // <context.cause> returns the reason the entity despawned. Can be: DEATH, CHUNK_UNLOAD, or OTHER
    //
    // -->


    public EntityDespawnScriptEvent() {
        instance = this;
    }

    public static EntityDespawnScriptEvent instance;
    public dEntity entity;
    public Element cause;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        return CoreUtilities.xthArgEquals(1, CoreUtilities.toLowerCase(s), "despawns");
    }

    @Override
    public boolean matches(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return entity.matchesEntity(CoreUtilities.getXthArg(0, s))
                && checkSwitch(lower, "cause", CoreUtilities.toLowerCase(cause.asString()));
    }

    @Override
    public String getName() {
        return "EntityDespawn";
    }

    @Override
    public void init() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(entity.isPlayer() ? dEntity.getPlayerFrom(entity.getBukkitEntity()) : null,
                entity.isCitizensNPC() ? dEntity.getNPCFrom(entity.getBukkitEntity()) : null);
    }

    @Override
    public HashMap<String, dObject> getContext() {
        HashMap<String, dObject> context = super.getContext();
        context.put("entity", entity);
        context.put("cause", cause);
        return context;
    }
}
