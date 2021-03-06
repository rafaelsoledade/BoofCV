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

package boofcv.factory.distort;

import boofcv.alg.distort.ImageDistort;
import boofcv.alg.distort.impl.*;
import boofcv.alg.interpolate.InterpolatePixel;
import boofcv.core.image.border.ImageBorder;
import boofcv.struct.image.*;

/**
 * @author Peter Abeles
 */
public class FactoryDistort {

	/**
	 * Creates a {@link boofcv.alg.distort.ImageDistort} for the specified image type, transformation
	 * and interpolation instance.
	 *
	 * @param interp Which interpolation algorithm should be used.
	 * @param border Specifies how requests to pixels outside the image should be handled.  If null then no change
	 * @param imageType Type of image being processed.
	 */
	public static <T extends ImageSingleBand>
	ImageDistort<T> distort(InterpolatePixel<T> interp, ImageBorder border, Class<T> imageType)
	{
		if( imageType == ImageFloat32.class ) {
			return (ImageDistort<T>)new ImplImageDistort_F32((InterpolatePixel<ImageFloat32>)interp,border);
		} else if( ImageSInt32.class.isAssignableFrom(imageType) ) {
			return (ImageDistort<T>)new ImplImageDistort_S32((InterpolatePixel<ImageSInt32>)interp,border);
		} else if( ImageInt16.class.isAssignableFrom(imageType) ) {
			return (ImageDistort<T>)new ImplImageDistort_I16((InterpolatePixel<ImageInt16>)interp,border);
		} else if( ImageInt8.class.isAssignableFrom(imageType) ) {
			return (ImageDistort<T>)new ImplImageDistort_I8((InterpolatePixel<ImageInt8>)interp,border);
		} else {
			throw new IllegalArgumentException("Image type not supported: "+imageType.getSimpleName());
		}
	}

	/**
	 * Avoid recomputing the distortion map for the entire image each time
	 * by caching the distortion for each pixel.  This can improve speed significantly when the distortion
	 * and output image size are both constant.
	 *
	 * @param interp Which interpolation algorithm should be used.
	 * @param border Specifies how requests to pixels outside the image should be handled.  If null then no change
	 * @param imageType Type of image being processed.
	 * @return Image distort which caches the distortion.
	 */
	public static <T extends ImageSingleBand>
	ImageDistort<T> distortCached(InterpolatePixel<T> interp, ImageBorder border ,
								  Class<T> imageType)
	{
		if( imageType == ImageFloat32.class ) {
			return (ImageDistort<T>)new ImplImageDistortCache_F32((InterpolatePixel<ImageFloat32>)interp,border);
		} else if( ImageSInt32.class.isAssignableFrom(imageType) ) {
			return (ImageDistort<T>)new ImplImageDistortCache_S32((InterpolatePixel<ImageSInt32>)interp,border);
		} else if( ImageInt16.class.isAssignableFrom(imageType) ) {
			return (ImageDistort<T>)new ImplImageDistortCache_I16((InterpolatePixel<ImageInt16>)interp,border);
		} else if( ImageInt8.class.isAssignableFrom(imageType) ) {
			return (ImageDistort<T>)new ImplImageDistortCache_I8((InterpolatePixel<ImageInt8>)interp,border);
		} else {
			throw new IllegalArgumentException("Image type not supported: "+imageType.getSimpleName());
		}
	}
}
