package com.woshidaniu.qrcode.io;


import java.awt.image.BufferedImage;

public class BufferedImageWithLogoLuminanceSource extends BufferedImageLuminanceSource {
	
	private final BufferedImage image;
	private final int left;
	private final int top;

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;
	
	
	public BufferedImageWithLogoLuminanceSource(BufferedImage image) {
		this(image, 0, 0, image.getWidth(), image.getHeight());
	}

	public BufferedImageWithLogoLuminanceSource(BufferedImage image, int left, int top, int width, int height) {
		
		super(image,left,top,width, height);

		int sourceWidth = image.getWidth();
		int sourceHeight = image.getHeight();
		if (left + width > sourceWidth || top + height > sourceHeight) {
			throw new IllegalArgumentException("Crop rectangle does not fit within image data.");
		}

		for (int y = top; y < top + height; y++) {
			for (int x = left; x < left + width; x++) {
				if ((image.getRGB(x, y) & BLACK) == 0) {
					image.setRGB(x, y, WHITE); // = white
				}
			}
		}

		this.image = new BufferedImage(sourceWidth, sourceHeight,BufferedImage.TYPE_BYTE_GRAY);
		this.image.getGraphics().drawImage(image, 0, 0, null);
		this.left = left;
		this.top = top;
	}

	@Override
	public byte[] getRow(int y, byte[] row) {
		if (y < 0 || y >= getHeight()) {
			throw new IllegalArgumentException("Requested row is outside the image: " + y);
		}
		int width = getWidth();
		if (row == null || row.length < width) {
			row = new byte[width];
		}
		image.getRaster().getDataElements(left, top + y, width, 1, row);
		return row;
	}

	@Override
	public byte[] getMatrix() {
		int width = getWidth();
		int height = getHeight();
		int area = width * height;
		byte[] matrix = new byte[area];
		image.getRaster().getDataElements(left, top, width, height, matrix);
		return matrix;
	}
	
	@Override
	public boolean isRotateSupported() {
		return true;
	}

}
