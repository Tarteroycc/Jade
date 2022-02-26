package snownee.jade.addon.vanilla;

import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IServerDataProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.config.IPluginConfig;
import mcp.mobius.waila.api.ui.IElementHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.jade.Jade;
import snownee.jade.VanillaPlugin;

public class BrewingStandProvider implements IComponentProvider, IServerDataProvider<BlockEntity> {
	public static final BrewingStandProvider INSTANCE = new BrewingStandProvider();

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!config.get(VanillaPlugin.BREWING_STAND) || !accessor.getServerData().contains("BrewingStand", Tag.TAG_COMPOUND)) {
			return;
		}
		CompoundTag tag = accessor.getServerData().getCompound("BrewingStand");
		int fuel = tag.getInt("Fuel");
		int time = tag.getInt("Time");
		IElementHelper helper = tooltip.getElementHelper();
		tooltip.add(Jade.smallItem(helper, new ItemStack(Items.BLAZE_POWDER)));
		tooltip.append(helper.text(new TextComponent(Integer.toString(fuel))));
		if (time > 0) {
			tooltip.append(helper.spacer(5, 0));
			tooltip.append(Jade.smallItem(helper, new ItemStack(Items.CLOCK)));
			tooltip.append(helper.text(new TranslatableComponent("jade.seconds", time / 20)));
		}
	}

	@Override
	public void appendServerData(CompoundTag tag, ServerPlayer player, Level arg2, BlockEntity te, boolean showDetails) {
		if (te instanceof BrewingStandBlockEntity) {
			BrewingStandBlockEntity brewingStand = (BrewingStandBlockEntity) te;
			CompoundTag compound = new CompoundTag();
			compound.putInt("Time", brewingStand.brewTime);
			compound.putInt("Fuel", brewingStand.fuel);
			tag.put("BrewingStand", compound);
		}
	}
}
