package me.ranzeplay.messagechain.nbtutils;

import lombok.SneakyThrows;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import net.minecraft.nbt.*;

import java.lang.reflect.Field;
import java.util.List;

public class NBTHelper {
    @SneakyThrows
    public static <T> NbtCompound serialize(T source) {
        var clazz = source.getClass();
        if (clazz.isAnnotationPresent(NBTSerializable.class)) {
            var result = new NbtCompound();

            for (var field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(NBTSerializableEntry.class)) {
                    var element = serializeField(source, field);
                    var entryAnnotation = field.getAnnotation(NBTSerializableEntry.class);
                    result.put(entryAnnotation.key(), element);
                }
            }

            return result;
        } else {
            throw new ReflectiveOperationException("NBTSerializable annotation required.");
        }
    }

    @SneakyThrows
    private static NbtElement serializeField(Object o, Field source) {
        source.setAccessible(true);

        var typeClass = source.getType();
        if (typeClass.isAssignableFrom(Integer.class)) {
            return NbtInt.of(source.getInt(o));
        } else if (typeClass.isAssignableFrom(Short.class)) {
            return NbtShort.of(source.getShort(o));
        } else if (typeClass.isAssignableFrom(Long.class)) {
            return NbtLong.of(source.getLong(o));
        } else if (typeClass.isAssignableFrom(Float.class)) {
            return NbtFloat.of(source.getFloat(o));
        } else if (typeClass.isAssignableFrom(Double.class)) {
            return NbtDouble.of(source.getDouble(o));
        } else if (typeClass.isAssignableFrom(Integer[].class)) {
            return new NbtIntArray((int[]) source.get(o));
        } else if (typeClass.isAssignableFrom(Byte[].class)) {
            return new NbtByteArray((byte[]) source.get(o));
        } else if (typeClass.isAssignableFrom(Long[].class)) {
            return new NbtLongArray((long[]) source.get(o));
        } else if (typeClass.isAssignableFrom(String.class)) {
            return NbtString.of(String.valueOf(source.get(o)));
        } else if (typeClass.isAssignableFrom(List.class)) {
            List<AbstractNBTSerializable> list = (List) source.get(o);
            var obj = new NbtList();
            for (var item : list) {
                obj.add(item.toNbt());
            }
            return obj;
        } else {
            var obj = typeClass.cast(source.get(o));
            return serialize(obj);
        }
    }
}
