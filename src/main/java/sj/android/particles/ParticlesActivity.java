package sj.android.particles;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2015/8/9.
 */
public class ParticlesActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final GLSurfaceView glSurfaceView = new GLSurfaceView(this);
        glSurfaceView.setEGLContextClientVersion(2);
        final ParticlesRenderer airHockeyRenderer = new ParticlesRenderer(this);
        glSurfaceView.setRenderer(airHockeyRenderer);
        setContentView(glSurfaceView);
    }

}
