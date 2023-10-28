package me.ranzeplay.messagechain.testing;

import lombok.*;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import me.ranzeplay.messagechain.nbtutils.NBTSerializable;
import me.ranzeplay.messagechain.nbtutils.NBTSerializableEntry;
import net.minecraft.nbt.NbtCompound;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@NBTSerializable
public class ExampleData extends AbstractNBTSerializable {
    @NBTSerializableEntry(key = "message")
    private String message;

    @Override
    public NbtCompound toNbt() {
        var nbt = new NbtCompound();
        nbt.putString("message", message);
        return nbt;
    }

    @Override
    public void fromNbt(NbtCompound nbt) {
        this.message = nbt.getString("message");
    }

    @Override
    public Class<ExampleData> getGenericClass() {
        return ExampleData.class;
    }
}
