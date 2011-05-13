/*
 * Copyright 2011 Peter Abeles
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package gecv.alg.filter.derivative;

import gecv.alg.InputSanityCheck;
import gecv.alg.filter.convolve.border.ConvolveJustBorder_General;
import gecv.alg.filter.derivative.impl.GradientSobel_Outer;
import gecv.alg.filter.derivative.impl.GradientSobel_UnrolledOuter;
import gecv.alg.filter.derivative.impl.HessianSobel_Shared;
import gecv.core.image.border.ImageBorderExtended;
import gecv.struct.convolve.Kernel2D_F32;
import gecv.struct.convolve.Kernel2D_I32;
import gecv.struct.image.ImageFloat32;
import gecv.struct.image.ImageSInt16;
import gecv.struct.image.ImageUInt8;


/**
 * <p>
 * Computes the second derivative (Hessian) of an image using.  This hessian is derived using the {@link GradientSobel}
 * gradient function.
 * </p>
 *
 * <p>
 *
 * Kernel for &part; <sup>2</sup>f/&part; y<sup>2</sup>:
 * <table border="1">
 * <tr> <td> 1 </td> <td> 4 </td> <td> 6 </td> <td> 4 </td> <td> 1 </td> </tr>
 * <tr> <td> 0 </td> <td> 0 </td> <td> 0 </td> <td> 0 </td> <td> 0 </td> </tr>
 * <tr> <td> -2 </td> <td> -8 </td> <td> -12 </td> <td> -8 </td> <td> -2 </td> </tr>
 * <tr> <td> 0 </td> <td> 0 </td> <td> 0 </td> <td> 0 </td> <td> 0 </td> </tr>
 * <tr> <td> 1 </td> <td> 4 </td> <td> 6 </td> <td> 4 </td> <td> 1 </td> </tr>
 * </table}
 * [1 0 -2 0 1] and &part;<sup>2</sup>f/&part; x&part;y is:<br>
 * <table border="1">
 * <tr> <td> 1 </td> <td> 2 </td> <td> 0 </td> <td> -2 </td> <td> -1 </td> </tr>
 * <tr> <td> 2 </td> <td> 4 </td> <td> 0 </td> <td> -4 </td> <td> -2 </td> </tr>
 * <tr> <td> 0 </td> <td> 0 </td> <td> 0 </td> <td>  0 </td> <td>  0 </td> </tr>
 * <tr> <td> -2 </td> <td> -4 </td> <td> 0 </td> <td> 4 </td> <td> 2 </td> </tr>
 * <tr> <td> -1 </td> <td> -2 </td> <td> 0 </td> <td> 2 </td> <td> 1 </td> </tr>
 * </table}
 * </p>
 *
 * @author Peter Abeles
 */
public class HessianSobel {

	public static Kernel2D_I32 kernelYY_I32 = new Kernel2D_I32(new int[]
			{1, 4, 6 , 4, 1,
			 0, 0, 0 , 0, 0,
			-2,-8,-12,-8,-2,
		     0, 0, 0 , 0, 0,
		     1, 4, 6 , 4, 1},5);
	public static Kernel2D_I32 kernelXX_I32 = new Kernel2D_I32(new int[]
			{1, 0,-2 , 0, 1,
			 4, 0,-8 , 0, 4,
		     6, 0,-12, 0, 6,
		     4, 0,-8 , 0, 4,
			 1, 0,-2 , 0, 1},5);
	public static Kernel2D_I32 kernelXY_I32 = new Kernel2D_I32(new int[]
			{1, 2,0,-2,-1,
			 2, 4,0,-4,-2,
		     0, 0,0, 0, 0,
		    -2,-4,0, 4, 2,
			-1,-2,0, 2, 1},5);
	public static Kernel2D_F32 kernelYY_F32 = new Kernel2D_F32(new float[]
			{1, 4, 6 , 4, 1,
			 0, 0, 0 , 0, 0,
			-2,-8,-12,-8,-2,
		     0, 0, 0 , 0, 0,
		     1, 4, 6 , 4, 1},5);
	public static Kernel2D_F32 kernelXX_F32 = new Kernel2D_F32(new float[]
			{1, 0,-2 , 0, 1,
			 4, 0,-8 , 0, 4,
		     6, 0,-12, 0, 6,
		     4, 0,-8 , 0, 4,
			 1, 0,-2 , 0, 1},5);
	public static Kernel2D_F32 kernelXY_F32 = new Kernel2D_F32(new float[]
			{1, 2,0,-2,-1,
			 2, 4,0,-4,-2,
		     0, 0,0, 0, 0,
		    -2,-4,0, 4, 2,
			-1,-2,0, 2, 1},5);

	/**
	 * Computes the image's second derivatives.
	 *
	 * @param orig   Which which is to be differentiated. Not Modified.
	 * @param derivXX Second derivative along the x-axis. Modified.
	 * @param derivYY Second derivative along the y-axis. Modified.
	 * @param derivXY Second cross derivative. Modified.
	 * @param processBorder If the image's border is processed or not.
	 */
	public static void process( ImageUInt8 orig,
								ImageSInt16 derivXX, ImageSInt16 derivYY, ImageSInt16 derivXY ,
								boolean processBorder ) {
		InputSanityCheck.checkSameShape(orig, derivXX, derivYY, derivXY);
		HessianSobel_Shared.process(orig, derivXX, derivYY, derivXY);

		if( processBorder ) {
			ConvolveJustBorder_General.convolve(kernelXX_I32, ImageBorderExtended.wrap(orig),derivXX,2);
			ConvolveJustBorder_General.convolve(kernelYY_I32, ImageBorderExtended.wrap(orig),derivYY,2);
			ConvolveJustBorder_General.convolve(kernelXY_I32, ImageBorderExtended.wrap(orig),derivXY,2);
		}
	}

	/**
	 * Computes the image's second derivatives.
	 *
	 * @param orig   Which which is to be differentiated. Not Modified.
	 * @param derivXX Second derivative along the x-axis. Modified.
	 * @param derivYY Second derivative along the y-axis. Modified.
	 * @param derivXY Second cross derivative. Modified.
	 * @param processBorder If the image's border is processed or not.
	 */
	public static void process( ImageFloat32 orig,
								ImageFloat32 derivXX, ImageFloat32 derivYY, ImageFloat32 derivXY ,
								boolean processBorder ) {
		InputSanityCheck.checkSameShape(orig, derivXX, derivYY, derivXY);
		HessianSobel_Shared.process(orig, derivXX, derivYY, derivXY);

		if( processBorder ) {
			ConvolveJustBorder_General.convolve(kernelXX_F32, ImageBorderExtended.wrap(orig),derivXX,2);
			ConvolveJustBorder_General.convolve(kernelYY_F32, ImageBorderExtended.wrap(orig),derivYY,2);
			ConvolveJustBorder_General.convolve(kernelXY_F32, ImageBorderExtended.wrap(orig),derivXY,2);
		}
	}
}