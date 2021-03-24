package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

public class ComposeVersionProvider extends ValueProviderSupport {

    public static final String V_1_0_0_BETA01 = "1.0.0-beta01";

    public static final String V_1_0_0_BETA02 = "1.0.0-beta02";

    public static final String SELECTED = V_1_0_0_BETA02;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_0_0_BETA01));
        proposal.add(new CompletionProposal(V_1_0_0_BETA02));
        return proposal;
    }

}