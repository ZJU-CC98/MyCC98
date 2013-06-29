package tk.djcrazy.libCC98.data;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class Bytes implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private byte[] bytes = null;
	
	public Bytes(byte[] bytes) {
		this.bytes = bytes;
	}
	
	public Bytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, baos);
		bytes = baos.toByteArray();
	}
	
	public Bitmap getBitmap() {
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	public byte[] getBytes() {
		return bytes;
	}

}
