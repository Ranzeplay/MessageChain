package me.ranzeplay.messagechain.nbtutils;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import net.minecraft.nbt.*;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

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
                if (field.isAnnotationPresent(NBTSerializationEntry.class)) {
                    var element = serializeField(source, field);
                    result.put(getNbtFieldName(field), element);
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
        } else if (typeClass.isAssignableFrom(int[].class)) {
            return new NbtIntArray((int[]) source);
        } else if (typeClass.isAssignableFrom(byte[].class)) {
            return new NbtByteArray((byte[]) source);
        } else if (typeClass.isAssignableFrom(long[].class)) {
            return new NbtLongArray((long[]) source);
        } else if (typeClass.isAssignableFrom(Boolean.class)) {
            return NbtByte.of((boolean) source);
        } else if (typeClass.isAssignableFrom(String.class)) {
            return NbtString.of(String.valueOf(source));
        } else if (typeClass.getSuperclass() == AbstractList.class) {
            AbstractList list = (AbstractList) source;
            var obj = new NbtCompound();
            // Put content type to the first
            if (list.isEmpty()) {
                obj.putString("type", "?");
            } else {
                obj.putString("type", list.get(0).getClass().getTypeName());
            }
            var nbtList = new NbtList();
            for (var item : list) {
                nbtList.add(serializeObject(item));
            }
            obj.put("items", nbtList);
            return obj;
        } else if (typeClass.getSuperclass() == AbstractMap.class) {
            Map map = (Map) source;
            var obj = new NbtCompound();
            if (map.isEmpty()) {
                obj.putString("type", "?");
            } else {
                var it = map.entrySet().iterator();
                var list = new NbtList();
                while (it.hasNext()) {
                    var itemComp = new NbtCompound();
                    var entry = (Map.Entry) it.next();
                    var keySerializationResult = serializeObject(entry.getKey());
                    var valueSerializationResult = serializeObject(entry.getValue());
                    if (keySerializationResult != null && valueSerializationResult != null) {
                        itemComp.put("key", keySerializationResult);
                        itemComp.put("value", valueSerializationResult);
                        list.add(itemComp);

                        if (!obj.contains("type")) {
                            obj.putString("type", String.format("%s : %s", entry.getKey().getClass().getTypeName(), entry.getValue().getClass().getTypeName()));
                        }
                    } else {
                        return obj;
                    }
                }

                obj.put("items", list);
            }

            return obj;
        } else {
            return null;
        }
    }

    @SneakyThrows
    public static <T> T deserialize(NbtCompound nbt, Class<T> targetType) {
        return deserializeClass(nbt, targetType);
    }

    private static Object deserializeObject(NbtElement element, Class<?> targetType) {
        var result = deserializeBasicType(element, targetType);
        if (result == null) return deserializeClass((NbtCompound) element, targetType);
        else return result;
    }

    @SneakyThrows
    private static Object deserializeBasicType(NbtElement element, Class<?> targetType) {
        if (targetType.isAssignableFrom(int.class) || targetType.isAssignableFrom(Integer.class)) {
            return ((NbtInt) element).intValue();
        } else if (targetType.isAssignableFrom(short.class) || targetType.isAssignableFrom(Short.class)) {
            return ((NbtShort) element).shortValue();
        } else if (targetType.isAssignableFrom(long.class) || targetType.isAssignableFrom(Long.class)) {
            return ((NbtLong) element).longValue();
        } else if (targetType.isAssignableFrom(float.class) || targetType.isAssignableFrom(Float.class)) {
            return ((NbtFloat) element).floatValue();
        } else if (targetType.isAssignableFrom(double.class) || targetType.isAssignableFrom(Double.class)) {
            return ((NbtDouble) element).doubleValue();
        } else if (targetType.isAssignableFrom(int[].class)) {
            return ((NbtIntArray) element).getIntArray();
        } else if (targetType.isAssignableFrom(byte[].class)) {
            return ((NbtByteArray) element).getByteArray();
        } else if (targetType.isAssignableFrom(long[].class)) {
            return ((NbtLongArray) element).getLongArray();
        } else if (targetType.isAssignableFrom(boolean.class) || targetType.isAssignableFrom(Boolean.class)) {
            return ((NbtByte) element).byteValue() != 0;
        } else if (targetType.isAssignableFrom(String.class)) {
            return element.asString();
        } else if (targetType.getSuperclass() == AbstractList.class) {
            var comp = (NbtCompound) element;
            var typeName = comp.getString("type");
            var constructor = targetType.getDeclaredConstructor();
            constructor.setAccessible(true);
            var result = (List) constructor.newInstance();

            if (!typeName.equalsIgnoreCase("?")) {
                var list = (NbtList) comp.get("items");
                for (var item : list) {
                    result.add(deserializeObject(item, Class.forName(typeName)));
                }
            }
            return result;
        } else if (targetType.getSuperclass() == AbstractMap.class) {
            var comp = (NbtCompound) element;
            var kvType = comp.getString("type").split(" : ");
            var keyTypeName = kvType[0];
            var valueTypeName = kvType[1];
            var constructor = targetType.getConstructor();
            constructor.setAccessible(true);
            var result = (AbstractMap) constructor.newInstance();

            if (!keyTypeName.equalsIgnoreCase("?") && !valueTypeName.equalsIgnoreCase("?")) {
                var list = (NbtList) comp.get("items");
                for (var item : list) {
                    var itemComp = (NbtCompound) item;
                    result.put(deserializeObject(itemComp.get("key"), Class.forName(keyTypeName)),
                            deserializeObject(itemComp.get("value"), Class.forName(valueTypeName)));
                }
            }

            return result;
        } else {
            return null;
        }
    }

    @SneakyThrows
    private static <T> T deserializeClass(NbtCompound nbt, Class<T> targetType) {
        if (targetType.isAnnotationPresent(NBTSerializable.class)) {
            var constructor = targetType.getDeclaredConstructor();
            constructor.setAccessible(true);
            var result = constructor.newInstance();

            var fields = Arrays.stream(targetType.getDeclaredFields())
                    .filter(f -> f.isAnnotationPresent(NBTSerializationEntry.class))
                    .collect(Collectors.toCollection(ArrayList::new));
            for (var field : fields) {
                var subElement = nbt.get(getNbtFieldName(field));
                var clazz = field.getType();
                field.set(result, deserializeObject(Objects.requireNonNull(subElement), clazz));
            }

            return result;
        }
        return null;
    }

    private static String getNbtFieldName(Field field) {
        field.setAccessible(true);
        var entryAnnotation = field.getAnnotation(NBTSerializationEntry.class);
        return Objects.equals(entryAnnotation.key(), "") ? field.getName() : entryAnnotation.key();
    }

    public static NbtCompound serializeUsingJson(Object source) {
        var result = new NbtCompound();
        var json = new Gson().toJson(source);
        result.putString("json", json);

        return result;
    }

    public static <T> T deserializeUsingJson(NbtCompound nbt, Class<T> targetType) {
        var json = nbt.getString("json");
        return new Gson().fromJson(json, targetType);
    }
}
