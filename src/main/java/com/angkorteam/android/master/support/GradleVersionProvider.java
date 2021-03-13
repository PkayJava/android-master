package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

public class GradleVersionProvider extends ValueProviderSupport {

    public static final String V_6_8_2 = "6.8.2";

    public static final String V_6_8_3 = "6.8.3";

    public static final String SELECTED = V_6_8_3;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_6_8_2));
        proposal.add(new CompletionProposal(V_6_8_3));
        return proposal;
    }

}