package moe.nightfall.vic.mod.util;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.event.HoverEvent;
import net.minecraft.event.HoverEvent.Action;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ItemClickHandler 
{
	private GuiContainer openContainer;
	
	public ItemClickHandler() 
	{
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void onGuiOpened(GuiOpenEvent event)
	{
		if(event.gui instanceof GuiContainer) 
			this.openContainer = (GuiContainer) event.gui;
		else this.openContainer = null;
	}
	
	@SubscribeEvent
	public void drawScreen(DrawScreenEvent event) throws Exception
	{
		if(event.gui instanceof GuiChat)
		{
			Minecraft mc = Minecraft.getMinecraft();
			GuiChat gui = (GuiChat) event.gui;
			IChatComponent component = mc.ingameGUI.getChatGUI().func_146236_a(Mouse.getX(), Mouse.getY());
			if(component == null) return;
			HoverEvent hoverEvent = component.getChatStyle().getChatHoverEvent();
			if(hoverEvent != null && hoverEvent.getAction() == Action.SHOW_ITEM)
			{
				FontRenderer fr = mc.fontRenderer;
				int oWidth = 0, oHeight = 0, oy = 0;
				
				NBTBase nbtbase = JsonToNBT.func_150315_a(hoverEvent.getValue().getUnformattedText());
				ItemStack stack = null;
				if(nbtbase instanceof NBTTagCompound)
					stack = ItemStack.loadItemStackFromNBT((NBTTagCompound) nbtbase);
				if(stack == null) return;
				List<String> tooltip = stack.getTooltip(mc.thePlayer, Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
				for(String line : tooltip)
				{
					int width = fr.getStringWidth(line);
					if(width > oWidth) oWidth = width;
				}
				oHeight = (fr.FONT_HEIGHT + 1) * tooltip.size() + 8;
				oWidth += 8;
				if(oWidth + event.mouseX + 4 > event.gui.width) oWidth = oWidth * -1 - 43;
				if(oHeight + event.mouseY - 14 > event.gui.height) oy = oHeight + event.mouseY - 14 - event.gui.height;
				
				int x = event.mouseX + 12 + oWidth;
				int y = event.mouseY - 12 - oy;
				int width = 16;
				int height = 16;
				
				int color1 = 0xF0100010;
				drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, color1, color1, 300);
				drawGradientRect(x - 3, y + height + 3, x + width + 3, y + height + 4, color1, color1, 300);
				drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, color1, color1, 300);
				drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, color1, color1, 300);
				drawGradientRect(x + width + 3, y - 3, x + width + 4, y + height + 3, color1, color1, 300);
				
				int color2 = 0x505000FF;
				int color3 = (color2 & 0x00FEFEFE) >> 1 | color2 & 0xFF000000;
				drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, color2, color3, 300);
				drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, color2, color3, 300);
				drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, color2, color2, 300);
				drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, color3, color3, 300);
				
				RenderItem renderItem = RenderItem.getInstance();
				float zLevel = renderItem.zLevel;
				renderItem.zLevel = 300;
				RenderHelper.enableGUIStandardItemLighting();
				try {
					renderItem.renderItemAndEffectIntoGUI(fr, mc.getTextureManager(), stack, x, y);
					renderItem.renderItemOverlayIntoGUI(fr, mc.getTextureManager(), stack, x, y);
				} catch (Exception e) {
					// Failed to render item, probably due to leaving out the NBT compound
					// There isn't much we can do about this... Apart from adding our own packet, of course
				}
				RenderHelper.disableStandardItemLighting();
				renderItem.zLevel = zLevel;
			}
		}
	}
	
	public static void drawGradientRect(int x1, int y1, int x2, int y2, int c1, int c2, int zLevel) 
	{
		float f = (float) (c1 >> 24 & 255) / 255.0F;
		float f1 = (float) (c1 >> 16 & 255) / 255.0F;
		float f2 = (float) (c1 >> 8 & 255) / 255.0F;
		float f3 = (float) (c1 & 255) / 255.0F;
		
		float f4 = (float) (c2 >> 24 & 255) / 255.0F;
		float f5 = (float) (c2 >> 16 & 255) / 255.0F;
		float f6 = (float) (c2 >> 8 & 255) / 255.0F;
		float f7 = (float) (c2 & 255) / 255.0F;
		
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		Tessellator tes = Tessellator.instance;
		
		tes.startDrawingQuads();
		tes.setColorRGBA_F(f1, f2, f3, f);
		tes.addVertex(x2, y1, zLevel);
		tes.addVertex(x1, y1, zLevel);
		tes.setColorRGBA_F(f5, f6, f7, f4);
		tes.addVertex(x1, y2, zLevel);
		tes.addVertex(x2, y2, zLevel);
		tes.draw();
		
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	// ASM injected
	public void onMouseClickedGui(int x, int y, int button)
	{
		if(GuiScreen.isCtrlKeyDown() && openContainer != null)
		{
			int guiLeft = ReflectionHelper.getPrivateValue(GuiContainer.class, openContainer, "guiLeft", "field_147003_i");
			int guiTop = ReflectionHelper.getPrivateValue(GuiContainer.class, openContainer, "guiTop", "field_147009_r");

			Slot clickedSlot = null;
			
			for(Slot slot : (List<Slot>) openContainer.inventorySlots.inventorySlots)
			{
				if(x >= slot.xDisplayPosition + guiLeft && y >= slot.yDisplayPosition + guiTop && x <= slot.xDisplayPosition + guiLeft + 16 && y <= slot.yDisplayPosition + guiTop + 16)
				{
					clickedSlot = slot;
					break;
				}
			}
			
			if(clickedSlot == null || !clickedSlot.getHasStack()) return;
			
			ItemStack stack = clickedSlot.getStack();
			String text = "[S%" + stack.getItem().delegate.name() + ", " + stack.stackSize + ", " + stack.getItemDamage() + "]";
			
			if(button == 1)
				Minecraft.getMinecraft().displayGuiScreen(new GuiChat(text));
			else Minecraft.getMinecraft().thePlayer.sendChatMessage(text);		
		}
	}
	
	@SubscribeEvent
	public void onChatMessage(ClientChatReceivedEvent event) {
		if (!(event.message instanceof ChatComponentTranslation)) return;
		ChatComponentTranslation in = (ChatComponentTranslation) event.message;
		if(!in.getKey().equals("chat.type.text")) return;
		
		IChatComponent res = new ChatComponentText("");		
		IChatComponent comp = (IChatComponent) in.getFormatArgs()[1];
		for (IChatComponent component : (List<IChatComponent>) comp.getSiblings()) {
			// Pick unformatted regions
			if (component instanceof ChatComponentText) {
				ChatComponentText tcomp = (ChatComponentText) component;
				String raw = tcomp.getUnformattedText();
				int index = raw.indexOf("[S%", 0);
				if (index == -1) {
					res.appendSibling(component);
					continue;
				}
				
				if (index > 0) {
					// Append previous text as new component
					appendWithStyle(raw.substring(0, index), component, res);
				}
				
				int index2 = -1, index3 = -1;
				while (index != -1) {
					index2 = raw.indexOf("]", index);
					if (index2 == -1) break;
					try {
						String[] data = raw.substring(index + 3, index2).split(",");
						if (!data[0].contains(":")) {
							data[0] = "minecraft:" + data[0];
						}
						ItemStack stack = GameRegistry.makeItemStack(
							data[0], 
							data.length > 2 ? Integer.parseInt(data[2].replaceAll(" ", "")) : 0, 
							1, 
							null
						);
						// Incompetent. 
						stack.stackSize = data.length > 1 ? Integer.parseInt(data[1].replaceAll(" ", "")) : 1;
						res.appendSibling(createCompound(stack));
					} catch (Exception e) {
						// Couldn't parse this correctly
						appendWithStyle(raw.substring(index, index2 + 1), component, res);
					}
					
					index3 = raw.indexOf("[S%", index2);
					if (index3 - index2 > 0) {
						// Append previous text as new component
						appendWithStyle(raw.substring(index2 + 1, index3), component, res);
					}
					index = index3;
				}
				
				if (index2 != -1 && index2 + 1 < raw.length()) {
					// Append left over text as new component
					appendWithStyle(raw.substring(index2 + 1, raw.length()), component, res);
				}
			} else {
				res.appendSibling(component);
			}
			event.message = new ChatComponentTranslation("chat.type.text", in.getFormatArgs()[0], res);
		}
	}
	
	private void appendWithStyle(String text, IChatComponent parent, IChatComponent res) {
		ChatComponentText textc = new ChatComponentText(text);
		textc.setChatStyle(parent.getChatStyle());
		res.appendSibling(textc);
	}
	
	private IChatComponent createCompound(ItemStack stack) {
		String start = "[";
		if (stack.stackSize > 1) {
			start += stack.stackSize + "* ";
		}
		IChatComponent ichatcomponent = (new ChatComponentText(start)).appendText(stack.getDisplayName()).appendText("]");

        if (stack.getItem() != null)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            stack.writeToNBT(nbttagcompound);
            ichatcomponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new ChatComponentText(nbttagcompound.toString())));
            ichatcomponent.getChatStyle().setColor(stack.getRarity().rarityColor);
        }

        return ichatcomponent;
	}
}
