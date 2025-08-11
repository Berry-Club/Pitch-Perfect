package dev.aaronhowser.mods.pitchperfect.util

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import io.netty.buffer.ByteBuf
import net.minecraft.core.Vec3i
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import java.util.*

object OtherUtil {

	fun Number.map(min1: Float, max1: Float, min2: Float, max2: Float): Float {
		return min2 + (max2 - min2) * ((this.toFloat() - min1) / (max1 - min1))
	}

	fun modResource(path: String): ResourceLocation {
		return ResourceLocation.fromNamespaceAndPath(PitchPerfect.ID, path)
	}

	fun getNearbyLivingEntities(originEntity: LivingEntity, range: Double): List<LivingEntity> {
		return originEntity.level().getEntities(
			originEntity,
			AABB(
				originEntity.x - range,
				originEntity.y - range,
				originEntity.z - range,
				originEntity.x + range,
				originEntity.y + range,
				originEntity.z + range
			)
		).filterIsInstance<LivingEntity>()
	}

	fun CompoundTag.getUuidOrNull(key: String): UUID? {
		return if (this.hasUUID(key)) this.getUUID(key) else null
	}

	val UUID_CODEC: Codec<UUID> = Codec.STRING.xmap(
		UUID::fromString,
		UUID::toString
	)

	val UUID_STREAM_CODEC: StreamCodec<ByteBuf, UUID> = ByteBufCodecs.STRING_UTF8.map(
		UUID::fromString,
		UUID::toString
	)

	fun Vec3i.toVec3(): Vec3 {
		return Vec3(this.x.toDouble(), this.y.toDouble(), this.z.toDouble())
	}

	operator fun Vec3.component1(): Double = x
	operator fun Vec3.component2(): Double = y
	operator fun Vec3.component3(): Double = z

}