package moe.nightfall.vic.mod.util.transformer;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class GuiContainerTransformer implements ITransformHandler {

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
			boolean isDeobf = m.name.equals("handleMouseClick");
			boolean isSRG = m.name.equals("func_146984_a");
			if(isDeobf || isSRG)
			{
				toInject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "net/minecraft/client/gui/GuiScreen", isDeobf ? "isCtrlKeyDown" : "func_146271_m", "()Z", false));
				LabelNode lnode = new LabelNode();
				toInject.add(new JumpInsnNode(Opcodes.IFEQ, lnode));
				toInject.add(new InsnNode(Opcodes.RETURN));
				toInject.add(lnode);
				m.instructions.insert(toInject);
				
				ClassWriter writer = new ClassWriter(0);
				classNode.accept(writer);
				
				System.out.println("[ChatUtils ASM]: " + contextName.substring(contextName.lastIndexOf(".") + 1, contextName.length()) + " patched!");
				return writer.toByteArray();
			}
		}
		return bytes;
	}
}
