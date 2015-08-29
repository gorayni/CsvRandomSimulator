package com.csvsim.generator;

import com.csvsim.generator.utils.Field;

public interface Generator {
  public void generate();
  public Field<?> getField();
}