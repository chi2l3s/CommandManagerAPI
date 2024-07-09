# 💡 Использование

Как же использовать данный API? Представим ситуацию, когда вы пишите плагин для Minecraft и вам нужно создать команду у которой будет еще куча своих подкоманд, на помощь приходит `CommandManagerAPI`, подключите его и сэкономьте своё драгоценное время, вместо того чтобы прописывать свою логику снова и снова в каждом проекте.

Для начала создадим класс с главной командой, который будет наследоваться от `LongCommandExecutor` из нашего API.

```java
package me.chi2l3s.amixolenchants.command;

import me.chi2l3s.amixolenchants.command.list.EnchantItemCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.amixoldev.api.commandmanagerapi.command.LongCommandExecutor;

import java.util.List;

public class EnchantCommand extends LongCommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length < 1) return false;
        final SubCommandWrapper wrapper = getWrapperFromLabel(args[0]);
        if (wrapper == null) return false;

        if (!sender.hasPermission(wrapper.getPermission())) {
            sender.sendMessage(command.getPermissionMessage());
            return true;
        }

        wrapper.getCommand().onExecute(sender, args);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return getFirstAliases();
        }
        final SubCommandWrapper wrapper = getWrapperFromLabel(args[0]);
        if (wrapper == null) return null;

        return wrapper.getCommand().onTabComplete(sender, args);
    }

}

```

Хорошо, класс для главной команды создан, теперь перейдём к подкомандам. Для каждой подкоманды у нас будет отдельный класс который будет реализовать интерфейс SubCommand.

```java
package me.chi2l3s.amixolenchants.command.list;

import me.chi2l3s.amixolenchants.AmixolEnchants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.amixoldev.api.commandmanagerapi.command.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class EnchantItemCommand implements SubCommand {

    @Override
    public void onExecute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatColor.RED + "Вы должны быть игроком!");
            return;
        }
        if (strings.length < 2) {
            player.sendMessage("Введите название чара и уровень");
            return;
        }
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType().isAir()) {
            player.sendMessage("Вы должны держать в руках предмет!");
            return;
        }
        String enchant = strings[1];
        int lvl;
        try {
            lvl = Integer.parseInt(strings[2]);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Уровень должен быть числом!");
            return;
        }
        switch (enchant) {
            case "drill":
                itemStack.addUnsafeEnchantment(AmixolEnchants.DRILL, lvl);

                ItemMeta meta = itemStack.getItemMeta();
                if (meta != null) {
                    List<String> lore = meta.getLore();
                    if (lore == null) {
                        lore = new ArrayList<>();
                    }
                    Component displayName = AmixolEnchants.DRILL.displayName(lvl);
                    lore.add(ChatColor.GRAY + LegacyComponentSerializer.legacySection().serialize(displayName));
                    meta.setLore(lore);
                    itemStack.setItemMeta(meta);
                }

                player.sendMessage("Предмет зачарован Drill уровнем " + lvl + "!");
                break;
            default:
                player.sendMessage(ChatColor.RED + "Неизвестное зачарование: " + enchant);
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, String[] strings) {
        return List.of();
    }
}

```

Когда мы закончили с подкомандой, нужно перейти обратно в класс главной команды и зарегистрировать нашу подкоманду там. Для этого создадим конструктор и вызовем метод `addSubCommand()`.

```java
public EnchantCommand() {
    addSubCommand(new EnchantItemCommand(), new String[] {"enchant"}, new Permission("amixolenchants.enchant"));
}
```

В этот метод мы передаём экземпляр класса нашей подкоманды, новый массив строк (можно написать саму подкоманду и через запятую перечислить [Элиасы](https://www.spigotmc.org/wiki/command-alias/)), 3-м аргументом передаём новый пермишен, который должен будет иметь игрок для выполнении именно этой подкомандой.

## На это все, создавайте подкоманд, сколько душе угодно!

В заключении скажем что данный механизм был придуман не нами, а просто позаимствован у умного человека.
