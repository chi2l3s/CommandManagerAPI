package ru.amixoldev.api.commandmanagerapi.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.TabExecutor;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;

public abstract class LongCommandExecutor implements TabExecutor {
    private final List<LongCommandExecutor.SubCommandWrapper> subCommands = new ArrayList();

    protected void addSubCommand(SubCommand command, String[] aliases, Permission permission) {
        this.subCommands.add(new LongCommandExecutor.SubCommandWrapper(command, aliases, permission));
    }

    @Nullable
    protected LongCommandExecutor.SubCommandWrapper getWrapperFromLabel(String label) {
        Iterator var2 = this.subCommands.iterator();

        while(var2.hasNext()) {
            LongCommandExecutor.SubCommandWrapper wrapper = (LongCommandExecutor.SubCommandWrapper)var2.next();
            String[] var4 = wrapper.aliases;
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String alias = var4[var6];
                if (alias.equalsIgnoreCase(label)) {
                    return wrapper;
                }
            }
        }

        return null;
    }

    protected List<String> getFirstAliases() {
        List<String> result = new ArrayList();
        Iterator var2 = this.subCommands.iterator();

        while(var2.hasNext()) {
            LongCommandExecutor.SubCommandWrapper wrapper = (LongCommandExecutor.SubCommandWrapper)var2.next();
            String alias = wrapper.aliases[0];
            result.add(alias);
        }

        return result;
    }

    protected List<LongCommandExecutor.SubCommandWrapper> getSubCommands() {
        return this.subCommands;
    }

    public static class SubCommandWrapper {
        private final SubCommand command;
        private final String[] aliases;
        private final Permission permission;

        public SubCommandWrapper(SubCommand command, String[] aliases, Permission permission) {
            this.command = command;
            this.aliases = aliases;
            this.permission = permission;
        }

        public SubCommand getCommand() {
            return this.command;
        }

        public String[] getAliases() {
            return this.aliases;
        }

        public Permission getPermission() {
            return this.permission;
        }
    }
}