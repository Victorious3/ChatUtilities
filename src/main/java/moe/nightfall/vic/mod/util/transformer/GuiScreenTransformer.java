package moe.nightfall.vic.mod.util.transformer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

public class GuiScreenTransformer implements ITransformHandler {

	@Override
	public byte[] transform(String contextName, byte[] bytes) 
	{
		InsnList toInject = new InsnList();
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		List<MethodNode> methods = classNode.methods;
		Iterator<MethodNode> iterator = methods.iterator();
		while(iterator.hasNext()) 
		{
			MethodNode m = iterator.next();
			boolean isDeobf = m.name.equals("handleMouseInput");
			boolean isSrg = m.name.equals("func_146274_d");
			if(isDeobf || isSrg)
			{
				for(int i = 0; i < m.instructions.size(); i++)
				{
					AbstractInsnNode ains = m.instructions.get(i);
					if(ains instanceof MethodInsnNode) 
					{
						MethodInsnNode mins = (MethodInsnNode) ains;
						if(mins.name.equals("mouseClicked") || mins.name.equals("func_73864_a"))
						{
							toInject.add(new FieldInsnNode(Opcodes.GETSTATIC, "moe/nightfall/vic/mod/util/ClientProxy", "itemClickHandler", "Lmoe/nightfall/vic/mod/util/ItemClickHandler;"));
							toInject.add(new VarInsnNode(Opcodes.ILOAD, 1));
							toInject.add(new VarInsnNode(Opcodes.ILOAD, 2));
							toInject.add(new VarInsnNode(Opcodes.ALOAD, 0));
							toInject.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiScreen", isDeobf ? "eventButton" : "field_146287_f", "I"));
							toInject.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "moe/nightfall/vic/mod/util/ItemClickHandler", "onMouseClickedGui", "(III)V", false));
							
							m.instructions.insert(mins, toInject);
							
							ClassWriter writer = new ClassWriter(0);
							classNode.accept(writer);
							
							System.out.println("[ChatUtils ASM]: GuiScreen patched!");
							return writer.toByteArray();
						}
					}
				}
			}
		}
		
		return bytes;
	}
	
	  private static final Printer printer = new Textifier();
	  private static final TraceMethodVisitor methodPrinter = new TraceMethodVisitor(printer);
	
	  public static String prettyprint(AbstractInsnNode insnNode) {
	    insnNode.accept(methodPrinter);
	    StringWriter sw = new StringWriter();
	    printer.print(new PrintWriter(sw));
	    printer.getText().clear();
	    return sw.toString();
	  }
}
