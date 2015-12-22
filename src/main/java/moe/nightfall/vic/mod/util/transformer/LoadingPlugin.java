package moe.nightfall.vic.mod.util.transformer;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class LoadingPlugin implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass() 
	{
		return new String[]{ClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() 
	{
		return null;
	}

	@Override
	public String getSetupClass() 
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() 
	{
		return null;
	}
}
