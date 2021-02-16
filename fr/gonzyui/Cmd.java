package fr.gonzyui;

import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;





public class Cmd
  implements Listener
{
  public Cmd(Main main) {}
  
  public void onDisable() {}
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
     if (cmd.getName().equalsIgnoreCase("rtp") && sender instanceof Player) {

      
       Player p = (Player)sender;
      
       Location loc = p.getLocation();
      
       Random r = new Random();
      
       int x = r.nextInt(500) + 1;
       int y = 69;
       int z = r.nextInt(500) + 1;
      
       Location teleportlocation = new Location(p.getWorld(), x, y, z);
      
       p.teleport(teleportlocation);
      
       p.sendMessage("§bSuccefully teleport to §7" + (int)teleportlocation.distance(loc) + " §bblocks away!");
       p.playSound(p.getLocation(), Sound.ANVIL_LAND, 5.0F, 5.0F);
    } 


    
     return true;
  }
}