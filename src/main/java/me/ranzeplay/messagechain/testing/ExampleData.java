package me.ranzeplay.messagechain.testing;

import lombok.*;
import me.ranzeplay.messagechain.nbtutils.AbstractNBTSerializable;
import me.ranzeplay.messagechain.nbtutils.NBTHelper;
import me.ranzeplay.messagechain.nbtutils.NBTSerializable;
import me.ranzeplay.messagechain.nbtutils.NBTSerializationEntry;
import net.minecraft.nbt.NbtCompound;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NBTSerializable
public class ExampleData extends AbstractNBTSerializable implements Cloneable {
    @NBTSerializationEntry
    private String message;
    @NBTSerializationEntry
    private boolean issueError;

    @SneakyThrows
    @Override
    public NbtCompound toNbt() {
        return NBTHelper.serialize(this.clone());
    }

    @Override
    public void fromNbt(NbtCompound nbt) {
        var result = NBTHelper.deserialize(nbt, ExampleData.class);
        this.message = result.message;
        this.issueError = result.issueError;
    }

    @Override
    public Class<ExampleData> getGenericClass() {
        return ExampleData.class;
    }
}
