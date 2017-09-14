package ru.metal.convert.mapper;
import org.dozer.MappingException;

import java.util.Collection;
import java.util.List;

public interface Mapper {
    <A, B> B map(A src, Class<B> clazz);

    <A, B> List<B> mapCollections(Collection<A> src, Class<B> clazz);

    /**
     * Performs mapping between source and destination objects
     *
     * @param src
     * @param target
     * @throws MappingException
     */
    <A, B> void map(A src, B target);

    <A, B> void map(A src, B target, String mapId);
}
