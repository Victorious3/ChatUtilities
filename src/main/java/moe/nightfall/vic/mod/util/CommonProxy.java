package moe.nightfall.vic.mod.util;

import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	
	public CommonProxy() {
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	public void preInit() {}
}
