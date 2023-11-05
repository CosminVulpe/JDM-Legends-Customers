package com.jdm.legends.users.service.mapping;

@FunctionalInterface
public interface Mapper<T1,T2>  {
    T2 map(T1 source);
}
