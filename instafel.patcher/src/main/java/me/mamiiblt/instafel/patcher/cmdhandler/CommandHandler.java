package me.mamiiblt.instafel.patcher.cmdhandler;

import java.util.HashMap;
import java.util.Map;

import me.mamiiblt.instafel.patcher.commands.BuildProject;
import me.mamiiblt.instafel.patcher.commands.HelpCmd;
import me.mamiiblt.instafel.patcher.commands.InitProject;
import me.mamiiblt.instafel.patcher.commands.ListPatches;
import me.mamiiblt.instafel.patcher.commands.RunPatch;
import me.mamiiblt.instafel.patcher.commands.VersionCmd;
import me.mamiiblt.instafel.patcher.utils.Environment;

public class CommandHandler {
    
    private final Map<String, Command> commands = new HashMap<>();

    public CommandHandler(String[] args) {
        registerCommands();
        setupHandler(args);
    }

    private void setupHandler(String[] args) {
        if (args.length == 0) {
            Environment.printPatcherHeader();
            System.out.println("Use `help` command for list all commands.");
        } else {
            String command = args[0];
            if (!command.equals("version")) {
                Environment.printPatcherHeader();
            }
            String[] commandArgs = new String[args.length - 1];
            System.arraycopy(args, 1, commandArgs, 0, args.length - 1);
            executeCommand(command, commandArgs);
        }
    }

    private void executeCommand(String commandName, String[] args) {
        Command command = commands.get(commandName);
        if (command != null) {
            command.execute(args);
        } else {
            System.out.println("Unkown command, use `help` for list all commands.");
        }
    }

    private void registerCommands() {
        commands.put("run", new RunPatch());
        commands.put("list", new ListPatches());
        commands.put("cwith", new InitProject());
        commands.put("build", new BuildProject());
        commands.put("version", new VersionCmd());
        commands.put("help", new HelpCmd());
    }
}
