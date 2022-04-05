package dev.weiiswurst.placestom.listeners;

import dev.weiiswurst.placestom.world.PlaceBlocks;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

import java.util.function.Consumer;

public record PlayerJoinListener(InstanceContainer instanceContainer) implements Consumer<PlayerLoginEvent> {

    private static final int ITEM_AMOUNT = Integer.getInteger("placestom.blocks.inventory-amount", 69);

    @Override
    public void accept(final PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        player.setAllowFlying(true);
        player.setLevel(69);
        player.setExp(0.5f);
        event.setSpawningInstance(instanceContainer);
        player.setRespawnPoint(new Pos(0.5, 42, 0.5));

        PlaceBlocks.ALLOWED_BLOCKS.stream()
                .filter(material -> !material.block().equals(PlaceBlocks.DEFAULT_BLOCK))
                .map(type -> ItemStack.of(type).withAmount(ITEM_AMOUNT))
                .forEach(player.getInventory()::addItemStack);
    }
}
