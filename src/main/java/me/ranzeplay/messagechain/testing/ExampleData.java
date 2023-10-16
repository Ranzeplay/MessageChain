package me.ranzeplay.messagechain.testing;

import lombok.*;
import me.ranzeplay.messagechain.models.AbstractNBTSerializable;
import net.minecraft.nbt.NbtCompound;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExampleData extends AbstractNBTSerializable {
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
