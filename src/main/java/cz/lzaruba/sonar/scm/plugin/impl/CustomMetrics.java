/*
 * Copyright 2021 Lukas Zaruba, lukas.zaruba@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.lzaruba.sonar.scm.plugin.impl;


import org.sonar.api.measures.Metric;
import org.sonar.api.measures.Metrics;

import java.util.Arrays;
import java.util.List;

/**
 * Provides necessary fake metric to enable the {@link IssueCollectorExtension}
 *
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class CustomMetrics  implements Metrics {

    public static final String METRIC_KEY = "sonar-scm-plugin:issue-collector-metric";

    @Override
    public List<Metric> getMetrics() {
        return Arrays.asList(new Metric.Builder(METRIC_KEY, "Issue Collector Metric", Metric.ValueType.INT).create());
    }

}
