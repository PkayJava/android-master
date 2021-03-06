package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommonsIOVersionProvider extends ValueProviderSupport {

    public static final String V_2_8_0 = "2.8.0";

    public static final String V_2_9_0 = "2.9.0";

    public static final String SELECTED = V_2_9_0;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_2_8_0));
        proposal.add(new CompletionProposal(V_2_9_0));
        return proposal;
    }

}