package sj.android.particles;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2015/8/9.
 */
public class ParticlesRenderer implements GLSurfaceView.Renderer {
    private Context context;

    public ParticlesRenderer(Context context) {
        this.context = context;
    }

    private final float angleVarianceInDegrees = 5f;
    private final float speedVariance = 1f;
    private ParticleShaderProgram particleShaderProgram;
    private ParticleSystem particleSystem;
    private ParticleShooter redParticleShooter;
    private ParticleShooter greenParticleShooter;
    private ParticleShooter blueParticleShooter;

    private long globalStartTime;
    private int texture;

    private SkyboxShaderProgram skyboxProgram;
    private Skybox skybox;
    private int skyboxTexture;
    private HeightmapShaderProgram heightmapProgram;
    private Heightmap heightmap;

    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewMatrixforSkybox = new float[16];
    private final float[] projectionMatrix = new float[16];

    private final float[] tempMatrix = new float[16];
    private final float[] modeViewProjectionMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        particleShaderProgram = new ParticleShaderProgram(context);
        particleSystem = new ParticleSystem(10000);
        globalStartTime = System.nanoTime();

        heightmapProgram = new HeightmapShaderProgram(context);
        heightmap = new Heightmap(((BitmapDrawable) context.getResources().getDrawable(R.drawable.heightmap)).getBitmap());

        final Geometry.Vector particleDirection = new Geometry.Vector(0f, 0.5f, 0f);

        redParticleShooter = new ParticleShooter(new Geometry.Point(-1f, 0f, 0f), particleDirection, Color.rgb(255, 50, 5), angleVarianceInDegrees, speedVariance);
        greenParticleShooter = new ParticleShooter(new Geometry.Point(0f, 0f, 0f), particleDirection, Color.rgb(25, 255, 25), angleVarianceInDegrees, speedVariance);
        blueParticleShooter = new ParticleShooter(new Geometry.Point(1f, 0f, 0f), particleDirection, Color.rgb(5, 50, 255), angleVarianceInDegrees, speedVariance);

        texture = TextureHelper.loadTexture(context, R.drawable.particle_texture);

        skyboxProgram = new SkyboxShaderProgram(context);
        skybox = new Skybox();
        skyboxTexture = TextureHelper.loadCubeMap(context, new int[]{R.drawable.left, R.drawable.right,
                R.drawable.bottom, R.drawable.top,
                R.drawable.front, R.drawable.back});
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 100f);
        updateViewMatrices();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        drawSkybox();
        drawHeightmap();
        drawParticles();
    }

    private void drawSkybox() {
//        Matrix.setIdentityM(viewMatrix, 0);
//        Matrix.rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
//        Matrix.rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
//        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.setIdentityM(modelMatrix, 0);
        updateMvpMatrixForSkybox();
        //如何不使用GL_LEQUAL，会看不到天空盒。但是由于浮点数的精度问题，它还是让天空盒的某些部分显示出来了。
        GLES20.glDepthFunc(GLES20.GL_LEQUAL); // This avoids problems with the skybox itself getting clipped.
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(modeViewProjectionMatrix, skyboxTexture);
        skybox.bindData(skyboxProgram);
        skybox.draw();
        GLES20.glDepthFunc(GLES20.GL_LESS);

    }

    private void drawParticles() {
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;

        redParticleShooter.addParticles(particleSystem, currentTime, 1);
        greenParticleShooter.addParticles(particleSystem, currentTime, 1);
        blueParticleShooter.addParticles(particleSystem, currentTime, 1);

//        Matrix.setIdentityM(viewMatrix, 0);
//        Matrix.rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
//        Matrix.rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
//        Matrix.translateM(viewMatrix, 0, 0f, -1.5f, -5f);
//        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        Matrix.setIdentityM(modelMatrix, 0);
        updateMvpMatrix();
        GLES20.glDepthMask(false);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
        particleShaderProgram.useProgram();
        particleShaderProgram.setUniforms(modeViewProjectionMatrix, currentTime, texture);
        particleSystem.bindData(particleShaderProgram);
        particleSystem.draw();
        GLES20.glDisable(GLES20.GL_BLEND);
        GLES20.glDepthMask(true);

    }

    private void drawHeightmap() {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.scaleM(modelMatrix, 0, 100f, 10f, 100f);
        updateMvpMatrix();
        heightmapProgram.useProgram();
        heightmapProgram.setUniforms(modeViewProjectionMatrix);
        heightmap.bindData(heightmapProgram);
        heightmap.draw();
    }

    private float xRotation, yRotation;

    public void handleTouchDrag(float deltaX, float deltaY) {
        xRotation += deltaX / 16f;
        yRotation += deltaY / 16f;
        if (yRotation < -90) {
            yRotation = -90;
        } else if (yRotation > 90) {
            yRotation = 90;
        }
        updateViewMatrices();
    }

    private void updateViewMatrices() {
        Matrix.setIdentityM(viewMatrix, 0);
        Matrix.rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        Matrix.rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        System.arraycopy(viewMatrix, 0, viewMatrixforSkybox, 0, viewMatrix.length);
        Matrix.translateM(viewMatrix, 0, 0, -1.5f, -5f);
    }

    private void updateMvpMatrix() {
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(modeViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
    }

    private void updateMvpMatrixForSkybox() {
        Matrix.multiplyMM(tempMatrix, 0, viewMatrixforSkybox, 0, modelMatrix, 0);
        Matrix.multiplyMM(modeViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
    }
}
