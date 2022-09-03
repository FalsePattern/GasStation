package io.github.tox1cozz.mixinbooterlegacy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;

/**
 * This is here for mixin-booter-legacy backwards compat
 */
@SuppressWarnings("unused")
public class MixinBooterLegacyPlugin {
    public static final Logger LOGGER = LogManager.getLogger("MixinBooter");

    @Mod(modid = "mixinbooterlegacy",
         version = "1.1.2",
         name = "MixinBooterLegacy",
         acceptableRemoteVersions = "*")
    public static class Container {

    }
}
