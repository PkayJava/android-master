package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * https://mvnrepository.com/artifact/androidx.compose.ui/ui?repo=space-public-compose-dev
 */
@Component
public class ComposeVersionProvider extends ValueProviderSupport {

    public static final String V_1_0_0_BETA01 = "1.0.0-beta01";

    public static final String V_1_0_0_BETA02 = "1.0.0-beta02";

    public static final String V_1_0_0_BETA03 = "1.0.0-beta03";

    public static final String V_1_0_0_BETA04 = "1.0.0-beta04";

    public static final String V_1_0_0_BETA05 = "1.0.0-beta05";

    public static final String V_1_0_0_BETA06 = "1.0.0-beta06";

    public static final String SELECTED = V_1_0_0_BETA06;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_1_0_0_BETA01));
        proposal.add(new CompletionProposal(V_1_0_0_BETA02));
        proposal.add(new CompletionProposal(V_1_0_0_BETA03));
        proposal.add(new CompletionProposal(V_1_0_0_BETA04));
        proposal.add(new CompletionProposal(V_1_0_0_BETA05));
        proposal.add(new CompletionProposal(V_1_0_0_BETA06));
        return proposal;
    }

}