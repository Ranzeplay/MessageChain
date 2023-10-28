package me.ranzeplay.messagechain;

import me.ranzeplay.messagechain.nbtutils.NBTHelper;
import me.ranzeplay.messagechain.testing.ExampleData;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBTSerializationTest {
    @Test
    public void testSerialization() {
        var data = new ExampleData("example");
        var result = NBTHelper.serialize(data);

        assertEquals(result.getString("message"), data.getMessage());
    }
}
