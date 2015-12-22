package moe.nightfall.vic.mod.util.transformer;

public interface ITransformHandler 
{
	public byte[] transform(String contextName, byte[] bytes);
}
