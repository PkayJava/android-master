package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

public class BuildToolGradleVersionProvider extends ValueProviderSupport {

    public static final String V_7_0_0_ALPHA08 = "7.0.0-alpha08";

    public static final String SELECTED = V_7_0_0_ALPHA08;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_7_0_0_ALPHA08));
        return proposal;
    }

}