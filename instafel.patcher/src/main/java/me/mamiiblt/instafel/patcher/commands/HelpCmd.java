package me.mamiiblt.instafel.patcher.commands;

import me.mamiiblt.instafel.patcher.utils.cmdhandler.Command;

public class HelpCmd implements Command {

    @Override
    public void execute(String[] args) {
        String[] lines = {
            "usage:",
            "  help                      Shows the list of available commands",
            "  about                     Displays patcher info (version, apktool, etc.)",
            "  list                      Lists all available patches",
            "  init <apk file>           Initializes a working directory with the Instagram APK",
            "  run <wdir> <patch name>   Applies the specified patch to the working directory",
            "  build <wdir>              Builds the modified APK from the working directory",
            "",
            "For a complete guide on using the patcher, visit:",
            "https://github.com/mamiiblt/instafel"
        };

        for (String line : lines) {
            System.out.println(line);
        }
    }
}
