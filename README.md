# projector-markdown-plugin
[![JetBrains incubator project](https://jb.gg/badges/incubator.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Tests status badge](https://github.com/JetBrains/projector-markdown-plugin/workflows/Tests/badge.svg)](https://github.com/JetBrains/projector-markdown-plugin/actions)

An IntelliJ plugin which provides a delegating Markdown preview.

For more info, please check out [Projector.md](https://github.com/JetBrains/projector-server/blob/master/docs/Projector.md).

## This project is obsolete
Since [PRJ-131](https://youtrack.jetbrains.com/issue/PRJ-131), MD Preview is bundled into projector-server, so we don't need a separate MD plugin anymore. It makes of projector installation even easier.

## Building
```shell script
./gradlew :buildPlugin
```

This will build the plugin here: `build/distributions/projector-markdown-plugin-VERSION.zip`.

## Running
For testing purposes, you can run this plugin with UI via
```shell script
./gradlew :runIde
```

To make this plugin useful, you should set up some callbacks. An example can be found in [projector-server](https://github.com/JetBrains/projector-server).

## Note
Since the bundled Markdown plugin doesn't support extensions now ([IDEA-235683](https://youtrack.jetbrains.com/issue/IDEA-235683)), we currently have to provide not only the preview, but also the full copy of the bundled plugin. So you have to **disable the bundled Markdown plugin** first and then enable this one to make it work.

## License
[MIT](LICENSE.txt).
