package sj.android.particles;


import android.graphics.Color;
import android.opengl.GLES20;

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

    public void addParticle(Geometry.Point position, int color, Geometry.Vector direction, float particleStartTime) {
        final int particleOffset = nextParticle * TOTOL_COMPONENT_COUNT;
        int currentOffset = particleOffset;
        nextParticle++;
        if (currentParticleCount < maxParticleCount) {
            currentParticleCount++;
        }
        if (nextParticle == maxParticleCount) {
            nextParticle = 0;
        }

        particles[currentOffset++] = position.x;
        particles[currentOffset++] = position.y;
        particles[currentOffset++] = position.z;

        particles[currentOffset++] = Color.red(color) / 255;
        particles[currentOffset++] = Color.green(color) / 255;
        particles[currentOffset++] = Color.blue(color) / 255;

        particles[currentOffset++] = direction.x;
        particles[currentOffset++] = direction.y;
        particles[currentOffset++] = direction.z;

        particles[currentOffset++]=particleStartTime;

        vertexArray.updateBuffer(particles,particleOffset,TOTOL_COMPONENT_COUNT);
    }
    public void bindData(ParticleShaderProgram particleProgram)
    {
        int dataOffset = 0;
        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getPositionAttributeLocaton(),
                POSITION_COMPONENT_COUNT,STRIDE);
        dataOffset +=POSITION_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getColorLocation(),
                COLOR_COMPONENT_COUNT,STRIDE);
        dataOffset+=COLOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getDirectionVectorLocation(),
                VECTOR_COMPONENT_COUNT,STRIDE);
        dataOffset +=VECTOR_COMPONENT_COUNT;

        vertexArray.setVertexAttribPointer(dataOffset,
                particleProgram.getParticleStartTimeLocation(),
                PARTICLE_START_TIME_COMPONENT_COUNT,STRIDE);
    }

    public void draw()
    {
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,currentParticleCount);
    }

}
