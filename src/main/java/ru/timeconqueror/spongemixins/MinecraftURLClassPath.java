/**
 * Copyright 2020 TimeConqueror
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ru.timeconqueror.spongemixins;

import java.io.File;

/**
 * This is here for SpongeMixins backwards compat
 */
@SuppressWarnings("unused")
public final class MinecraftURLClassPath {
    public static File getJarInModPath(final String jarname) {
        return com.falsepattern.gasstation.MinecraftURLClassPath.getJarInModPath(jarname);
    }

    public static boolean findJarInClassPath(final String jarname) {
        return com.falsepattern.gasstation.MinecraftURLClassPath.findJarInClassPath(jarname);
    }

    public static void addJar(File pathToJar) throws Exception {
        com.falsepattern.gasstation.MinecraftURLClassPath.addJar(pathToJar);
    }

    private MinecraftURLClassPath() {
    }
}
