package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/androidx.compose.ui/ui?repo=google
 */
@Component
public class ComposeVersionProvider extends ValueProviderSupport {


    public static final String V_1_0_0_BETA06 = "1.0.0-beta06";

    public static final String V_1_0_0_BETA07 = "1.0.0-beta07";

    public static final String SELECTED = V_1_0_0_BETA07;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_0_0_BETA06));
        proposal.add(new CompletionProposal(V_1_0_0_BETA07));
        return proposal;
    }

}