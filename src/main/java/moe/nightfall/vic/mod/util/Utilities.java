package moe.nightfall.vic.mod.util;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;

@Mod(modid = "chatutils")
public class Utilities
{
	@Instance("chatutils")
	public static Utilities instance;
	
	@SidedProxy(modId = "chatutils", clientSide = "moe.nightfall.vic.mod.util.ClientProxy", serverSide = "moe.nightfall.vic.mod.util.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit();
	}
}
