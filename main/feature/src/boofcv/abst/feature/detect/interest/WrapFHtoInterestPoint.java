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

package boofcv.abst.feature.detect.interest;

import boofcv.alg.feature.detect.interest.FastHessianFeatureDetector;
import boofcv.alg.feature.orientation.OrientationIntegral;
import boofcv.alg.transform.ii.GIntegralImageOps;
import boofcv.struct.feature.ScalePoint;
import boofcv.struct.image.ImageSingleBand;
import georegression.struct.point.Point2D_F64;

import java.util.List;


/**
 * Wrapper around {@link boofcv.alg.feature.detect.interest.FastHessianFeatureDetector} for {@link InterestPointDetector}.
 *
 * @author Peter Abeles
 */
public class WrapFHtoInterestPoint<T extends ImageSingleBand, II extends ImageSingleBand> implements InterestPointDetector<T> {

	// optionally will compute the feature's orientation
	OrientationIntegral<II> orientation;
	// detects the feature's location and scale
	FastHessianFeatureDetector<II> detector;
	List<ScalePoint> location;
	II integral;

	public WrapFHtoInterestPoint(FastHessianFeatureDetector<II> detector) {
		this.detector = detector;
	}

	public WrapFHtoInterestPoint(FastHessianFeatureDetector<II> detector,
								 OrientationIntegral<II> orientation ) {
		this.detector = detector;
		this.orientation = orientation;
	}

	@Override
	public void detect(T input) {
		if( integral != null ) {
			integral.reshape(input.width,input.height);
		}

		integral = GIntegralImageOps.transform(input,integral);

		detector.detect(integral);
		if( orientation != null )
			orientation.setImage(integral);

		location = detector.getFoundPoints();
	}

	@Override
	public int getNumberOfFeatures() {
		return location.size();
	}

	@Override
	public Point2D_F64 getLocation(int featureIndex) {
		return location.get(featureIndex);
	}

	@Override
	public double getScale(int featureIndex) {
		return location.get(featureIndex).scale;
	}

	@Override
	public double getOrientation(int featureIndex) {
		if( orientation != null ) {
			Point2D_F64 p = location.get(featureIndex);
			orientation.setScale(location.get(featureIndex).scale);
			return orientation.compute(p.x,p.y);
		}
		return 0;
	}

	@Override
	public double getCanonicalRadius() {
		return detector.getSmallestWidth()/2;
	}

	@Override
	public boolean hasScale() {
		return true;
	}

	@Override
	public boolean hasOrientation() {
		return orientation != null;
	}
}
