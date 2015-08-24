package sj.android.particles;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by Administrator on 2015/8/24.
 */
public class ParticleShaderProgram extends ShaderProgram{
    private final int uMatrixLocation;
    private final int uTimeLocation;
    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aDirectionVectorLocation;
    private final int aParticleStartTimeLocation;

    protected ParticleShaderProgram(Context context) {
        super(context, R.raw.particle_vertex_shader, R.raw.particle_fragment_shader);
        uMatrixLocation = GLES20.glGetUniformLocation(program,U_MATRIX);
        uTimeLocation = GLES20.glGetUniformLocation(program,U_TIME);

        aPositionLocation = GLES20.glGetAttribLocation(program,A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(program,A_COLOR);
        aDirectionVectorLocation=GLES20.glGetAttribLocation(program,A_DIRECTION_VECTOR);
        aParticleStartTimeLocation=GLES20.glGetAttribLocation(program,A_PARTICLE_START_TIME);
    }
    public void setUniforms(float[] matrix,float elapsedTime)
    {
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        GLES20.glUniform1f(uTimeLocation,elapsedTime);
    }
    public int getPositionAttributeLocaton()
    {
        return aPositionLocation;
    }
    public int getColorLocation()
    {
        return aColorLocation;
    }
    public int getDirectionVectorLocation()
    {return aDirectionVectorLocation;}

    public  int getParticleStartTimeLocation()
    {return aParticleStartTimeLocation;}


}
