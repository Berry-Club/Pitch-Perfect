package dev.aaronhowser.mods.pitchperfect.utils

import com.aaronhowser.mods.pitchperfect.config.ClientConfig
import com.aaronhowser.mods.pitchperfect.config.ServerConfig
import com.aaronhowser.mods.pitchperfect.event.ModScheduler.scheduleSynchronisedTask
import com.aaronhowser.mods.pitchperfect.utils.ClientUtils.spawnParticle
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.phys.Vec3
import kotlin.math.max

class ParticleLine(
    private val originPositionVec: Vec3,
    private val destinationPositionVec: Vec3,
    private val particleType: SimpleParticleType,
) {

    private var iteration: Int = 1
    private val particlesPerBlock: Int = ClientConfig.ELECTRIC_PARTICLE_DENSITY.get()
    private val totalTravelTime: Int = ServerConfig.ELECTRIC_JUMP_TIME.get()


    fun spawnWave() {
        if (particlesPerBlock == 0) return
        if (totalTravelTime == 0) {
            spawnEntireLine()
            return
        }

        val pathVector = originPositionVec.vectorTo(destinationPositionVec)
        val pathSize = pathVector.length().toFloat()
        val totalParticleCount = (pathSize * particlesPerBlock.toFloat()).toInt()
        if (totalParticleCount == 0) return

        val distanceBetweenParticles = 1f / particlesPerBlock.toFloat()
        val deltaVector = pathVector.scale((1f / totalParticleCount).toDouble())
        val ticksPerParticle = max((totalTravelTime / totalParticleCount).toDouble(), 1.0).toInt()
        // Unfortunately, as spawning the next particle uses a tick scheduler, there's a maximum of 1 tick per particle
        // This may cause the particle wave to arrive significantly after the enchantment damages the next mob

        // 🦆
        // An example:
        // A mob at (0,10,0) is it, and the next targeted is at (3,10,0)
        // pathVector = (3,0,0)
        // pathSize = 3.0F
        // totalParticleCount = 3*3 = 9
        // distanceBetweenParticles = 1/3 = 0.333F
        spawnNextParticleInWave(deltaVector, ticksPerParticle, totalParticleCount)
    }

    private fun spawnNextParticleInWave(deltaVector: Vec3, ticksPerParticle: Int, totalParticleCount: Int) {
        if (iteration >= totalParticleCount) return
        val dx = deltaVector.x() * iteration
        val dy = deltaVector.y() * iteration
        val dz = deltaVector.z() * iteration
        spawnParticle(
            particleType,
            originPositionVec.x() + dx,
            originPositionVec.y() + dy,
            originPositionVec.z() + dz,
            1f, 1f, 1f
        )
        iteration++
        scheduleSynchronisedTask(ticksPerParticle) {
            spawnNextParticleInWave(
                deltaVector,
                ticksPerParticle,
                totalParticleCount
            )
        }
    }

    fun spawnEntireLine() {
        if (particlesPerBlock == 0) return

        scheduleSynchronisedTask(totalTravelTime) {
            val pathVector = originPositionVec.vectorTo(destinationPositionVec)
            val pathSize = pathVector.length().toFloat()
            val totalParticleCount = (pathSize * particlesPerBlock.toFloat()).toInt()
            if (totalParticleCount == 0) return@scheduleSynchronisedTask
            val deltaVector = pathVector.scale((1f / totalParticleCount).toDouble())
            for (i in 1..totalParticleCount) {
                val dx = deltaVector.x() * i
                val dy = deltaVector.y() * i
                val dz = deltaVector.z() * i

                spawnParticle(
                    particleType,
                    originPositionVec.x() + dx,
                    originPositionVec.y() + dy,
                    originPositionVec.z() + dz,
                    1f, 1f, 1f
                )
            }
        }
    }

}