package net.p3pp3rf1y.sophisticatedstorage.client.render;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedstorage.block.ITintableBlockItem;
import net.p3pp3rf1y.sophisticatedstorage.block.ShulkerBoxBlock;
import net.p3pp3rf1y.sophisticatedstorage.block.ShulkerBoxBlockEntity;

public class ShulkerBoxItemRenderer extends BlockEntityWithoutLevelRenderer {
	private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
	private final LoadingCache<BlockItem, ShulkerBoxBlockEntity> shulkerBoxBlockEntities = CacheBuilder.newBuilder().maximumSize(512L).weakKeys().build(new CacheLoader<>() {
		@Override
		public ShulkerBoxBlockEntity load(BlockItem blockItem) {
			return new ShulkerBoxBlockEntity(BlockPos.ZERO, blockItem.getBlock().defaultBlockState().setValue(ShulkerBoxBlock.FACING, Direction.SOUTH));
		}
	});

	public ShulkerBoxItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
		super(blockEntityRenderDispatcher, entityModelSet);
		this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
	}

	@Override
	public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (!(stack.getItem() instanceof BlockItem blockItem)) {
			return;
		}

		ShulkerBoxBlockEntity shulkerBoxBlockEntity = shulkerBoxBlockEntities.getUnchecked(blockItem);
		if (stack.getItem() instanceof ITintableBlockItem tintableBlockItem) {
			shulkerBoxBlockEntity.getStorageWrapper().setMainColor(tintableBlockItem.getMainColor(stack).orElse(-1));
			shulkerBoxBlockEntity.getStorageWrapper().setAccentColor(tintableBlockItem.getAccentColor(stack).orElse(-1));
		}
		var blockentityrenderer = blockEntityRenderDispatcher.getRenderer(shulkerBoxBlockEntity);
		if (blockentityrenderer != null) {
			blockentityrenderer.render(shulkerBoxBlockEntity, 0.0F, poseStack, buffer, packedLight, packedOverlay);
		}
	}
}
