package com.csvsim.random;

class Category<C, B> {
  
	private final C value;
  
  private final B binSize;

  Category(C value, B binSize) {
    this.value = value;
    this.binSize = binSize;
  }

  public C getValue() {
    return this.value;
  }

  public B getBinSize() {
    return this.binSize;
  }
}
