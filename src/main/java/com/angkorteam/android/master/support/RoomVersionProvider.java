package com.angkorteam.android.master.support;

import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;

import java.util.ArrayList;
import java.util.List;

public class RoomVersionProvider extends ValueProviderSupport {

    public static final String V_2_3_0_BETA02 = "2.3.0-beta02";

    public static final String V_2_3_0_BETA03 = "2.3.0-beta03";

    public static final String V_2_3_0_RC01 = "2.3.0-rc01";

    public static final String SELECTED = V_2_3_0_RC01;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        List<CompletionProposal> proposal = new ArrayList<>();
        proposal.add(new CompletionProposal(V_2_3_0_BETA02));
        proposal.add(new CompletionProposal(V_2_3_0_BETA03));
        proposal.add(new CompletionProposal(V_2_3_0_RC01));
        return proposal;
    }

}