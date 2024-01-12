package com.susen36.terraworld.block;

import com.susen36.terraworld.TerraWorld;
import com.susen36.terraworld.dimension.DimensionTeleport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class NetherDoorBlock  extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    protected static final VoxelShape LATITUDE_AABB = Shapes.or(box(0.0D, 0.0D, 6.0D, 2.0D, 12.0D, 10.0D), box(14.0D, 0.0D, 6.0D, 16.0D, 12.0D, 10.0D),box(0.0D,0.0D,6.0D,16.0D,1.0D,10.0D));
    protected static final VoxelShape LATITUDE_TOP_AABB = Shapes.or(box(0.0D, 0.0D, 6.0D, 2.0D, 12.0D, 10.0D), box(14.0D, 0.0D, 6.0D, 16.0D, 12.0D, 10.0D),box(0.0D,15.0D,6.0D,16.0D,16.0D,10.0D));

    protected static final VoxelShape LONGITUDE_AABB = Shapes.or(box(6.0D, 0.0D, 0.0D, 10.0D, 12.0D, 2.0D), box(6.0D, 0.0D, 14.0D, 10.0D, 12.0D, 16.0D),box(6.0D,0.0D,0.0D,10.0D,1.0D,16.0D));
    protected static final VoxelShape LONGITUDE_TOP_AABB = Shapes.or(box(6.0D, 0.0D, 0.0D, 10.0D, 12.0D, 2.0D), box(6.0D, 0.0D, 14.0D, 10.0D, 12.0D, 16.0D),box(6.0D,15.0D,0.0D,10.0D,16.0D,16.0D));


    public NetherDoorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, Boolean.valueOf(false)).setValue(POWERED, Boolean.valueOf(false)).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @NotNull
    public VoxelShape getShape(BlockState blockState, BlockGetter p_52808_, BlockPos p_52809_, CollisionContext p_52810_) {
        Direction direction = blockState.getValue(FACING);
        DoubleBlockHalf half = blockState.getValue(HALF);
        return switch (direction) {
            default ->  half == DoubleBlockHalf.LOWER ? LONGITUDE_AABB :LONGITUDE_TOP_AABB;
            case SOUTH, NORTH -> half == DoubleBlockHalf.LOWER ? LATITUDE_AABB : LATITUDE_TOP_AABB;
        };
    }
    @NotNull
    public BlockState updateShape(BlockState p_52796_, Direction p_52797_, BlockState blockState, LevelAccessor p_52799_, BlockPos p_52800_, BlockPos p_52801_) {
        DoubleBlockHalf doubleblockhalf = p_52796_.getValue(HALF);
        if (p_52797_.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (p_52797_ == Direction.UP)) {
            return blockState.is(this) && blockState.getValue(HALF) != doubleblockhalf ? p_52796_.setValue(FACING, blockState.getValue(FACING)).setValue(OPEN, blockState.getValue(OPEN)).setValue(POWERED, blockState.getValue(POWERED)) : Blocks.AIR.defaultBlockState();
        } else {
            return  super.updateShape(p_52796_, p_52797_, blockState, p_52799_, p_52800_, p_52801_);
        }
    }


    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext p_52739_) {
        BlockPos blockpos = p_52739_.getClickedPos();
        Level level = p_52739_.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(p_52739_)) {
            boolean flag = level.hasNeighborSignal(blockpos) || level.hasNeighborSignal(blockpos.above());
            return this.defaultBlockState().setValue(FACING, p_52739_.getHorizontalDirection()).setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag)).setValue(HALF, DoubleBlockHalf.LOWER);
        } else {
            return null;
        }
    }

    public void setPlacedBy(Level p_52749_, BlockPos p_52750_, BlockState p_52751_, LivingEntity p_52752_, ItemStack p_52753_) {
        p_52749_.setBlock(p_52750_.above(), p_52751_.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @NotNull
    public InteractionResult use(BlockState blockState, Level level, BlockPos p_52771_, Player player, InteractionHand interactionHand, BlockHitResult p_52774_) {
        ItemStack itemStack = player.getMainHandItem();
        if(!player.isSecondaryUseActive() && (itemStack.getItem() instanceof FlintAndSteelItem|| this.isOpen(blockState))) {
            blockState = blockState.cycle(OPEN);
            level.setBlock(p_52771_, blockState, 10);
            this.playSound(player, level, p_52771_, blockState.getValue(OPEN));
            level.gameEvent(player, this.isOpen(blockState) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, p_52771_);
            itemStack.hurtAndBreak(1, player, (p_41300_) -> {
                p_41300_.broadcastBreakEvent(player.getUsedItemHand());
            });
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
            return InteractionResult.PASS;
    }

    public boolean isOpen(BlockState p_52816_) {
        return p_52816_.getValue(OPEN);
    }
    @Nullable
    public void setOpen( Entity p_153166_, Level p_153167_, BlockState p_153168_, BlockPos p_153169_, boolean p_153170_) {
        if (p_153168_.is(this) && p_153168_.getValue(OPEN) != p_153170_) {
            p_153167_.setBlock(p_153169_, p_153168_.setValue(OPEN, Boolean.valueOf(p_153170_)), 10);
            this.playSound(p_153166_, p_153167_, p_153169_, p_153170_);
            p_153167_.gameEvent(p_153166_, p_153170_ ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, p_153169_);
        }
    }

    public void neighborChanged(BlockState p_52776_, @NotNull Level p_52777_, BlockPos p_52778_, Block p_52779_, BlockPos p_52780_, boolean p_52781_) {
        boolean flag = p_52777_.hasNeighborSignal(p_52778_) || p_52777_.hasNeighborSignal(p_52778_.relative(p_52776_.getValue(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN));
        if (!this.defaultBlockState().is(p_52779_) && flag != p_52776_.getValue(POWERED)) {
            if (flag != p_52776_.getValue(OPEN)) {
                this.playSound((Entity)null, p_52777_, p_52778_, flag);
                p_52777_.gameEvent((Entity)null, flag ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, p_52778_);
            }

            p_52777_.setBlock(p_52778_, p_52776_.setValue(POWERED, Boolean.valueOf(flag)).setValue(OPEN, Boolean.valueOf(flag)), 2);
        }

    }

    public boolean canSurvive(BlockState p_52783_, LevelReader p_52784_, BlockPos p_52785_) {
        return true;
    }

    private void playSound(@Nullable Entity p_251616_, Level p_249656_, BlockPos p_249439_, boolean p_251628_) {
        p_249656_.playSound(p_251616_, p_249439_, p_251628_ ? SoundEvents.FLINTANDSTEEL_USE : SoundEvents.PORTAL_TRIGGER, SoundSource.BLOCKS, 1.0F, p_249656_.getRandom().nextFloat() * 0.1F + 0.2F);
    }

    public BlockState rotate(BlockState p_52790_, Rotation p_52791_) {
        return p_52790_.setValue(FACING, p_52791_.rotate(p_52790_.getValue(FACING)));
    }

    public long getSeed(BlockState blockState, BlockPos blockPos) {
        return Mth.getSeed(blockPos.getX(), blockPos.below(blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), blockPos.getZ());
    }

    public void entityInside(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, Entity entity) {
        if (entity.canChangeDimensions() && this.isOpen(blockState)) {
            double d =0.5f;

            if (entity.distanceToSqr(pos.getX()+d,pos.getY(),pos.getZ()+d)<=0.15f) {
                this.transferPlayer(entity, pos);
            }
        }
    }
    public void transferPlayer(Entity entity, BlockPos pos) {
        if (entity.isOnPortalCooldown()) {
            entity.setPortalCooldown();
        } else if (entity.level() instanceof ServerLevel serverLevel){
            MinecraftServer minecraftserver = serverLevel.getServer();
            ResourceKey<Level> resourcekey = entity.level().dimension() == TerraWorld.TERRA_DIMENSION ? Level.OVERWORLD : TerraWorld.TERRA_DIMENSION;
            ServerLevel serverlevel1 = minecraftserver.getLevel(resourcekey);
            if (serverlevel1 == null) {
                TerraWorld.LOGGER.error("Could not find dimension.");
            }else {
                entity.changeDimension(serverlevel1,new DimensionTeleport(pos));
            }
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_52803_) {
        p_52803_.add(HALF, FACING, OPEN, POWERED);
    }

}