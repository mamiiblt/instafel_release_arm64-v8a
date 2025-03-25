package me.mamiiblt.instafel.patcher.commands;

import brut.androlib.ApktoolProperties;
import me.mamiiblt.instafel.patcher.cmdhandler.Command;
import me.mamiiblt.instafel.patcher.utils.Environment;

public class VersionCmd implements Command {

    @Override
    public void execute(String[] args) {
        String[] helpLines = {
            "Instafel Patcher\nby mamiiblt\n",
            "version    : " + Environment.PROP_VERSION_STRING + " (" + Environment.PROP_PROJECT_TAG + ")",
            "commit     : " + Environment.PROP_COMMIT_HASH,
            "apktool    : " + ApktoolProperties.getVersion(),
            "baksmali   : " + ApktoolProperties.getBaksmaliVersion(),
            "telegram   : t.me/instafel",
            "repository : github.com/mamiiblt/instafel",
            "",
            "This project is licensed under the MIT license"
        };

        for (String line : helpLines) {
            System.out.println(line);
        }
    }
}
