package me.ranzeplay.messagechain.nbtutils;


import net.minecraft.nbt.NbtCompound;

/**
 * Implements deserialization and serialization methods of a data structure.
 * A helper that converts data to or from NBT.
 */
public abstract class AbstractNBTSerializable {
    /**
     * Empty constructor that initializes a nearly null instance for filling data.
     */
    public AbstractNBTSerializable() {
    }

    /**
     * @return Serialized NBT structure.
     */
    public abstract NbtCompound toNbt();

    /**
     * Loads current object instance with NBT data.
     *
     * @param nbt Source data
     */
    public abstract void fromNbt(NbtCompound nbt);

    public AbstractNBTSerializable loadFromNbt(NbtCompound nbt) {
        fromNbt(nbt);
        return this;
    }

    /**
     * Get implemented class
     *
     * @return Implemented
     */
    public abstract Class<?> getGenericClass();
}
