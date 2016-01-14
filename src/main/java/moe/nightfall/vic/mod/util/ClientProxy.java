package moe.nightfall.vic.mod.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	public static ItemClickHandler itemClickHandler;

	@Override
	public void preInit() {
		itemClickHandler = new ItemClickHandler();
	}	
}
