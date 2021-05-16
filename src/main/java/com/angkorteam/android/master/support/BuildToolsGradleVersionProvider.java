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

    public static final String V_7_0_0_ALPHA08 = "7.0.0-alpha08";

    public static final String V_7_0_0_ALPHA09 = "7.0.0-alpha09";

    public static final String V_7_0_0_ALPHA10 = "7.0.0-alpha10";

    public static final String V_7_0_0_ALPHA11 = "7.0.0-alpha11";

    public static final String V_7_0_0_ALPHA12 = "7.0.0-alpha12";

    public static final String V_7_0_0_ALPHA13 = "7.0.0-alpha13";

    public static final String V_7_0_0_ALPHA14 = "7.0.0-alpha14";

    public static final String V_7_0_0_ALPHA15 = "7.0.0-alpha15";

    public static final String SELECTED = V_7_0_0_ALPHA15;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA08));
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA09));
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA10));
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA11));
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA12));
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA13));
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA14));
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA15));
        return proposal;
    }

}