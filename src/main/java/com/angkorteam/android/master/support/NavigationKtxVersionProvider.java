package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

public class NavigationKtxVersionProvider extends ValueProviderSupport {

    public static final String V_2_3_3 = "2.3.3";

    public static final String V_2_3_4 = "2.3.4";

    public static final String SELECTED = V_2_3_4;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_2_3_3));
        proposal.add(new CompletionProposal(V_2_3_4));
        return proposal;
    }

}