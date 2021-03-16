package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * com.android.tools.build:gradle
 */
public class BuildToolGradleVersionProvider extends ValueProviderSupport {

    public static final String V_7_0_0_ALPHA08 = "7.0.0-alpha08";

    public static final String V_7_0_0_ALPHA09 = "7.0.0-alpha09";

    public static final String V_7_0_0_ALPHA10 = "7.0.0-alpha10";

    public static final String SELECTED = V_7_0_0_ALPHA10;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA08));
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA09));
        return proposal;
    }

}