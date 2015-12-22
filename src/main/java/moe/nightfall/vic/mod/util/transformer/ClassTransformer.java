package moe.nightfall.vic.mod.util.transformer;

import java.util.HashMap;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
	
	private HashMap<String, ITransformHandler> classes = new HashMap<String, ITransformHandler>();
	
	public ClassTransformer() throws ClassNotFoundException 
	{
		classes.put("net.minecraft.client.gui.GuiScreen", new GuiScreenTransformer());
		
		GuiContainerTransformer gctransformer = new GuiContainerTransformer();
		classes.put("net.minecraft.client.gui.inventory.GuiContainer", gctransformer);
		classes.put("net.minecraft.client.gui.inventory.GuiContainerCreative", gctransformer);
	}
	
	@Override
	public byte[] transform(String contextName, String transformedName, byte[] bytes) 
	{
		if(classes.containsKey(contextName))
			return classes.get(contextName).transform(contextName, bytes);
		
		return bytes;
	}
}