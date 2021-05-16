package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/com.google.accompanist/accompanist-glide
 * com.google.accompanist:accompanist-glide
 */
@Component
public class AccompanistGlideVersionProvider extends ValueProviderSupport {

    public static final String V_0_7_0 = "0.7.0";

    public static final String V_0_7_1 = "0.7.1";

    public static final String V_0_8_0 = "0.8.0";

    public static final String V_0_8_1 = "0.8.1";

    public static final String V_0_9_0 = "0.9.0";

    public static final String V_0_9_1 = "0.9.1";

    public static final String SELECTED = V_0_9_1;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_0_7_0));
        proposal.add(new CompletionProposal(V_0_7_1));
        proposal.add(new CompletionProposal(V_0_8_0));
        proposal.add(new CompletionProposal(V_0_8_1));
        proposal.add(new CompletionProposal(V_0_9_0));
        proposal.add(new CompletionProposal(V_0_9_1));
        return proposal;
    }

}