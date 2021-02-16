package fr.gonzyui;

import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


public class Panneaux
  implements Listener
{
  public Panneaux(Main main) {}
  
  @EventHandler
  public void onPInteract(PlayerInteractEvent e) {
     if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
      
       BlockState block = e.getClickedBlock().getState();
      
       if (block instanceof Sign) {
         Sign sign = (Sign)block;
         if (sign.getLine(0).equalsIgnoreCase("[§bRandomTP§0]")) {
           e.getPlayer();
           if (sign.getLine(1).equalsIgnoreCase("§3Right click")) {

            
             Player p = e.getPlayer();
             p.getWorld();
             p.performCommand("randomtp");
          } 
        } 
      } 
    } 
  }
}