package net.mcud.udtitle.listener;

import net.mcud.udtitle.Lang;
import net.mcud.udtitle.GuiTitle;
import net.mcud.udtitle.Title;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryView;

public class PlayerEvent implements Listener {
	Title plugin;
	public PlayerEvent(Title plugin) {
		this.plugin = plugin;
	}
	
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        String prefix = plugin.chatApi.getPlayerPrefix(p).replace("&", "§");
        int id = plugin.extractId(prefix);
        if (id >= 1) {
            if (plugin.hasTitle(p.getName(), id)) {
                String a = plugin.GetTitleForTitltID(id);
                if (!a.equals(prefix)) {
                    plugin.setPlayerTitle(p, a);
                    return;
                }
                return;
            }
            plugin.setDefaultPrefix(p);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        InventoryView inventory = e.getView();
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            String iname = inventory.getTitle();
            if (iname.indexOf(plugin.titleGuiTitle) == 0) {
                e.setCancelled(true);
                int index = plugin.extractId(iname.substring(plugin.titleGuiTitle.length(), iname.length()));
                e.getCurrentItem().getItemMeta().hasDisplayName();
                String name = e.getCurrentItem().getItemMeta().getDisplayName();
                if (name.equals(GuiTitle.prevPage)) {
                    p.closeInventory();
                    p.updateInventory();
                    plugin.getGuiTitle().open(p, index < 2 ? 1 : index - 1);
                } else if (name.equals(GuiTitle.nextPage)) {
                    p.closeInventory();
                    p.updateInventory();
                    plugin.getGuiTitle().open(p, index + 1);
                } else if (name.equals(GuiTitle.cancelTag)) {
                    plugin.setDefaultPrefix(p);
                    p.closeInventory();
                    plugin.getGuiTitle().open(p, index);
                } else if (plugin.extractId(name) < 1) {
                    p.sendMessage(plugin.msgManager.getMsg(Lang.InvalidTitle));
                    p.closeInventory();
                } else {
                    if (!plugin.hasTitle(p.getName(), plugin.extractId(name))) {
                        p.sendMessage(plugin.msgManager.getMsg(Lang.nohava));
                    } else if (plugin.cost <= 0.0d) {
                        plugin.setPlayerTitle(p, plugin.extractId(name));
                    } else if (!plugin.setPlayerTitleUseMoney(p, plugin.extractId(name))) {
                        p.sendMessage(plugin.msgManager.getMsg(Lang.NotEnoughMoney));
                        return;
                    } else {
                        p.sendMessage(plugin.msgManager.getMsg(Lang.ExpendMoney).replace("%1", new StringBuilder(String.valueOf(plugin.cost)).toString()));
                    }
                    p.sendMessage(plugin.msgManager.getMsg(Lang.change));
                    p.closeInventory();
                    p.updateInventory();
                    plugin.getGuiTitle().open(p, index);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClickA(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        InventoryView inventory = e.getView();
        if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
            String iname = inventory.getTitle();
            if (iname.indexOf(plugin.listGuiTitle) == 0) {
                e.setCancelled(true);
                int index = plugin.extractId(iname.substring(plugin.listGuiTitle.length(), iname.length()));
                e.getCurrentItem().getItemMeta().hasDisplayName();
                String name = e.getCurrentItem().getItemMeta().getDisplayName();
                if (name.equals(GuiTitle.prevPage)) {
                    p.closeInventory();
                    p.updateInventory();
                    plugin.getGuiList().open(p, index < 2 ? 1 : index - 1);
                } else if (name.equals(GuiTitle.nextPage)) {
                    p.closeInventory();
                    p.updateInventory();
                    plugin.getGuiList().open(p, index + 1);
                } else if (name.equals(GuiTitle.cancelTag)) {
                    plugin.setDefaultPrefix(p);
                    p.closeInventory();
                    plugin.getGuiList().open(p, index);
                } else if (plugin.extractId(name) < 1) {
                    p.sendMessage(plugin.msgManager.getMsg(Lang.InvalidTitle));
                    p.closeInventory();
                } else {
                    if (!plugin.hasTitle(p.getName(), plugin.extractId(name))) {
                        p.sendMessage(plugin.msgManager.getMsg(Lang.nohava));
                    } else if (plugin.cost <= 0.0d) {
                        plugin.setPlayerTitle(p, plugin.extractId(name));
                    } else if (!plugin.setPlayerTitleUseMoney(p, plugin.extractId(name))) {
                        p.sendMessage(plugin.msgManager.getMsg(Lang.NotEnoughMoney));
                        return;
                    } else {
                        p.sendMessage(plugin.msgManager.getMsg(Lang.ExpendMoney).replace("%1", new StringBuilder(String.valueOf(plugin.cost)).toString()));
                    }
                    p.closeInventory();
                    p.updateInventory();
                    plugin.getGuiList().open(p, index);
                }
            }
        }
    }
}