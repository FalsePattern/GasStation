package com.falsepattern.gasstation.mixins.mixin.regex;

import com.falsepattern.gasstation.mixins.Helper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import cpw.mods.fml.common.discovery.DirectoryDiscoverer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(value = DirectoryDiscoverer.class,
       remap = false)
public abstract class DirectoryDiscovererMixin {
    private String fileName;

    @Redirect(method = "exploreFileSystem",
              at = @At(value = "INVOKE",
                       target = "Ljava/util/regex/Pattern;matcher(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;"),
              require = 1)
    private Matcher noMatcher(Pattern instance, CharSequence charSequence) {
        fileName = charSequence.toString();
        return null;
    }

    @Redirect(method = "exploreFileSystem",
              at = @At(value = "INVOKE",
                       target = "Ljava/util/regex/Matcher;matches()Z"),
              require = 1)
    private boolean fastMatch(Matcher instance) {
        return Helper.classFileRegex(fileName);
    }
}
