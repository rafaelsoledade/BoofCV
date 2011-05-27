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

package gecv.abst.filter.convolve;

import gecv.abst.filter.FilterInterface;
import gecv.struct.convolve.KernelBase;
import gecv.struct.image.ImageBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Generalized interface for filtering images with convolution kernels while skipping pixels.
 * Can invoke different techniques for handling image borders.
 *
 * @author Peter Abeles
 */
public class GenericConvolveDown<Input extends ImageBase, Output extends ImageBase>
	implements FilterInterface<Input,Output>
{
	Method m;
	KernelBase kernel;
	BorderType type;
	int skip;

	public GenericConvolveDown(Method m, KernelBase kernel, BorderType type, int skip ) {
		this.m = m;
		this.kernel = kernel;
		this.type = type;
		this.skip = skip;
	}

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	@Override
	public void process(Input input, Output output) {
		try {
			m.invoke(null,kernel,input,output,skip);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getHorizontalBorder() {
		if( type == BorderType.SKIP)
			return kernel.getRadius();
		else
			return 0;
	}

	@Override
	public int getVerticalBorder() {
		return getHorizontalBorder();
	}
}
