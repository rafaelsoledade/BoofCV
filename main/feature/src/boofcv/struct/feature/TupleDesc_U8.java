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

package boofcv.struct.feature;


/**
 * Basic description of an image feature's attributes using an array.
 *
 * @author Peter Abeles
 */
public class TupleDesc_U8 implements TupleDesc<TupleDesc_U8> {
	public byte value[];

	public TupleDesc_U8(int numFeatures) {
		this.value = new byte[ numFeatures ];
	}

	protected TupleDesc_U8() {
	}

	public void set( byte ...value ) {
		System.arraycopy(value,0,this.value,0,this.value.length);
	}

	public TupleDesc_U8 copy() {
		TupleDesc_U8 ret = new TupleDesc_U8( value.length );
		System.arraycopy(value,0,ret.value,0,value.length);
		return ret;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	public void setTo(TupleDesc_U8 source) {
		System.arraycopy(source.value,0,value,0,value.length);
	}

	@Override
	public double getDouble(int index) {
		return value[index] & 0xFF;
	}

	@Override
	public int size() {
		return value.length;
	}
}
