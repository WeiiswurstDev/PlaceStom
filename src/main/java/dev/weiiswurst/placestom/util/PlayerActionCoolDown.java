package dev.weiiswurst.placestom.util;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerActionCoolDown {

    public static final int COOL_DOWN_TIME_SECONDS = Integer.getInteger("placestom.cooldown", 3);

    private final Map<UUID, Long> playerCoolDownMap = new HashMap<>();

    public boolean performAction(UUID player) {
        long currentTime = System.currentTimeMillis();
        if (playerCoolDownMap.containsKey(player) && playerCoolDownMap.get(player) > currentTime) {
                return false;
        }
        playerCoolDownMap.put(player, currentTime + 1000L * COOL_DOWN_TIME_SECONDS);
        return true;
    }

    public void setCooldownMillis(UUID player, int newCooldown) {
        if (newCooldown == 0) {
            playerCoolDownMap.remove(player);
        } else {
            playerCoolDownMap.put(player, System.currentTimeMillis() + newCooldown);
        }
    }

    @ApiStatus.Internal
    public void applyAnimation(Player player) {
        Long time = playerCoolDownMap.get(player.getUuid());
        if (time == null) {
            return;
        }
        float remainingTimeSeconds = (time - System.currentTimeMillis()) / 1000f;
        player.setLevel((int)remainingTimeSeconds);
        if (remainingTimeSeconds <= 0) {
            playerCoolDownMap.remove(player.getUuid());
            player.playSound(Sound.sound(Key.key("ui.button.click"), Sound.Source.MASTER,1,1));
            player.setExp(0);
        } else if (remainingTimeSeconds / COOL_DOWN_TIME_SECONDS <= 1) {
            // This check is needed because setCooldownMillis
            // can set the cooldown of a player to a number greater than COOL_DOWN_TIME_SECONDS
            player.setExp(remainingTimeSeconds / COOL_DOWN_TIME_SECONDS);
        } else {
            // The cooldown is greater than the max.
            player.setExp(1);
        }
    }

    public int getCoolDownTime(UUID player) {
        if (playerCoolDownMap.containsKey(player)) {
            return (int) (playerCoolDownMap.get(player) - System.currentTimeMillis());
        } else {
            return 0;
        }
    }

}
