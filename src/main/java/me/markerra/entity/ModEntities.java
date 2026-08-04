package me.markerra.entity;

import me.markerra.NektoMod;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    // 1. entity identifier
    public static final Identifier NPC_ID = Identifier.fromNamespaceAndPath(NektoMod.MOD_ID, "npc");

    // 2. registry key
    public static final ResourceKey<EntityType<?>> NPC_KEY = ResourceKey.create(Registries.ENTITY_TYPE, NPC_ID);

    // 3. entity register
    public static final EntityType<NpcEntity> NPC = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            NPC_KEY,
            EntityType.Builder.of(NpcEntity::new, MobCategory.MISC)
                    .sized(0.6F, 1.8F)
                    .build(NPC_KEY)
    );

    public static void registerModEntities() {

    }
}