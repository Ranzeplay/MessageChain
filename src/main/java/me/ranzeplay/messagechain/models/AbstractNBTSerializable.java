package me.ranzeplay.messagechain.models;


import net.minecraft.nbt.NbtCompound;

public abstract class AbstractNBTSerializable {
    public AbstractNBTSerializable() {
    }

    public abstract NbtCompound toNbt();

    public abstract void fromNbt(NbtCompound nbt);

    public AbstractNBTSerializable loadFromNbt(NbtCompound nbt) {
        fromNbt(nbt);
        return this;
    }

    public abstract Class<?> getGenericClass();
}
