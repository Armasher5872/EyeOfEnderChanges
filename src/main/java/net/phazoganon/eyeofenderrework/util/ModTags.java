package net.phazoganon.eyeofenderrework.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.phazoganon.eyeofenderrework.EyeOfEnderRework;

public class ModTags {
    public static class Structures {
        public static final TagKey<Structure> BASTION = create("bastion");
        public static final TagKey<Structure> END_CITIES = create("end_cities");
        private static TagKey<Structure> create(String name) {
            return createStructure(ResourceLocation.fromNamespaceAndPath(EyeOfEnderRework.MODID, name));
        }
        private static TagKey<Structure> createStructure(ResourceLocation name) {
            return TagKey.create(Registries.STRUCTURE, name);
        }
    }
}