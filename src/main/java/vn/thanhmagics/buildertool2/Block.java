package vn.thanhmagics.buildertool2;

import org.bukkit.Material;

public class Block {

    private int x, y, z;

    private String material;

    public Block(int x, int y, int z, String material) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.material = material;
    }

    public Material getMaterial() {
        return Material.valueOf(material);
    }

    @Override
    public String toString() {
        return x + " | " + y + " | " + z + " | " + material;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
