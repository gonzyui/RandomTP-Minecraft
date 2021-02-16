package fr.gonzyui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


public class Cmd2
  implements Listener
{
  public Cmd2(Main main) {}
  
  @EventHandler
  public void onCommandes(PlayerCommandPreprocessEvent e) {
     Player p = e.getPlayer();
     String msg = e.getMessage();
     String[] args = msg.split(" ");
    
     if (args[0].equalsIgnoreCase("/rversion")) {
       p.sendMessage("§bYou are running with the latest version of RandomTP.");
       e.setCancelled(true);
    } 
  }
}
