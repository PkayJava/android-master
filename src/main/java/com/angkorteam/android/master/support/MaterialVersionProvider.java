package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MaterialVersionProvider extends ValueProviderSupport {

    public static final String V_1_3_0 = "1.3.0";

    public static final String V_1_4_0_alpha02 = "1.4.0-alpha02";

    public static final String SELECTED = V_1_3_0;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_3_0));
        proposal.add(new CompletionProposal(V_1_4_0_alpha02));
        return proposal;
    }

}