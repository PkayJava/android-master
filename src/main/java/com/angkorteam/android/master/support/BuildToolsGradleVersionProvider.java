package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/com.android.tools.build/gradle?repo=google
 * com.android.tools.build:gradle
 */
@Component
public class BuildToolsGradleVersionProvider extends ValueProviderSupport {

    public static final String V_7_0_0_ALPHA15 = "7.0.0-alpha15";

    public static final String V_7_0_0_BETA03 = "7.0.0-beta03";

    public static final String SELECTED = V_7_0_0_BETA03;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA15));
        proposal.add(new CompletionProposal(V_7_0_0_BETA03));
        return proposal;
    }

}