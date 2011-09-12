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

package boofcv.alg.feature.describe.stability;

import boofcv.abst.feature.detect.interest.InterestPointDetector;
import boofcv.alg.feature.benchmark.BenchmarkAlgorithm;
import boofcv.alg.feature.benchmark.distort.BenchmarkFeatureDistort;
import boofcv.alg.feature.benchmark.distort.CompileImageResults;
import boofcv.alg.feature.benchmark.distort.FactoryBenchmarkFeatureDistort;
import boofcv.alg.feature.benchmark.distort.StabilityEvaluator;
import boofcv.alg.feature.orientation.OrientationNoGradient;
import boofcv.alg.feature.orientation.stability.UtilOrientationBenchmark;
import boofcv.factory.feature.orientation.FactoryOrientationAlgs;
import boofcv.struct.image.ImageBase;
import boofcv.struct.image.ImageFloat32;

import java.util.List;


/**
 * Evalutes
 *
 * @author Peter Abeles
 */
public class BenchmarkStabilityDescribe <T extends ImageBase, D extends ImageBase>
{
	Class<T> imageType;
	Class<D> derivType;

	int border = 13;
	int radius = 12;

	// algorithms which are to be evaluated
	List<BenchmarkAlgorithm> algs;

	public BenchmarkStabilityDescribe(Class<T> imageType, Class<D> derivType) {
		this.imageType = imageType;
		this.derivType = derivType;
		algs = UtilStabilityBenchmark.createAlgorithms(radius,imageType,derivType);
	}

	public List<BenchmarkAlgorithm> getEvaluationAlgs() {
		return algs;
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

	private void perform( BenchmarkFeatureDistort<T> benchmark ) {
		CompileImageResults<T> compile = new CompileImageResults<T>(benchmark);
		compile.addImage("evaluation/data/outdoors01.jpg");
		compile.addImage("evaluation/data/indoors01.jpg");
		compile.addImage("evaluation/data/scale/beach01.jpg");
		compile.addImage("evaluation/data/scale/mountain_7p1mm.jpg");
		compile.addImage("evaluation/data/sunflowers.png");

		InterestPointDetector<T> detector = UtilOrientationBenchmark.defaultDetector(imageType,derivType);
		OrientationNoGradient<T> orientation = FactoryOrientationAlgs.nogradient(radius,imageType);
		// comment/uncomment to change the evaluator
//		StabilityEvaluator<T> evaluator = new DescribeEvaluator<T>(border,detector,orientation);
		StabilityEvaluator<T> evaluator = new DescribeAssociateEvaluator<T>(border,detector,orientation);

		compile.setAlgorithms(algs,evaluator);

		compile.process();
	}

	public static void main( String args[] ) {
		BenchmarkStabilityDescribe<ImageFloat32,ImageFloat32> benchmark
				= new BenchmarkStabilityDescribe<ImageFloat32,ImageFloat32>(ImageFloat32.class, ImageFloat32.class);

//		benchmark.testNoise();
//		benchmark.testIntensity();
		benchmark.testRotation();
//		benchmark.testScale();
	}
}
