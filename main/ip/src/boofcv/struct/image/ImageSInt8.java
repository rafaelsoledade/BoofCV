/*
 * Copyright (c) 2011-2012, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://boofcv.org).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package boofcv.struct.image;

/**
 * <p>
 * An image where the primitive type is an signed 8-bit byte.
 * </p>
 *
 * @author Peter Abeles
 */
public class ImageSInt8 extends ImageInt8<ImageSInt8> {

	/**
	 * Creates a new gray scale (single band/color) image.
	 *
	 * @param width  number of columns in the image.
	 * @param height number of rows in the image.
	 */
	public ImageSInt8(int width, int height) {
		super(width, height);
	}

	public ImageSInt8() {
	}

	@Override
	public int unsafe_get(int x, int y) {
		return data[getIndex(x, y)];
	}

	@Override
	public ImageTypeInfo<ImageSInt8> getTypeInfo() {
		return ImageTypeInfo.S8;
	}

	@Override
	public ImageSInt8 _createNew(int imgWidth, int imgHeight) {
		if (imgWidth == -1 || imgHeight == -1) {
			return new ImageSInt8();
		}
		return new ImageSInt8(imgWidth, imgHeight);
	}
}
