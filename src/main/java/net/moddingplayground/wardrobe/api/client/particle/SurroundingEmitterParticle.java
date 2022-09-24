package net.moddingplayground.wardrobe.api.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;

import static java.lang.Math.*;

@Environment(EnvType.CLIENT)
public class SurroundingEmitterParticle extends NoRenderParticle implements CustomEmitterParticle {
    private final Entity entity;
    private int emitterAge;
    private final int maxEmitterAge;
    private final ParticleEffect parameters;

    public SurroundingEmitterParticle(ClientWorld world, Entity entity, ParticleEffect parameters) {
        this(world, entity, parameters, 3);
    }

    public SurroundingEmitterParticle(ClientWorld world, Entity entity, ParticleEffect parameters, int maxEmitterAge) {
        this(world, entity, parameters, maxEmitterAge, entity.getVelocity());
    }

    public SurroundingEmitterParticle(ClientWorld world, Entity entity, ParticleEffect parameters, int maxEmitterAge, Vec3d velocity) {
        super(world, entity.getX(), entity.getBodyY(0.5), entity.getZ(), velocity.x, velocity.y, velocity.z);
        this.entity = entity;
        this.maxEmitterAge = maxEmitterAge;
        this.parameters = parameters;
        this.tick();
    }

    @Override
    public void tick() {
        for (int i = 0; i < 16; ++i) {
            double vx = this.random.nextFloat() * 2.0f - 1.0f;
            double vy = this.random.nextFloat() * 2.0f - 1.0f;
            double vz = this.random.nextFloat() * 2.0f - 1.0f;

            if ((vx * vx) + (vy * vy) + (vz * vz) > 1.0) {
                continue;
            }

            double x = this.entity.offsetX(vx * 1.2D);
            double y = this.entity.getBodyY(pow(this.random.nextFloat(), 4));
            double z = this.entity.offsetZ(vz * 1.2D);
            this.world.addParticle(this.parameters, false, x, y, z, vx, vy + 0.2, vz);
        }

        this.emitterAge++;
        if (this.emitterAge >= this.maxEmitterAge) {
            for (int i = 0; i < 128; ++i) {
                double vx = this.random.nextFloat() * 2.0f - 1.0f;
                double vy = this.random.nextFloat() * 2.0f - 1.0f;
                double vz = this.random.nextFloat() * 2.0f - 1.0f;

                if ((vx * vx) + (vy * vy) + (vz * vz) > 1.0) {
                    continue;
                }

                double x = this.entity.offsetX(vx * 3.5D);
                double y = this.entity.getBodyY(this.random.nextFloat() * 1.5D);
                double z = this.entity.offsetZ(vz * 3.5D);
                this.world.addParticle(this.parameters, false, x, y, z, vx, vy + 0.2, vz);
            }

            this.markDead();
        }
    }

    @Override
    public boolean isAlive() {
        return !this.dead;
    }
}
