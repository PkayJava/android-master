package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gradle.org/releases/
 */
@Component
public class GradleVersionProvider extends ValueProviderSupport {

    public static final String V_6_8_2 = "6.8.2";

    public static final String V_6_8_3 = "6.8.3";

    public static final String V_7_0 = "7.0";

    public static final String V_7_0_1 = "7.0.1";

    public static final String V_7_0_2 = "7.0.2";

    public static final String SELECTED = V_7_0_2;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_6_8_2));
        proposal.add(new CompletionProposal(V_6_8_3));
        proposal.add(new CompletionProposal(V_7_0));
        proposal.add(new CompletionProposal(V_7_0_1));
        proposal.add(new CompletionProposal(V_7_0_2));
        return proposal;
    }

}