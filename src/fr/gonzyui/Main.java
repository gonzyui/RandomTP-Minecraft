package fr.gonzyui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    public HashMap<String, String> Messages = new HashMap();

    public HashMap<String, Date> Cooldowns = new HashMap();

    public String colourTranslate(String msg) {
        String translatedMsg = msg.replaceAll("&", "§");
        return translatedMsg;
    }

    public void log(String msgType, String msg) {
        ConsoleCommandSender console = getServer().getConsoleSender();
        String str;
        switch ((str = msgType.toUpperCase()).hashCode()) {
            case 2251950:
                if (!str.equals("INFO"))
                    break;
                console.sendMessage(colourTranslate("&b&l[INFO]&6&l[RTP]&3" + msg));
                return;
            case 2656902:
                if (!str.equals("WARN"))
                    break;
                console.sendMessage(colourTranslate("&e&l[WARN]&6&l[RTP]&3" + msg));
                return;
            case 66247144:
                if (!str.equals("ERROR"))
                    break;
                console.sendMessage(colourTranslate("&4&l[ERROR]&6&l[RTP]&3" + msg));
                return;
        }
        console.sendMessage(colourTranslate("&b&l[" + msgType.toUpperCase() + "]&6&l[RTP]&3" + msg));
    }

    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        this.Messages.put("notPlayer", colourTranslate(getConfig().getString("not-player-message")));
        this.Messages.put("notPermissions", colourTranslate(getConfig().getString("not-permissions-message")));
        log("INFO", "Enabling RTP");
    }

    public void randomTP(Player player, World world, Integer min, Integer max) {
        Random rand = new Random();
        min = Integer.valueOf(min.intValue() / 2);
        max = Integer.valueOf(max.intValue() / 2);
        Integer xOffset = Integer.valueOf(rand.nextInt(max.intValue() - min.intValue()) + min.intValue());
        Integer zOffset = Integer.valueOf(rand.nextInt(max.intValue() - min.intValue()) + min.intValue());
        Integer x = Integer.valueOf((int)player.getLocation().getX() + xOffset.intValue());
        Integer z = Integer.valueOf((int)player.getLocation().getX() + zOffset.intValue());
        Integer y = Integer.valueOf(world.getHighestBlockYAt(x.intValue(), z.intValue()));
        Block block = world.getBlockAt(new Location(world, x.intValue(), (y.intValue() - 1), z.intValue()));
        while (block.isLiquid()) {
            xOffset = Integer.valueOf(rand.nextInt(max.intValue() - min.intValue()) + min.intValue());
            zOffset = Integer.valueOf(rand.nextInt(max.intValue() - min.intValue()) + min.intValue());
            x = Integer.valueOf((int)player.getLocation().getX() + xOffset.intValue());
            z = Integer.valueOf((int)player.getLocation().getX() + zOffset.intValue());
            y = Integer.valueOf(world.getHighestBlockYAt(x.intValue(), z.intValue()));
            block = world.getBlockAt(new Location(world, x.intValue(), (y.intValue() - 1), z.intValue()));
        }
        Integer dist = Integer.valueOf(xOffset.intValue() + zOffset.intValue());
        Location loc = new Location(world, x.intValue(), y.intValue(), z.intValue());
        player.teleport(loc);
        if (getConfig().getBoolean("log-teleports"))
            log("INFO", "Randomly teleported " + player.getName() + " " + dist.toString() + " blocks away");
        player.sendMessage(colourTranslate(getConfig().getString("tp-message").replaceAll("\\{distance\\}", dist.toString())));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName();
        HashMap<String, Pair<String, String>> cmds = new HashMap();
        cmds.put("", new Pair("teleports you to a random locations", "rtp.rtp"));
        cmds.put("help", new Pair("lists all the available commands", "rtp.help"));
        cmds.put("setconfig <variable> <value>", new Pair("sets the config variable to the specified value", "rtp.setConfig"));
        cmds.put("reload", new Pair("reloads the config", "rtp.reload"));
        if (cmdName.equalsIgnoreCase("rtp"))
            if (args.length == 0) {
                Boolean isPlayer = Boolean.valueOf(sender instanceof Player);
                if (!isPlayer.booleanValue()) {
                    sender.sendMessage(this.Messages.get("notPlayer"));
                    return true;
                }
                if (!sender.hasPermission("rtp.rtp")) {
                    sender.sendMessage(this.Messages.get("notPermissions"));
                    return true;
                }
                Player player = (Player)sender;
                Integer cooldownTime = Integer.valueOf(getConfig().getInt("cooldown-time"));
                if (this.Cooldowns.get(player.getName()) != null && cooldownTime.intValue() > 0 && !player.hasPermission("rtp.ignoreCooldowns")) {
                    Date cooldownDate = this.Cooldowns.get(player.getName());
                    Date current = new Date();
                    Long seconds = Long.valueOf(TimeUnit.SECONDS.convert(current.getTime() - cooldownDate.getTime(), TimeUnit.MILLISECONDS));
                    if (seconds.longValue() < cooldownTime.intValue()) {
                        player.sendMessage(ChatColor.DARK_RED + "You have to wait " + ChatColor.RED + ChatColor.BOLD + (cooldownTime.intValue() - seconds.longValue()) + ChatColor.DARK_RED + " seconds");
                        return true;
                    }
                }
                Integer minDist = Integer.valueOf(getConfig().getInt("min-distance"));
                Integer maxDist = Integer.valueOf(getConfig().getInt("max-distance"));
                randomTP(player, player.getWorld(), minDist, maxDist);
                if (cooldownTime.intValue() > 0)
                    this.Cooldowns.put(player.getName(), new Date());
            } else if (args[0].equalsIgnoreCase("help")) {
                if (!sender.hasPermission("rtp.help")) {
                    sender.sendMessage(this.Messages.get("notPermissions"));
                    return true;
                }
                sender.sendMessage(ChatColor.GOLD + "----[[ Available commands for rtp ]]----");
                for (Map.Entry<String, Pair<String, String>> entry : cmds.entrySet()) {
                    if (sender.hasPermission((String)((Pair)entry.getValue()).getValue()))
                        sender.sendMessage(ChatColor.AQUA + "/rtp " + (String)entry.getKey() + " " + ChatColor.YELLOW + (String)((Pair)entry.getValue()).getKey());
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("rtp.reload")) {
                    sender.sendMessage(this.Messages.get("notPermissions"));
                    return true;
                }
                reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Successfully reloaded the config");
            } else if (args[0].equalsIgnoreCase("setconfig")) {
                if (!sender.hasPermission("rtp.setConfig")) {
                    sender.sendMessage(this.Messages.get("notPermissions"));
                    return true;
                }
                if (getConfig().getString(args[1]) == null) {
                    String keyList = "";
                    for (String key : getConfig().getKeys(false)) {
                        if (keyList == "") {
                            keyList = String.valueOf(keyList) + ChatColor.RED + ChatColor.BOLD + key;
                            continue;
                        }
                        keyList = String.valueOf(keyList) + ChatColor.DARK_RED + ", " + ChatColor.RED + ChatColor.BOLD + key;
                    }
                    sender.sendMessage(ChatColor.DARK_RED + "The config option " + ChatColor.DARK_RED + ChatColor.BOLD + args[1] + ChatColor.RED + " doesn't exist");
                    sender.sendMessage(ChatColor.DARK_RED + "Valid options are: " + keyList);
                    return true;
                }
                if (args[2] == null) {
                    sender.sendMessage(ChatColor.RED + "Please enter a value to change it to");
                    return true;
                }
                getConfig().set(args[1], Integer.valueOf(Integer.parseInt(args[2])));
                saveConfig();
                reloadConfig();
                sender.sendMessage(ChatColor.GREEN + "Successfully set " + ChatColor.RED + ChatColor.BOLD + args[1] + ChatColor.GREEN + " to " + ChatColor.RED + ChatColor.BOLD + args[2]);
            }
        return true;
    }
}
