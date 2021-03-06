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

package boofcv.abst.geo;

import boofcv.struct.geo.GeoModelEstimator1;
import boofcv.struct.geo.PointPosePair;
import georegression.struct.se.Se3_F64;

/**
 * Marker interface for computing one solution to the Perspective N-Point (PnP) problem.  Given a set of point
 * observations from a single view and the known 3D location of the points being observed, estimate the camera
 * location.  All observations must be in calibrated image coordinates.
 *
 * @author Peter Abeles
 */
public interface Estimate1ofPnP extends GeoModelEstimator1<Se3_F64,PointPosePair> {
}
