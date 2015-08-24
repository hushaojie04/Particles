package sj.android.particles;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2015/8/9.
 */
public class BufferUtils {
    public static FloatBuffer getFloatBuffer(float[] vertexes)
    {
        FloatBuffer buffer;
        ByteBuffer qbb = ByteBuffer.allocateDirect(vertexes.length*4);
        qbb.order(ByteOrder.nativeOrder());
        buffer = qbb.asFloatBuffer();
        buffer.put(vertexes);
        buffer.position(0);
        return buffer;
    }

}
