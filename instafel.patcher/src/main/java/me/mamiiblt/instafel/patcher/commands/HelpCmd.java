package me.mamiiblt.instafel.patcher.commands;

import me.mamiiblt.instafel.patcher.cmdhandler.Command;

public class HelpCmd implements Command {

    @Override
    public void execute(String[] args) {
        String[] lines = {
            "usage:",
            "  help - Shows help command",
            "  version - Shows patcher, baksmali version etc.",
            "  list - Lists available patches",
            "  info - Shows patch info details",
            "  cwdir <apk file> - Creates an workdir with Instagram APK",
            "  run <dir> <patch name> - Run selected patch",
            "",
            "For guide, visit this link,",
            "https://instafel.mamiiblt.me/guide/patcher-usage"
        };

        for (String line : lines) {
            System.out.println(line);
        }
    }
}
