/*
 * Copyright (c) 2011, Peter Abeles. All Rights Reserved.
 *
 * This file is part of BoofCV (http://www.boofcv.org).
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

package boofcv.alg.feature.orientation.stability;

import boofcv.abst.feature.detect.interest.InterestPointDetector;
import boofcv.abst.filter.derivative.ImageGradient;
import boofcv.alg.feature.benchmark.distort.BenchmarkFeatureDistort;
import boofcv.alg.feature.benchmark.distort.CompileImageResults;
import boofcv.alg.feature.benchmark.distort.FactoryBenchmarkFeatureDistort;
import boofcv.factory.filter.derivative.FactoryDerivative;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageSInt16;
import boofcv.struct.image.ImageUInt8;


/**
 * @author Peter Abeles
 */
public class BenchmarkStabilityOrientation<T extends ImageBase, D extends ImageBase> {

	int randSeed = 234234;
	Class<T> imageType;
	Class<D> derivType;

	int border = 10;
	int radius = 8;

	public BenchmarkStabilityOrientation(Class<T> imageType, Class<D> derivType) {
		this.imageType = imageType;
		this.derivType = derivType;
	}

	public void testNoise() {
		BenchmarkFeatureDistort<T> benchmark =
				FactoryBenchmarkFeatureDistort.noise(imageType);
		perform(benchmark);
	}

	public void testIntensity() {
		BenchmarkFeatureDistort<T> benchmark =
				FactoryBenchmarkFeatureDistort.intensity(imageType);
		perform(benchmark);
	}

	public void testRotation() {
		BenchmarkFeatureDistort<T> benchmark =
				FactoryBenchmarkFeatureDistort.rotate(imageType);
		perform(benchmark);
	}

	public void testScale() {
		BenchmarkFeatureDistort<T> benchmark =
				FactoryBenchmarkFeatureDistort.scale(imageType);
		perform(benchmark);
	}

	private void perform( BenchmarkFeatureDistort<T> eval ) {
		CompileImageResults<T> compile = new CompileImageResults<T>(eval);
		compile.addImage("evaluation/data/outdoors01.jpg");
		compile.addImage("evaluation/data/indoors01.jpg");
		compile.addImage("evaluation/data/scale/beach01.jpg");
		compile.addImage("evaluation/data/scale/mountain_7p1mm.jpg");
		compile.addImage("evaluation/data/sunflowers.png");

		ImageGradient<T,D> gradient = FactoryDerivative.sobel(imageType,derivType);
		InterestPointDetector<T> detector = UtilOrientationBenchmark.defaultDetector(imageType,derivType);
		OrientationEvaluator<T,D> evaluator = new OrientationEvaluator<T,D>(border,detector,gradient);

		compile.setAlgorithms(UtilOrientationBenchmark.createAlgorithms(radius,imageType,derivType),evaluator);

		compile.process();
	}

	public static void main( String args[] ) {
//		BenchmarkStabilityOrientation<ImageFloat32,ImageFloat32> benchmark
//				= new BenchmarkStabilityOrientation<ImageFloat32,ImageFloat32>(ImageFloat32.class,ImageFloat32.class);
		BenchmarkStabilityOrientation<ImageUInt8, ImageSInt16> benchmark
				= new BenchmarkStabilityOrientation<ImageUInt8,ImageSInt16>(ImageUInt8.class,ImageSInt16.class);

//		benchmark.testNoise();
//		benchmark.testIntensity();
		benchmark.testRotation();
//		benchmark.testScale();

	}
}
