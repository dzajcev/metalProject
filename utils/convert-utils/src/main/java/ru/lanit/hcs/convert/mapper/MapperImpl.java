package ru.lanit.hcs.convert.mapper;

import org.dozer.DozerBeanMapperSingletonWrapper;

import javax.ejb.EJB;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Named
@Singleton
public class MapperImpl implements Mapper {
    private org.dozer.Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    @Override
    public <A, B> B map(A src, Class<B> clazz) {
        if (src != null) {
            return mapper.map(src, clazz);
        }
        return null;
    }

    @Override
    public <A, B> void map(A src, B target) {
        if (src != null) {
            mapper.map(src, target);
        }
    }

    @Override
    public <A, B> void map(A src, B target, String mapId) {
        if (src != null) {
            mapper.map(src, target, mapId);
        }
    }

    @Override
    public <A, B> List<B> mapCollections(Collection<A> src, Class<B> clazz) {
        if (src == null || src.size() == 0) {
            return new ArrayList<B>(0);
        }
        List<B> dst = new ArrayList<B>();
        for (A a : src) {
            if (a != null) {
                dst.add(mapper.map(a, clazz));
            }
        }
        return dst;
    }
}
