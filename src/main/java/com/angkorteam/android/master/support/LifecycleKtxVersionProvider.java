package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * androidx.lifecycle:lifecycle-runtime-ktx
 */
@Component
public class LifecycleKtxVersionProvider extends ValueProviderSupport {

    public static final String V_2_3_0 = "2.3.0";

    public static final String V_2_3_1 = "2.3.1";

    public static final String V_2_4_0_alpha01 = "2.4.0-alpha01";

    public static final String SELECTED = V_2_3_1;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_2_3_0));
        proposal.add(new CompletionProposal(V_2_3_1));
        proposal.add(new CompletionProposal(V_2_4_0_alpha01));
        return proposal;
    }

}