package me.ranzeplay.messagechain.nbtutils;

import lombok.SneakyThrows;
import net.minecraft.nbt.*;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.Objects;

public class NBTHelper {
    @SneakyThrows
    public static <T> NbtCompound serialize(T source) {
        var clazz = source.getClass();
        if (clazz.isAnnotationPresent(NBTSerializable.class)) {
            return serializeClass(source);
        } else {
            throw new ReflectiveOperationException("NBTSerializable annotation required.");
        }
    }

    private static NbtElement serializeObject(Object source) {
        var result = serializeBasicType(source);
        if (result == null) return serializeClass(source);
        else return result;
    }

    private static NbtCompound serializeClass(Object source) {
        var typeClass = source.getClass();
        if (typeClass.isAnnotationPresent(NBTSerializable.class)) {
            var result = new NbtCompound();
            for (var field : typeClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(NBTSerializableEntry.class)) {
                    var element = serializeField(source, field);
                    var entryAnnotation = field.getAnnotation(NBTSerializableEntry.class);
                    result.put(Objects.equals(entryAnnotation.key(), "") ? field.getName() : entryAnnotation.key(), element);
                }
            }

            return result;
        } else {
            return null;
        }
    }

    @SneakyThrows
    private static NbtElement serializeField(Object o, Field source) {
        source.setAccessible(true);
        return serializeObject(source.get(o));
    }

    // TODO: Implement serialization for Map<K, V>
    private static NbtElement serializeBasicType(Object source) {
        var typeClass = source.getClass();
        if (typeClass.isAssignableFrom(Integer.class)) {
            return NbtInt.of((int) source);
        } else if (typeClass.isAssignableFrom(Short.class)) {
            return NbtShort.of((short) source);
        } else if (typeClass.isAssignableFrom(Long.class)) {
            return NbtLong.of((long) source);
        } else if (typeClass.isAssignableFrom(Float.class)) {
            return NbtFloat.of((float) source);
        } else if (typeClass.isAssignableFrom(Double.class)) {
            return NbtDouble.of((double) source);
        } else if (typeClass.isAssignableFrom(Integer[].class)) {
            return new NbtIntArray((int[]) source);
        } else if (typeClass.isAssignableFrom(Byte[].class)) {
            return new NbtByteArray((byte[]) source);
        } else if (typeClass.isAssignableFrom(Long[].class)) {
            return new NbtLongArray((long[]) source);
        } else if (typeClass.isAssignableFrom(String.class)) {
            return NbtString.of(String.valueOf(source));
        } else if (typeClass.getSuperclass() == AbstractList.class) {
            AbstractList list = (AbstractList) source;
            var obj = new NbtList();
            for (var item : list) {
                obj.add(serializeObject(item));
            }
            return obj;
        } else {
            return null;
        }
    }
}
