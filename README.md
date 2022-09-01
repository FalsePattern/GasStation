# SpongeMixins Library Mod
Mod that allows you to use mixins in 1.7.10

## How to setup library in dev workspace:
1. Put `SpongeMixins-dev.jar` in the %project%/libs folder and refresh gradle.<br><br>
1.1. Add this string to the dependencies in your main class of mod: `required-after:spongemixins@[1.1.0,);`<br><br>
2. Add this stuff to your `build.gradle`:<p>

&emsp;&emsp;2.1 Setup your workspace by including this script at the end of your `build.gradle` and replace strings `yourMixinConfig`, `refMapForYourConfig` and `relativePathToMixinAP` as represented in the comments near them:
```groovy
//##########################################################################################################
//########################################    Mixin  Part   ################################################
//##########################################################################################################
/**
* The name of your mixin config. Should match the name of mixin config file, which you placed in src/main/resources/
*/
def yourMixinConfig = 'mixins.yourmodid.json'
/**
* The file with this name will be generated during build. Should math the remap value inside your src/main/resources/*yourMixinConfig*
*/
def refMapForYourConfig = 'mixins.yourmodid.refmap.json'
/**
* Relative path to the new annotation processor. 
* You should download it (https://github.com/TimeConqueror/SpongeMixins/raw/master/mixin-0.8-SNAPSHOT.jar) and then place to the project folder. 
* You may also place it in the inner folders, only thing you should to do is to provide right RELATIVE path.
*/
def relativePathToMixinAP = 'mixin-0.8-SNAPSHOT.jar'

repositories {
    maven {
        name = 'sponge'
        url = 'https://repo.spongepowered.org/repository/maven-public'
    }
}

dependencies {
    compile('org.spongepowered:mixin:0.7.11-SNAPSHOT') {
        // Mixin includes a lot of dependencies that are too up-to-date
        exclude module: 'launchwrapper'
        exclude module: 'guava'
        exclude module: 'gson'
        exclude module: 'commons-io'
        exclude module: 'log4j-core'
    }
}

def refMap = "${tasks.compileJava.temporaryDir}" + File.separator + refMapForYourConfig

def mixinSrg = "${tasks.reobf.temporaryDir}" + File.separator + "mixins.srg"

jar {
    from refMap
    manifest {
        attributes([
                "TweakClass": "org.spongepowered.asm.launch.MixinTweaker",
                'MixinConfigs': yourMixinConfig,
                'FMLCorePluginContainsFMLMod': 'true',
                "ForceLoadAsMod": true
        ])
    }
}

reobf {
    addExtraSrgFile mixinSrg
}

afterEvaluate {
    def fixedRelPathToAP = relativePathToMixinAP
    if(fixedRelPathToAP.startsWith('./') || fixedRelPathToAP.startsWith('.\\')){
        fixedRelPathToAP = fixedRelPathToAP.substring(2)
    } else if(fixedRelPathToAP.startsWith('/') || fixedRelPathToAP.startsWith('\\')){
        fixedRelPathToAP = fixedRelPathToAP.substring(1)
    }

    tasks.compileJava {
        println "Path: " + projectDir.absolutePath
        options.compilerArgs += [
                // There's a bug in the AnnotationProcessor for 0.7.11 that will generate the annotations pointing to the parent class instead of subclass
                // resulting in the mixin not being applied.  This is fixed in 0.8, however 0.8 needs guava > 21.0, and minecraft ships with 17.0.
                // So as a hacky workaround... ship with 0.7.11, but use the AP from 0.8 for compiling
                "-processorpath", projectDir.absolutePath + '/' + fixedRelPathToAP,
                "-processor", "org.spongepowered.tools.obfuscation.MixinObfuscationProcessorInjection,org.spongepowered.tools.obfuscation.MixinObfuscationProcessorTargets",
                "-Xlint:-sunapi", "-XDenableSunApiLintControl", "-XDignore.symbol.file",
                "-AreobfSrgFile=${tasks.reobf.srg}", "-AoutSrgFile=${mixinSrg}", "-AoutRefMapFile=${refMap}"
        ]
    }
}

sourceSets {
    main {
        output.resourcesDir = output.classesDir
        ext.refMap = refMapForYourConfig
    }
}
```

## Starting magic with mixins or how to add its config
Create `mixins.yourmodid.json` file directly in resources folder (replace `yourmodid` with your modid). 
Here is an example of how to fill it (**remember about changing `yourmodid`**): 
```json
{
  "required": true,
  "minVersion": "0.7.11",
  "package": "com.company.mypackage.mixins",
  "refmap": "mixins.yourmodid.refmap.json",
  "target": "@env(DEFAULT)",
  "compatibilityLevel": "JAVA_8",
  "mixins": [
    "MixinTest1",
    "MixinTest2"
  ],
  "client": [
    "MixinMinecraft"
  ]
}
```
More info here: https://github.com/SpongePowered/Mixin

## How to launch in dev-workspace

Add following Program Agruments:<br> `--tweakClass org.spongepowered.asm.launch.MixinTweaker --mixin mixins.yourmodid.json`<br> and replace `yourmodid` as mentioned above.

## FAQ
1. Q: `project\build\tmp\reobf\mixins.srg (Can't find given file)` while build<br>
A: You should run minecraft after you created or added new mixins to generate this file
