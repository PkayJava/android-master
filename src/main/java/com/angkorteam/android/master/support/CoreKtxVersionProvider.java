package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/androidx.core/core-ktx
 * androidx.core:core-ktx
 */
@Component
public class CoreKtxVersionProvider extends ValueProviderSupport {

    public static final String V_1_3_2 = "1.3.2";

    public static final String V_1_5_0_BETA01 = "1.5.0-beta01";

    public static final String V_1_6_0_ALPHA02 = "1.6.0-alpha02";

    public static final String V_1_6_0_ALPHA03 = "1.6.0-alpha03";

    public static final String V_1_6_0_BETA01 = "1.6.0-beta01";

    public static final String SELECTED = V_1_6_0_BETA01;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_3_2));
        proposal.add(new CompletionProposal(V_1_5_0_BETA01));
        proposal.add(new CompletionProposal(V_1_6_0_ALPHA02));
        proposal.add(new CompletionProposal(V_1_6_0_ALPHA03));
        proposal.add(new CompletionProposal(V_1_6_0_BETA01));
        return proposal;
    }

}