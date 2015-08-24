package sj.android.particles;

import android.opengl.GLES20;

/**
 * Created by Administrator on 2015/8/9.
 */
public class ShaderHelper {
    private static final String TAG = "ShaderHelper";

    private static int loadShader(int shaderType, String source) {
        //创建一个新shader
        int shader = GLES20.glCreateShader(shaderType);
        //若创建成功则加载shader
        if (shader != 0) {
            //加载shader的源代码
            GLES20.glShaderSource(shader, source);
            //编译shader
            GLES20.glCompileShader(shader);
            //存放编译成功shader数量的数组
            int[] compiled = new int[1];
            //获取Shader的编译情况
            GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                //若编译失败则显示错误日志并删除此shader
                LogUtil.L(TAG, "Could not compile shader " + shaderType + ":");
                LogUtil.L(TAG, GLES20.glGetShaderInfoLog(shader));
                GLES20.glDeleteShader(shader);
                shader = 0;
            }
        }

        return shader;
    }

    public static int createProgram(String vertexSource, String fragmentSource) {

        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);

        if (vertexShader == 0) {
            LogUtil.L(TAG, "vertexShader == 0");
            return 0;
        }

        //加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (pixelShader == 0) {
            LogUtil.L(TAG, "pixelShader == 0");
            return 0;
        }

        //创建着色器程序

        int program = GLES20.glCreateProgram();

        //若程序创建成功则向程序中加入顶点着色器与片元着色器

        if (program != 0) {

            //向程序中加入顶点着色器

            GLES20.glAttachShader(program, vertexShader);

            //向程序中加入片元着色器

            GLES20.glAttachShader(program, pixelShader);

            //链接程序

            GLES20.glLinkProgram(program);

            //存放链接成功program数量的数组

            int[] linkStatus = new int[1];

            //获取program的链接情况

            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);

            //若链接失败则报错并删除程序

            if (linkStatus[0] != GLES20.GL_TRUE) {
                LogUtil.L(TAG, "Could not link program: ");
                LogUtil.L(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;

            }

        }
        return program;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {

        //创建着色器程序
        int program = GLES20.glCreateProgram();
        //若程序创建成功则向程序中加入顶点着色器与片元着色器
        if (program != 0) {
            //向程序中加入顶点着色器
            GLES20.glAttachShader(program, vertexShaderId);
            //向程序中加入片元着色器
            GLES20.glAttachShader(program, fragmentShaderId);
            //链接程序
            GLES20.glLinkProgram(program);
            //存放链接成功program数量的数组
            int[] linkStatus = new int[1];
            //获取program的链接情况
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            //若链接失败则报错并删除程序
            if (linkStatus[0] != GLES20.GL_TRUE) {
                LogUtil.L(TAG, "Could not link program: ");
                LogUtil.L(TAG, GLES20.glGetProgramInfoLog(program));
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public static int  buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;
        //加载顶点着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderSource);

        if (vertexShader == 0) {
            LogUtil.L(TAG, "vertexShader == 0");
            return 0;
        }

        //加载片元着色器
        int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderSource);
        if (pixelShader == 0) {
            LogUtil.L(TAG, "pixelShader == 0");
            return 0;
        }
        program = linkProgram(vertexShader, pixelShader);
        return program;
    }
}
