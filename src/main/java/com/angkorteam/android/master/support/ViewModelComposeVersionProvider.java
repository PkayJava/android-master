package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

public class ViewModelComposeVersionProvider extends ValueProviderSupport {

    public static final String V_1_0_0_ALPHA02 = "1.0.0-alpha02";

    public static final String SELECTED = V_1_0_0_ALPHA02;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_0_0_ALPHA02));
        return proposal;
    }

}