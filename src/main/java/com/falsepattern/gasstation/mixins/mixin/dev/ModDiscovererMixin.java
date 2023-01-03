package com.falsepattern.gasstation.mixins.mixin.dev;

import com.falsepattern.gasstation.mixins.IModDiscovererMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.discovery.ModDiscoverer;

import java.util.Arrays;
import java.util.List;

@Mixin(value = ModDiscoverer.class,
       remap = false)
public abstract class ModDiscovererMixin implements IModDiscovererMixin {
    @Shadow private List<ModCandidate> candidates;

    @Override
    public List<ModCandidate> getCandidates() {
        return candidates;
    }

    @ModifyVariable(method = "findClasspathMods",
            at = @At("STORE"),
            require = 1)
    private List<String> noKnownLibraries(List<String> original) {
        return Arrays.asList();
    }
}
