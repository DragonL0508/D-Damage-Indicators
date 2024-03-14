package me.dragonl.damageIndicators;

import com.cryptomorin.xseries.messages.ActionBar;
import io.fairyproject.bootstrap.bukkit.BukkitPlugin;
import io.fairyproject.bukkit.events.player.EntityDamageByPlayerEvent;
import io.fairyproject.bukkit.listener.RegisterAsListener;
import io.fairyproject.container.InjectableComponent;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.w3c.dom.Text;

import java.awt.*;

@InjectableComponent
@RegisterAsListener
public class GlobalListener implements Listener {
    private final DoubleFormatter doubleFormatter;

    public GlobalListener(DoubleFormatter doubleFormatter) {
        this.doubleFormatter = doubleFormatter;
    }

    @EventHandler
    public void playerAttack(EntityDamageByPlayerEvent event) {
        LivingEntity livingEntity = (LivingEntity) event.getEntity();
        World world = event.getEntity().getWorld();
        //Actionbar messages
        String damageInfo = (doubleFormatter.format(livingEntity.getHealth() - event.getDamage(), 1)) + "/" + livingEntity.getMaxHealth() + "❤";
        if (livingEntity.getHealth() - event.getDamage() <= 0)
            damageInfo = "§f☠";

        ActionBar.sendActionBar(BukkitPlugin.INSTANCE, event.getPlayer(),
                "§e" + event.getEntity().getName()
                        + " §c" + damageInfo
                , 20);

        //text display
        spawnIndicator(event.getEntity(), false, event.getDamage());
    }

    @EventHandler
    public void EntityHeal(EntityRegainHealthEvent event) {
        if (!event.getEntity().getType().equals(EntityType.PLAYER)) {
            Entity entity = event.getEntity();
            World world = entity.getWorld();

            //text display
            spawnIndicator(entity, true, event.getAmount());

            //particles
            world.spawnParticle(Particle.HEART, entity.getLocation().add(0, 0.5, 0), 3, 0.5, 0.5, 0.5, 0);
        }
    }

    private TextDisplay spawnIndicator(Entity eventEntity, Boolean isHeal, double number) {
        //vector operation
        Vector location = eventEntity.getLocation().toVector();
        Vector facingVector = eventEntity.getFacing().getDirection();
        Vector spawnVector = location.add(facingVector.multiply(0.75));

        //summon entity
        TextDisplay indicators = eventEntity.getWorld().spawn(spawnVector.toLocation(eventEntity.getWorld()).add(0,0.7,0), TextDisplay.class);
        String prefix = "§c-";
        if (isHeal) prefix = "§a+";

        indicators.setText(prefix + doubleFormatter.format(number, 1));
        indicators.setBillboard(Display.Billboard.CENTER);
        indicators.setShadowed(true);
        indicators.setTeleportDuration(2);
        indicatorAnimation(indicators);

        return indicators;
    }

    private void indicatorAnimation(TextDisplay indicator) {
        new BukkitRunnable() {
            int height = 0;

            @Override
            public void run() {
                indicator.teleport(indicator.getLocation().add(0, 0.1, 0));
                if (height == 15) {
                    indicator.remove();
                    this.cancel();
                }

                height++;
            }
        }.runTaskTimer(BukkitPlugin.INSTANCE, 0, 1);
    }
}
