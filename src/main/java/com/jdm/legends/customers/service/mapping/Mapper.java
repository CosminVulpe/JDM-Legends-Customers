package com.jdm.legends.customers.service.mapping;

@FunctionalInterface
public interface Mapper<T1, T2>  {
    T2 map(T1 source);
}
