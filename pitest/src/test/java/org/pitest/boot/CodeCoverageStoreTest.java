/*
 * Copyright 2011 Henry Coles
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and limitations under the License. 
 */
package org.pitest.boot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CodeCoverageStoreTest {

  @Mock
  private InvokeReceiver receiver;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    CodeCoverageStore.init(this.receiver);
  }


  @Test
  public void shouldRegisterNewClassesWithReceiver() {
    final int id = CodeCoverageStore.registerClass("Foo");
    verify(this.receiver).registerClass(id, "Foo");
  }

  @Test
  public void shouldGenerateNewClassIdForEachClass() {
    final int id = CodeCoverageStore.registerClass("Foo");
    final int id2 = CodeCoverageStore.registerClass("Bar");
    assertFalse(id == id2);
  }
  
  @Test
  public void shouldCodeAndEncodeWhenClassIdAndLineNumberAreAtMaximum() {
    long value = CodeCoverageStore.encode(Integer.MAX_VALUE, Integer.MAX_VALUE);
    assertEquals(Integer.MAX_VALUE, CodeCoverageStore.decodeClassId(value) );
    assertEquals(Integer.MAX_VALUE, CodeCoverageStore.decodeLineId(value) );
  }

  @Test
  public void shouldCodeAndEncodeWhenClassIdAndLineNumberAreAtMinimum() {
    long value = CodeCoverageStore.encode(Integer.MIN_VALUE, 0);
    assertEquals(Integer.MIN_VALUE, CodeCoverageStore.decodeClassId(value) );
    assertEquals(0, CodeCoverageStore.decodeLineId(value) );
  }
  
}