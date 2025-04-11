package me.mamiiblt.instafel.patcher.commands;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;

public class HelpCmd implements Command {

    @Override
    public void execute(String[] args) {
        String[] lines = {
            "usage:",
            "  help - Shows help command",
            "  about - Shows patcher, baksmali version etc.",
            "  list - Lists available patches",
            "  info - Shows patch info details",
            "  init <apk file> - Creates an workdir with Instagram APK",
            "  run <dir> <patch name> - Run selected patch",
            "  build <dir> - Bild selected workdir",
            "",
            "For guide about patcher, visit this link,",
            "https://github.com/mamiiblt/instafel"
        };

        for (String line : lines) {
            System.out.println(line);
        }
    }
}
