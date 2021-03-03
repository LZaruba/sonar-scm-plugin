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
package cz.lzaruba.sonar.scm;

import com.github.difflib.unifieddiff.UnifiedDiff;
import com.github.difflib.unifieddiff.UnifiedDiffReader;
import cz.lzaruba.sonar.scm.model.Analysis;
import cz.lzaruba.sonar.scm.providers.SCMProviderFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class AnalysisProcessor {

    private static final Logger LOG = Loggers.get(AnalysisProcessor.class);
    private static final SCMProviderFactory PROVIDER_FACTORY = new SCMProviderFactory();

    public void process(Analysis analysis) {
        LOG.info(analysis.toString());
        String diff = PROVIDER_FACTORY.getProvider("github").getDiff(analysis.getProperties());
        ByteArrayInputStream is = new ByteArrayInputStream(diff.getBytes(StandardCharsets.UTF_8));
        try {
            UnifiedDiff d = UnifiedDiffReader.parseUnifiedDiff(is);
            d.getFiles().forEach(f -> f.getPatch().getDeltas().forEach(delta -> {
                List<Integer> positions = delta.getTarget().getChangePosition();
                LOG.info(f.getToFile() + ": " + positions);
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
