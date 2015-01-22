package org.pitest.mutationtest.engine.gregor.mutators.experimental;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;
import org.pitest.mutationtest.engine.Mutant;
import org.pitest.mutationtest.engine.gregor.MutatorTestBase;

public class InsertionOrderCollectionMutatorTest extends MutatorTestBase {

	@Before
	public void setupEngineToReplaceInsertionOrderCollections() {
		createTesteeWith(mutateOnlyCallMethod(), InsertionOrderCollectionMutator.INSERTION_ORDER_COLLECTION_MUTATOR);
	}

	private static class HasMethodWithLinkedHashMap implements Callable<String> {
		public String call() {
			Map<Object, Object> map = new LinkedHashMap<Object, Object>();
			return map.getClass().getSimpleName();
		}
	}
	
	@Test
	public void replacesLinkedHashMapConstructor() throws Exception {
	    final Mutant mutant = getFirstMutant(HasMethodWithLinkedHashMap.class);
	    assertMutantCallableReturns(new HasMethodWithLinkedHashMap(), mutant, "HashMap");		
	}
}
