package ru.lanit.hcs.convert.mapper;

import javax.annotation.*;
import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@ParametersAreNonnullByDefault
public abstract class CollectionConverter<Entity, Dto> implements Converter<Entity, Dto> {

    @Nonnull
    public Stream<Entity> toEntities(Collection<? extends Dto> dtos) {
        return dtos
                .stream()
                .map(this::toEntity)
                .filter(Objects::nonNull);
    }

    @Nonnull
    public List<Entity> toEntityList(Collection<? extends Dto> dtos) {
        return toEntities(dtos)
                .collect(toList());
    }

    @Nonnull
    public Set<Entity> toEntitySet(Collection<? extends Dto> dtos) {
        return toEntities(dtos).collect(toSet());
    }

    @Nonnull
    public Stream<Dto> toDtos(Collection<? extends Entity> entities) {
        return entities
                .stream()
                .map(this::toDto)
                .filter(Objects::nonNull);
    }

    @Nonnull
    public List<Dto> toDtoList(Collection<? extends Entity> entities) {
        return toDtos(entities)
                .collect(toList());
    }

    @Nonnull
    public Set<Dto> toDtoSet(Collection<? extends Entity> entities) {
        return toDtos(entities)
                .collect(toSet());
    }
}