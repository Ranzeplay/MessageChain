package me.ranzeplay.messagechain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;
import me.ranzeplay.messagechain.nbtutils.NBTHelper;
import me.ranzeplay.messagechain.nbtutils.NBTSerializable;
import me.ranzeplay.messagechain.nbtutils.NBTSerializationEntry;
import net.minecraft.nbt.NbtCompound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTSerializationTest {
    @AllArgsConstructor
    @NBTSerializable
    @NoArgsConstructor
    static class MasterContent {
        @NBTSerializationEntry
        String a;
        @NBTSerializationEntry
        long b;
        @NBTSerializationEntry
        ArrayList<SubContent> subContents;
        @NBTSerializationEntry
        ArrayList<String> stringArrayList;
        @NBTSerializationEntry
        SubContent sub;
        @NBTSerializationEntry
        HashMap<Integer, String> mapStore;
    }

    @AllArgsConstructor
    @NBTSerializable
    @NoArgsConstructor
    static class SubContent {
        @NBTSerializationEntry
        int a;
        @NBTSerializationEntry
        int b;
        @NBTSerializationEntry
        int[] arr;
    }

    @AllArgsConstructor
    @NBTSerializable
    @NoArgsConstructor
    static class Another extends AbstractNBTSerializable {
        @NBTSerializationEntry
        int a;
        @NBTSerializationEntry
        String b;

        @Override
        public NbtCompound toNbt() {
            return NBTHelper.serialize(this);
        }

        @Override
        public void fromNbt(NbtCompound nbt) {
            var deserializationResult = NBTHelper.deserialize(nbt, Another.class);
            a = deserializationResult.a;
            b = deserializationResult.b;
        }

        @Override
        public Class<?> getGenericClass() {
            return Another.class;
        }
    }

    MasterContent content;

    @BeforeEach
    public void setup() {
        var map = new HashMap<Integer, String>();
        map.put(1, "foo");
        map.put(2, "bar");

        var data = new MasterContent("hello", 233, new ArrayList<>(), new ArrayList<>(), new SubContent(2, 3, new int[]{2, 3, 4}), map);
        data.stringArrayList.add("MessageChain");
        data.stringArrayList.add("hNation");
        data.stringArrayList.add("Jeb Feng");
        data.subContents.add(new SubContent(1, 4, new int[]{-2, -3, -4}));
        data.subContents.add(new SubContent(5, -2, new int[]{0, 0, 0}));

        content = data;
    }

    @Test
    public void testSerialization() {
        var result = NBTHelper.serialize(content);
        assertEquals(233, result.getLong("b"));
    }

    @Test
    public void testDeserialization() {
        var nbt = NBTHelper.serialize(content);
        var result = NBTHelper.deserialize(nbt, MasterContent.class);

        assertEquals(233, result.b);
    }

    @Test
    public void testInheritance() {
        var model = new Another(3, "another");
        var nbt = model.toNbt();
        var back = new Another();
        back.fromNbt(nbt);

        assertEquals("another", back.b);
    }
}
