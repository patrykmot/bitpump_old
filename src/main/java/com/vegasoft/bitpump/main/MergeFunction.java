package com.vegasoft.bitpump.main;

@FunctionalInterface
public interface MergeFunction {
    void merge(int targetIndex, int sourceIndex);
}
