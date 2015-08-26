package sj.android.particles;

import android.opengl.GLES20;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2015/8/26.
 */
public class Skybox {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private final VertexArray vertexArray;
    private final ByteBuffer indexArray;
    public Skybox()
    {
        vertexArray = new VertexArray(new float[]{
                -1,1,1,
                1,1,1,
                -1,-1,1,
                1,-1,1,
                -1,1,-1,
                1,1,-1,
                -1,-1,-1,
                1,-1,-1
        });

        indexArray=ByteBuffer.allocateDirect(6*6).put(new byte[]{
                1,3,0,
                0,3,2,

                4,6,5,
                5,6,7,

                0,2,4,
                4,2,6,
                
                5,7,1,
                1,7,3,

                5,1,4,
                4,1,0,

                6,2,7,
                7,2,3
        });
        indexArray.position(0);
    }
    public void bindData(SkyboxShaderProgram skyboxShaderProgram)
    {
        vertexArray.setVertexAttribPointer(0,skyboxShaderProgram.getPositionAttributeLocation(),POSITION_COMPONENT_COUNT,0);
    }

    public void draw()
    {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES,36,GLES20.GL_UNSIGNED_BYTE,indexArray);
    }

}
