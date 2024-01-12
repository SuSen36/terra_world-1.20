package com.susen36.terraworld.dimension;


import com.susen36.terraworld.TerraWorld;
import com.susen36.terraworld.init.BlocksInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.susen36.terraworld.block.NetherDoorBlock.HALF;

public class DimensionTeleport implements ITeleporter {

    private BlockPos pos;

    public DimensionTeleport(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity e = repositionEntity.apply(false);

        LevelChunk chunk = (LevelChunk) destWorld.getChunk(pos);
        Vec3 spawnPos = findPortalInChunk(chunk);

        if (spawnPos == null) {
            if (destWorld.dimension().equals(TerraWorld.TERRA_DIMENSION)) {
                spawnPos = placeDoorTerra(destWorld, chunk);
            } else {
                spawnPos = placeDoorverworld(destWorld, chunk);
            }
        }
        if (spawnPos == null) {
            return e;
        }

        e.teleportTo(spawnPos.x(), spawnPos.y(), spawnPos.z());
        return e;
    }

    @Nullable
    private Vec3 findPortalInChunk(LevelChunk chunk) {
        return chunk.getBlockEntities()
                .entrySet()
                .stream()
                .sorted((o1, o2) -> {
                        return Integer.compare(o1.getKey().getY(), o2.getKey().getY());

                })
                .map(Map.Entry::getKey)
                .map(pos -> getTeleporterSpawnPos(chunk.getLevel(), pos))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private static Vec3 getTeleporterSpawnPos(Level level, BlockPos blockPos) {
        return DismountHelper.findSafeDismountLocation(EntityType.PLAYER, level, blockPos.above(), false);
    }

    private Vec3 placeDoorTerra(ServerLevel world, LevelChunk chunk) {
        boolean deep = true;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        int min = world.getMinBuildHeight();
        int max = world.getMaxBuildHeight();
        for (int y = min; y < max - 1; y = y + 1) {
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    pos.set(x, y, z);
                    if (isAirOrDoor(chunk, pos) && isAirOrDoor(chunk, pos.above(1)) && !chunk.getBlockState(pos.below()).isAir()) {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos.getX(), pos.getY(), pos.getZ());
                        world.setBlockAndUpdate(absolutePos, BlocksInit.NETHER_DOOR.get().defaultBlockState());
                        world.setBlockAndUpdate(absolutePos.above(), BlocksInit.NETHER_DOOR.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER));

                        return new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + 1, absolutePos.getZ() + 0.5);
                    }
                }
            }
        }

        for (int y = min; y < max - 1; y = y + 1) {
            for (int x = 0; x < 8; x++) {
                for (int z = 0; z < 8; z++) {
                    pos.set(x, y, z);
                    if (isAirOrDoor(chunk, pos) && isAirOrDoor(chunk, pos.above(1))&& !chunk.getBlockState(pos.below()).isAir()) {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos.getX(), pos.getY(), pos.getZ());
                            world.setBlockAndUpdate(absolutePos, BlocksInit.NETHER_DOOR.get().defaultBlockState());
                            world.setBlockAndUpdate(absolutePos.above(), BlocksInit.NETHER_DOOR.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER));
                     return new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + 1, absolutePos.getZ() + 0.5);
                        }
                    }
                }
            }
        return null;
    }

    private boolean isAirOrDoor(LevelChunk chunk, BlockPos pos) {
        BlockState state = chunk.getBlockState(pos);
        return state.isAir()|| state.getBlock().equals(BlocksInit.NETHER_DOOR.get());
    }


    private Vec3 placeDoorverworld(ServerLevel world, LevelChunk chunk) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 8; x++) {
            for (int z = 0; z < 8; z++) {
                for (int y = world.getMaxBuildHeight(); y >= world.getMinBuildHeight(); y--) {
                    pos.set(x, y, z);
                    if (isAirOrDoor(chunk, pos) && isAirOrDoor(chunk, pos.above(1)) && !isAirOrDoor(chunk, pos.below(1))) {
                        BlockPos absolutePos = chunk.getPos().getWorldPosition().offset(pos.getX(), pos.getY(), pos.getZ());
                        world.setBlockAndUpdate(absolutePos, BlocksInit.NETHER_DOOR.get().defaultBlockState());
                        world.setBlockAndUpdate(absolutePos.above(), BlocksInit.NETHER_DOOR.get().defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER));

                        return new Vec3(absolutePos.getX() + 0.5, absolutePos.getY() + 1, absolutePos.getZ() + 0.5);
                    }
                }
            }
        }
        return null;
    }

}
