package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * com.google.dagger:hilt-android-gradle-plugin
 */
@Component
public class HiltPluginVersionProvider extends ValueProviderSupport {

    public static final String V_2_32_ALPHA = "2.32-alpha";

    public static final String V_2_33_BETA = "2.33-beta";

    public static final String V_2_35_1 = "2.35.1";

    public static final String SELECTED = V_2_32_ALPHA;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_2_32_ALPHA));
        proposal.add(new CompletionProposal(V_2_33_BETA));
        proposal.add(new CompletionProposal(V_2_35_1));
        return proposal;
    }

}