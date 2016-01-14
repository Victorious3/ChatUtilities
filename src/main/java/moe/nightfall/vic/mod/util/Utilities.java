package moe.nightfall.vic.mod.util;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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
