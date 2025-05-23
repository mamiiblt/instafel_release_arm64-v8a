package me.mamiiblt.instafel.patcher.commands;

import brut.androlib.ApktoolProperties;
import me.mamiiblt.instafel.patcher.utils.Env;
import me.mamiiblt.instafel.patcher.utils.cmdhandler.Command;

public class AboutCmd implements Command {

    @Override
    public void execute(String[] args) {
        String[] helpLines = {
            "Instafel Patcher\nPatch Instagram alpha APKs fastly!\n",
            "version    : v" + Env.PROP_VERSION_STRING + " (" + Env.PROP_PROJECT_TAG + ")",
            "commit     : " + Env.PROP_COMMIT_HASH + "/main",
            "apktool    : v" + ApktoolProperties.getVersion(),
            "baksmali   : v" + ApktoolProperties.getBaksmaliVersion(),
            "telegram   : t.me/instafel",
            "repository : github.com/mamiiblt/instafel",
            "",
            "Developed with ❤️ by mamiiblt"
        };

        for (String line : helpLines) {
            System.out.println(line);
        }
    }
}
