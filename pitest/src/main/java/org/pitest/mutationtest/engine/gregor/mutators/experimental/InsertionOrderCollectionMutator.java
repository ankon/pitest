package org.pitest.mutationtest.engine.gregor.mutators.experimental;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.engine.gregor.MethodInfo;
import org.pitest.mutationtest.engine.gregor.MethodMutatorFactory;
import org.pitest.mutationtest.engine.gregor.MutationContext;

public enum InsertionOrderCollectionMutator implements MethodMutatorFactory {
	INSERTION_ORDER_COLLECTION_MUTATOR;
	
	public MethodVisitor create(final MutationContext context, final MethodInfo methodInfo, final MethodVisitor methodVisitor) {
		return new MethodVisitor(Opcodes.ASM5, methodVisitor) {
			private String replaceType;
			private MutationIdentifier newId;
			
			@Override
			public void visitTypeInsn(int opcode, String type) {
				if (opcode == Opcodes.NEW) {
					assert newId == null;

					// XXX: Should check where this gets assigned to, and potentially replace the type of the target/avoid mutating incompatible call sites.
					if ("java/util/LinkedHashMap".equals(type)) {
						replaceType = "java/util/HashMap";
					} else if ("java/util/LinkedHashSet".equals(type)) {
						replaceType = "java/util/HashSet";
					} else {
						replaceType = null;
						mv.visitTypeInsn(opcode, type);
						return;
					}
					
					newId = context.registerMutation(InsertionOrderCollectionMutator.this, "replaced " + type + " with " + replaceType);
					if (context.shouldMutate(newId)) {
						mv.visitTypeInsn(opcode, replaceType);
					} else {
						mv.visitTypeInsn(opcode, type);						
					}
				} else {
					mv.visitTypeInsn(opcode, type);
				}
			}
			
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
				// If that call happens to be a <init> for java.util.LinkedHash{Map,Set}, replace it
				// with a call to java.util.Hash{Map,Set}#<init>().
				MutationIdentifier id = newId;
				newId = null;
				if (id == null || !context.shouldMutate(id)) {
					methodVisitor.visitMethodInsn(opcode, owner, name, desc, itf);					
				} else {
					methodVisitor.visitMethodInsn(opcode, replaceType, name, desc, itf);					
				}
			}
		};
	}

	public String getGloballyUniqueId() {
	    return this.getClass().getName();
	}

	public String getName() {
		return name();
	}
}
