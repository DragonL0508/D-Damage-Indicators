package me.dragonl.damageIndicators;

import io.fairyproject.FairyLaunch;
import io.fairyproject.log.Log;
import io.fairyproject.plugin.Plugin;
import org.bukkit.Bukkit;

@FairyLaunch
public class DamageIndicators extends Plugin {

    @Override
    public void onPluginEnable() {
        showCredit();
    }

    public void showCredit(){
        Bukkit.getConsoleSender().sendMessage("§7-----------------------------------");
        Bukkit.getConsoleSender().sendMessage("§6 D Damage Indicators");
        Bukkit.getConsoleSender().sendMessage("§6 Thanks for downloading !");
        Bukkit.getConsoleSender().sendMessage("§7-----------------------------------");
    }

}
