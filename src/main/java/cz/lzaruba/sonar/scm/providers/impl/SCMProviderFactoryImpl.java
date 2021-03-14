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
package cz.lzaruba.sonar.scm.providers.impl;

import cz.lzaruba.sonar.scm.providers.SCMProvider;
import cz.lzaruba.sonar.scm.providers.SCMProviderFactory;
import cz.lzaruba.sonar.scm.providers.stash.BitbucketServerSCMProvider;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * @author Lukas Zaruba, lukas.zaruba@gmail.com, 2021
 */
public class SCMProviderFactoryImpl implements SCMProviderFactory {

    private static final List<SCMProvider> PROVIDERS = Arrays.asList(
            new BitbucketServerSCMProvider(),
            new GitHubSCMProvider());

    private final Map<String, SCMProvider> providersMap;

    public SCMProviderFactoryImpl() {
        providersMap = PROVIDERS.stream().collect(toMap(SCMProvider::getId, identity()));
    }

    @Override
    public SCMProvider getProvider(String providerId) {
        SCMProvider provider = providersMap.get(providerId);
        if (provider == null) {
            throw new IllegalArgumentException("There is no provider registered for id " + providerId
                    + ". Available ids are: " + providersMap.keySet());
        }
        return provider;
    }

}
