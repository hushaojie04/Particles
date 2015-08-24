package sj.android.particles;


/**
 * Created by Administrator on 2015/8/24.
 */
public class ParticleSystem {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int VECTOR_COMPONENT_COUNT = 3;
    private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;
    private static final int TOTOL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT + VECTOR_COMPONENT_COUNT + PARTICLE_START_TIME_COMPONENT_COUNT;
    private static final int STRIDE = TOTOL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;

    private final float[] particles;
    private final VertexArray vertexArray;
    private final int maxParticleCount;

    private int currentParticleCount;
    private int nextParticle;

    public ParticleSystem(int maxParticleCount) {
        particles = new float[maxParticleCount * TOTOL_COMPONENT_COUNT];
        vertexArray = new VertexArray(particles);
        this.maxParticleCount = maxParticleCount;
    }

    public void addParticle(Geometry.Point point, int color, Geometry.Vector direction, float particleStartTime) {
        final int particleOffset = nextParticle * TOTOL_COMPONENT_COUNT;
        int currentOffset = particleOffset;
        nextParticle++;
        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }
        if (nextParticle == maxParticleCount) {
            nextParticle = 0;
        }
    }

}
