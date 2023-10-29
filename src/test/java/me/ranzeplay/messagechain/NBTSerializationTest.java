package me.ranzeplay.messagechain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.ranzeplay.messagechain.nbtutils.NBTHelper;
import me.ranzeplay.messagechain.nbtutils.NBTSerializable;
import me.ranzeplay.messagechain.nbtutils.NBTSerializableEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTSerializationTest {
    @AllArgsConstructor
    @NBTSerializable
    @NoArgsConstructor
    static class MasterContent {
        @NBTSerializableEntry
        String a;
        @NBTSerializableEntry
        long b;
        @NBTSerializableEntry
        ArrayList<SubContent> subContents;
        @NBTSerializableEntry
        ArrayList<String> stringArrayList;
        @NBTSerializableEntry
        SubContent sub;
    }

    @AllArgsConstructor
    @NBTSerializable
    @NoArgsConstructor
    static class SubContent {

        @NBTSerializableEntry
        int a;
        @NBTSerializableEntry
        int b;
        @NBTSerializableEntry
        int[] arr;
    }

    MasterContent content;

    @BeforeEach
    public void setup() {
        var data = new MasterContent("hello", 233, new ArrayList<>(), new ArrayList<>(), new SubContent(2, 3, new int[]{2, 3, 4}));
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
}
