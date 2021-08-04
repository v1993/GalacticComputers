package com.v1993.galacticcomputers.gccore;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSealedCable extends Block {
	public BlockSealedCable() {
		super(Material.CLAY);
		setResistance(0.2F);
		setHardness(0.4f);
		setSoundType(SoundType.STONE);
		setUnlocalizedName("com.v1993.galacticcomputers.gccore.sealed_cable");
		setRegistryName("sealed_cable");
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntitySealedCable();
	}
}
