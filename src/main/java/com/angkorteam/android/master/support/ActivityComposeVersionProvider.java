package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

public class ActivityComposeVersionProvider extends ValueProviderSupport {

    public static final String V_1_3_0_ALPHA03 = "1.3.0-alpha03";

    public static final String SELECTED = V_1_3_0_ALPHA03;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_3_0_ALPHA03));
        return proposal;
    }

}