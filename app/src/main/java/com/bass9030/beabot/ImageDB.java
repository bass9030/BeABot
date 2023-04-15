package com.bass9030.beabot;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ImageDB {
    private Bitmap ProfileImage;
    private Bitmap Image;

    public ImageDB(Bitmap ProfileImage, Bitmap Image) {
        this.ProfileImage = ProfileImage;
        this.Image = Image;
    }

    public String getProfileImage() {
        return Bitmap2Base64(this.ProfileImage);
    }

    public Bitmap getProfileImageBitmap() {
        return this.ProfileImage;
    }

    public Bitmap getProfileBitmap() {
        return this.ProfileImage;
    }

    public String getProfileHash() {
        return String.valueOf(this.getProfileImage().hashCode());
    }

    public String getImage() {
        return this.Image != null ? Bitmap2Base64(this.Image) : null;
    }

    public Bitmap getImageBitmap() {
        return this.Image;
    }

    private String Bitmap2Base64(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
}
