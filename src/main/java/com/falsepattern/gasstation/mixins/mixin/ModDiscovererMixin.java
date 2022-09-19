package com.falsepattern.gasstation.mixins.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.launchwrapper.Launch;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.discovery.ModDiscoverer;

import java.io.File;
import java.util.List;

@Mixin(value = ModDiscoverer.class,
       remap = false)
public abstract class ModDiscovererMixin {
    @Shadow private List<ModCandidate> candidates;

    @Inject(method = "findClasspathMods",
            at = @At(value = "INVOKE",
                     target = "Lcpw/mods/fml/common/FMLLog;finer(Ljava/lang/String;[Ljava/lang/Object;)V"),
            locals = LocalCapture.CAPTURE_FAILHARD,
            require = 1)
    private void smartCheck(ModClassLoader modClassLoader, CallbackInfo ci, List<String> knownLibraries, File[] minecraftSources, int i) {
        if ((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment")) {
            File source = minecraftSources[i];
            if (source.getName().contains("00gasstation")) {
                FMLLog.fine("Found a minecraft related file at %s, examining for mod candidates", minecraftSources[i].getAbsolutePath());
                candidates.add(new ModCandidate(minecraftSources[i], minecraftSources[i], ContainerType.JAR, i == 0, true));
                return;
            }
        }
        FMLLog.finer("Skipping known library file %s", minecraftSources[i].getAbsolutePath());
    }

    @Redirect(method = "findClasspathMods",
              at = @At(value = "INVOKE",
                       target = "Lcpw/mods/fml/common/FMLLog;finer(Ljava/lang/String;[Ljava/lang/Object;)V"),
              require = 1)
    private void noLog(String format, Object[] data) {

    }
}
