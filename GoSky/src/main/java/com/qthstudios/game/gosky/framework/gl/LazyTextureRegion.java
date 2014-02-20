package com.qthstudios.game.gosky.framework.gl;

/**
 * Created by Dinh Quang Trung on 2/20/14.
 */
public class LazyTextureRegion {
    public final float u1, v1;
    public final float u2, v2;
    public final LazyTexture texture;

    public LazyTextureRegion(LazyTexture texture, float x, float y, float width, float height) {
        this.u1 = x / texture.width;
        this.v1 = y / texture.height;
        this.u2 = this.u1 + width / texture.width;
        this.v2 = this.v1 + height / texture.height;
        this.texture = texture;
    }
}
