package moe.nightfall.vic.mod.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	public static ItemClickHandler itemClickHandler;

	@Override
	public void preInit() {
		itemClickHandler = new ItemClickHandler();
	}	
}
