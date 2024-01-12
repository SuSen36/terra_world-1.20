package com.susen36.terraworld.mixin;

import com.susen36.terraworld.TerraWorld;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractPiglin.class)
public abstract class AbstractPiglinMixin  extends Monster {

    @Shadow protected abstract boolean isImmuneToZombification();

    protected AbstractPiglinMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    /**
     * @author susen36
     * @reason makePiglinConvertWhenYIsHigherThan0
     */
    @Overwrite
    public boolean isConverting() {
        boolean flag = this.level().dimension().equals(TerraWorld.TERRA_DIMENSION);
        return (!this.level().dimensionType().piglinSafe()||(flag && this.getY()>0)) && !this.isImmuneToZombification() && !this.isNoAi();
    }
}
