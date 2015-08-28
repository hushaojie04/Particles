package sj.android.particles;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2015/8/28.
 */
public class VertexBuffer {
    private final int bufferId;
    public VertexBuffer(float[] vertexData)
    {
        final int  buffers[] = new int[1];
        GLES20.glGenBuffers(buffers.length, buffers, 0);
        if(buffers[0]==0)
        {
            throw new RuntimeException("Count not create a new vertex buffer object");
        }
        bufferId = buffers[0];
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,buffers[0]);

        FloatBuffer vertexArray = ByteBuffer.allocateDirect(vertexData.length * Constants.BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexArray.position(0);

        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                vertexArray.capacity()*Constants.BYTES_PER_FLOAT,
                vertexArray,GLES20.GL_STATIC_DRAW);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
    }

    public void setVertexAttribPointer(int dataOffset,int attributeLocation,int componentCount,int stride)
    {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,bufferId);
        GLES20.glVertexAttribPointer(attributeLocation,componentCount,GLES20.GL_FLOAT,false,stride,dataOffset);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,0);
    }

}
