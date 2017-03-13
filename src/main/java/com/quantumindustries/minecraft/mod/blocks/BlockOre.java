package com.quantumindustries.minecraft.mod.blocks;

import com.quantumindustries.minecraft.mod.items.ItemOreDict;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.oredict.OreDictionary;

public class BlockOre extends BlockBase implements ItemOreDict {

    private String oreName;

    public BlockOre(String name, String oreName, float hardness, float resistance) {
        super(Material.ROCK, name);

        this.oreName = oreName;

        setHardness(hardness);
        setResistance(resistance);
    }

    @Override
    public void initOreDict() {
        OreDictionary.registerOre(oreName, this);
    }

    @Override
    public BlockOre setCreativeTab(CreativeTabs tab) {
        super.setCreativeTab(tab);
        return this;
    }

}
