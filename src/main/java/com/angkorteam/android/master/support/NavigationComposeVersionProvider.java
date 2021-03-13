package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

public class NavigationComposeVersionProvider extends ValueProviderSupport {

    public static final String V_1_0_0_ALPHA08 = "1.0.0-alpha08";

    public static final String V_1_0_0_ALPHA09 = "1.0.0-alpha09";

    public static final String SELECTED = V_1_0_0_ALPHA09;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_0_0_ALPHA08));
        proposal.add(new CompletionProposal(V_1_0_0_ALPHA09));
        return proposal;
    }

}