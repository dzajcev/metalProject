package ru.metal.convert.mapper;
import javax.annotation.*;

@ParametersAreNonnullByDefault
public interface Converter<Entity, Dto> {

    @Nonnull
    Entity toEntity(Dto dto);

    @Nonnull
    Dto toDto(Entity entity);
}