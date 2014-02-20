package com.qthstudios.game.gosky.framework.gl;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.opengl.GLUtils;
import android.os.Build;
import android.util.Log;

import com.qthstudios.game.gosky.framework.impl.GLGame;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Dinh Quang Trung on 2/19/14.
 */
public class LazyTexture extends Texture {
    public int topOffset = 0;
    public int contextTopOffset = 0;

    public LazyTexture(GLGame glGame, String fileName) {
        super(glGame, fileName);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
    @Override
    protected void load() {
        Log.e("TRUNGDQ", "load: " + topOffset);
        //
        GL10 gl = glGraphics.getGL();
        int[] textureIds = new int[1];
        gl.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];

        InputStream in = null;
        try {
            in = fileIO.readAsset(fileName);
            // Log.e("TRUNGDQ", "LOAD fileName: " + fileName + " | leftOffset: " + leftOffset + " | topOffset: " + topOffset + " | widthOffset: " + widthOffset + " | heightOffset: " + heightOffset);
//            Bitmap bitmap = BitmapRegionDecoder.newInstance(in, false).decodeRegion(
//                    new Rect(leftOffset, topOffset, leftOffset + widthOffset, topOffset + heightOffset), null);
            Bitmap bitmap = BitmapRegionDecoder.newInstance(in, false).decodeRegion(
                    new Rect(0, topOffset, 320, topOffset + 480), null);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            bitmap.recycle();
        } catch(IllegalArgumentException e) {
            e.printStackTrace();
        } catch(IOException e) {
            throw new RuntimeException("Couldn't load texture '" + fileName +"'", e);
        } finally {
            if(in != null)
                try { in.close(); } catch (IOException ignored) { }
        }
    }
}
