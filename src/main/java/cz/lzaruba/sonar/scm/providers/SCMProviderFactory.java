package cz.lzaruba.sonar.scm.providers;

public class SCMProviderFactory {

    public SCMProvider getProvider(String providerId) {
        return new BitbucketSCMProvider();
    }

}
